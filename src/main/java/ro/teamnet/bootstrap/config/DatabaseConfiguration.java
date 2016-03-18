package ro.teamnet.bootstrap.config;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.dbcp2.BasicDataSource;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;
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

import static ro.teamnet.bootstrap.plugin.jpa.JpaType.DEFAULT_JPA_PACKAGE_TO_SCAN;
import static ro.teamnet.bootstrap.plugin.jpa.JpaType.JPA_PACKAGE_TO_SCAN;

/**
 * Spring configuration class.
 * This class will initialize data configuration beans used by Spring.
 */
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

    // JTDS driver
    /*@Bean(name = "dataSource")
    public DataSource dataSource() {
        JtdsDataSource ret = new JtdsDataSource();
        ret.setUser(propertyResolver.getProperty("username"));
        ret.setPassword(propertyResolver.getProperty("password"));
        ret.setServerName(propertyResolver.getProperty("serverName"));
        ret.setPortNumber(Integer.parseInt(propertyResolver.getProperty("portNumber")));
        ret.setDatabaseName(propertyResolver.getProperty("databaseName"));
        return ret;
    }*/

    // MS JDBC driver
    @Bean(name = "dataSource", destroyMethod = "close")
    public DataSource dataSource() {
        BasicDataSource ret = new BasicDataSource();
        ret.setUrl(propertyResolver.getProperty("url"));
        ret.setUsername(propertyResolver.getProperty("username"));
        ret.setPassword(propertyResolver.getProperty("password"));
        ret.setDriverClassName(propertyResolver.getProperty("driverClassName"));
        ret.setMinIdle(Integer.parseInt(propertyResolver.getProperty("minIdleConnections")));
        ret.setMaxIdle(Integer.parseInt(propertyResolver.getProperty("maxIdleConnections")));
        ret.setMinEvictableIdleTimeMillis(Integer.parseInt(propertyResolver.getProperty("minEvictableIdleTime")));
        ret.setMaxConnLifetimeMillis(Integer.parseInt(propertyResolver.getProperty("maxLifeTime")));
        ret.setMaxWaitMillis(Integer.parseInt(propertyResolver.getProperty("maxWaitTime")));
        return ret;
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
            EntityManagerFactoryBuilder builder, @Qualifier("jpaPackagesToScanPluginRegistry")
    PluginRegistry<JpaPackagesToScanPlugin, JpaType> jpaPackagesToScanPluginRegistry) {
        List<String> entityPackagesToScan = getEntityPackagesToScan();

        EntityManagerFactoryBuilder.Builder entBuild = builder.dataSource(dataSource())
                .persistenceUnit("default");

        List<JpaPackagesToScanPlugin> jpaPackagesToScanPlugins = jpaPackagesToScanPluginRegistry.getPluginsFor(
                JPA_PACKAGE_TO_SCAN, jpaPackagesToScanPluginRegistry.getPluginsFor(DEFAULT_JPA_PACKAGE_TO_SCAN));
        for (JpaPackagesToScanPlugin jpaPackagesToScanPlugin : jpaPackagesToScanPlugins) {
            if (jpaPackagesToScanPlugin.packagesToScan() != null) {
                entityPackagesToScan.addAll(jpaPackagesToScanPlugin.packagesToScan());
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
