package ro.teamnet.bootstrap.config;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.teamnet.bootstrap.extend.AppRepositoryFactoryBean;
import ro.teamnet.bootstrap.plugin.jpa.JpaPackagesToScanPlugin;
import ro.teamnet.bootstrap.plugin.jpa.JpaType;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableJpaRepositories(basePackages = {"ro.teamnet.bootstrap.repository"},
        repositoryFactoryBeanClass = AppRepositoryFactoryBean.class)
@EnableTransactionManagement
@EnablePluginRegistries({JpaPackagesToScanPlugin.class})
public class DatabaseConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    public void setPropertyResolver(RelaxedPropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    private Environment env;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");

    }

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;


    @Bean(name = "dataSource", destroyMethod = "shutdown")
    @ConditionalOnMissingClass(name = "ro.teamnet.renns.config.HerokuDatabaseConfiguration")
    @Profile("!" + Constants.SPRING_PROFILE_CLOUD)
    public DataSource dataSource() {
        log.debug("Configuring Datasource");
        if (propertyResolver.getProperty("url") == null && propertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application" +
                            "cannot start. Please check your Spring profile, current profiles are: {}",
                    Arrays.toString(env.getActiveProfiles()));

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(propertyResolver.getProperty("dataSourceClassName"));
        if (propertyResolver.getProperty("url") == null || "".equals(propertyResolver.getProperty("url"))) {
            config.addDataSourceProperty("databaseName", propertyResolver.getProperty("databaseName"));
            config.addDataSourceProperty("serverName", propertyResolver.getProperty("serverName"));
        } else {
            config.addDataSourceProperty("url", propertyResolver.getProperty("url"));
        }
        config.addDataSourceProperty("user", propertyResolver.getProperty("username"));
        config.addDataSourceProperty("password", propertyResolver.getProperty("password"));

        if (metricRegistry != null) {
            config.setMetricRegistry(metricRegistry);
        }
        return new HikariDataSource(config);
    }

    @Bean(name = "liquibase")
    public SpringLiquibase liquibase(@Qualifier("dataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath*:config/liquibase/master.xml");
        liquibase.setContexts("development, production");
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
            liquibase.setShouldRun(false);
        } else {
            log.debug("Configuring Liquibase");
        }
        return liquibase;
    }

    @Bean
    public Hibernate4Module hibernate4Module() {
        return new Hibernate4Module();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,@Qualifier("jpaPackagesToScanPluginRegistry")
    PluginRegistry<JpaPackagesToScanPlugin,JpaType> jpaPackagesToScanPluginRegistry) {
        List<String> entityPackagesToScan = getEntityPackagesToScan();


        EntityManagerFactoryBuilder.Builder entBuild= builder.dataSource(dataSource())
                .persistenceUnit("default");

        if(jpaPackagesToScanPluginRegistry.hasPluginFor(JpaType.JPA_PACKAGE_TO_SCAN)){
            for (JpaPackagesToScanPlugin jpaPackagesToScanPlugin : jpaPackagesToScanPluginRegistry.getPluginsFor(JpaType.JPA_PACKAGE_TO_SCAN)) {
                if(jpaPackagesToScanPlugin.packagesToScan()!=null){
                    entityPackagesToScan.addAll(jpaPackagesToScanPlugin.packagesToScan());
                }
                if(jpaPackagesToScanPlugin.packageToScan()!=null){
                    entityPackagesToScan.add(jpaPackagesToScanPlugin.packageToScan());
                }

            }
        }
        entBuild.packages(entityPackagesToScan.toArray(new String[entityPackagesToScan.size()]));
        return entBuild.build();
    }

    public List<String> getEntityPackagesToScan() {
        ArrayList<String> entityPackages = new ArrayList<>();
        entityPackages.add("ro.teamnet.bootstrap");
        return entityPackages;
    }

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setDataSource(dataSource());
        return jpaTransactionManager;
    }




}
