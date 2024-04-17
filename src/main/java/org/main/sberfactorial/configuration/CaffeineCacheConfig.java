package org.main.sberfactorial.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine", matchIfMissing = true)
public class CaffeineCacheConfig {
    private static final Logger log = LoggerFactory.getLogger(CaffeineCacheConfig.class);

    @Value("${spring.cache.caffeine.spec.expireAfterWrite}")
    private long expireAfterWriteSeconds;

    @Value("${spring.cache.caffeine.spec.maximumSize}")
    private long maximumSize;

    @Bean
    public CaffeineCacheManager cacheManager() {
        log.info("Configuring Caffeine Cache: expireAfterWrite={} seconds, maximumSize={}", expireAfterWriteSeconds, maximumSize);
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteSeconds, TimeUnit.SECONDS)
                .maximumSize(maximumSize);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        log.info("Caffeine Cache Manager is set up.");
        return cacheManager;
    }
}