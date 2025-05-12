package co.com.redeban.cache.config;

import co.com.redeban.model.configmol.ConfigMol;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Value("${durationExpire}")
    private Long durationExpire;

    @Value("${maximumSize}")
    private Long maximumSize;


    @Bean
    public Cache<String, ConfigMol> configMolCache() {
        return Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(durationExpire, TimeUnit.SECONDS)
                .build();
    }
}

