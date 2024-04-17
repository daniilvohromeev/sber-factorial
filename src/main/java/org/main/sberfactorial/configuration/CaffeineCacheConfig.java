package org.main.sberfactorial.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Конфигурационный класс для настройки менеджера кэша Caffeine.
 * <p>
 * Этот класс отвечает за создание и настройку {@link CaffeineCacheManager}, который используется
 * для кэширования объектов в приложении. Конфигурация включает параметры времени жизни записей после записи
 * и максимального размера кэша.
 * <p>
 */
@Configuration
public class CaffeineCacheConfig {
    private static final Logger log = LoggerFactory.getLogger(CaffeineCacheConfig.class);

    @Value("${factorial.cache-time}")
    private long expireAfterWriteSeconds;

    @Value("${factorial.cache-size}")
    private long maximumSize;

    @Value("${factorial.cache-name}")
    private String[] cacheNames;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWriteSeconds, TimeUnit.SECONDS)
                .maximumSize(maximumSize);
    }

    @Bean
    public CaffeineCacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheNames);
        cacheManager.setCaffeine(caffeine);
        log.info("Caffeine Cache Manager настроен с именами: {}", Arrays.toString(cacheNames));
        return cacheManager;
    }
}
