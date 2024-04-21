package org.main.sberfactorial.configuration;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Nullable;
import org.main.sberfactorial.component.FactorialCacheLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Value("${factorial.cache-time}")
    private long expireAfterWriteSeconds;

    @Value("${factorial.cache-size}")
    private long maximumSize;

    @Value("${factorial.cache-names}")
    private List<String> cacheNames;

    private final FactorialCacheLoader factorialCacheLoader;

    public CaffeineCacheConfig(@Nullable FactorialCacheLoader factorialCacheLoader) {
        this.factorialCacheLoader = factorialCacheLoader;
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(cacheNames);
        cacheManager.setCaffeine(caffeineConfig());
        if (factorialCacheLoader != null) {
            CacheLoader<Long, BigInteger> loader = factorialCacheLoader;
            cacheManager.setCacheLoader((key) -> loader.load((Long) key));
        }
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteSeconds, TimeUnit.SECONDS)
                .maximumSize(maximumSize);
    }

}
