package ro.teamnet.bootstrap.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ro.teamnet.bootstrap.extend.AppFilterHandlerMethodArgumentResolver;
import ro.teamnet.bootstrap.extend.AppLocaleHandlerMethodArgumentResolver;
import ro.teamnet.bootstrap.extend.AppPageableHandlerMethodArgumentResolver;
import ro.teamnet.bootstrap.extend.AppSortHandlerMethodArgumentResolver;
import ro.teamnet.bootstrap.holder.SessionHolder;

/**
 * Spring configuration class.
 * This class will initialize MVC beans used by Spring.
 */
@Configuration("appMvcConfig")
public class AppMvcConfig {

    @Bean
    public AppPageableHandlerMethodArgumentResolver pageableResolver() {
        return new AppPageableHandlerMethodArgumentResolver(appSortResolver(), appFilterPlusResolver(), appLocaleResolver());
    }

    @Bean
    public AppSortHandlerMethodArgumentResolver appSortResolver() {
        return new AppSortHandlerMethodArgumentResolver();
    }

    @Bean
    public AppFilterHandlerMethodArgumentResolver appFilterPlusResolver() {
        return new AppFilterHandlerMethodArgumentResolver();
    }

    @Bean
    public AppLocaleHandlerMethodArgumentResolver appLocaleResolver() {
        return new AppLocaleHandlerMethodArgumentResolver();
    }


    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionHolder sessionHolder() {
        return new SessionHolder();
    }





}
