package ro.teamnet.bootstrap.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ro.teamnet.bootstrap.web.rest.dto.UploadFileLogDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Radu.Hoaghe on 7/20/2015.
 */
@Configuration
@EnableCaching
public class FileUploadTokenConfiguration {

    @Bean
    @Scope(value = "singleton")
    @Qualifier("tokens")
    public Map<String, Object> tokens(){
        return new ConcurrentHashMap<>();
    }
}
