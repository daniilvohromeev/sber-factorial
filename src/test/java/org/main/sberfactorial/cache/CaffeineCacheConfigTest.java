package org.main.sberfactorial.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CaffeineCacheConfigTest.TestConfig.class)
public class CaffeineCacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private Ticker ticker;

    @Configuration
    static class TestConfig {

        @Bean
        public CaffeineCacheManager cacheManager() {
            Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                    .expireAfterWrite(60, TimeUnit.SECONDS)
                    .ticker(ticker());
            CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
            caffeineCacheManager.setCaffeine(caffeine);
            return caffeineCacheManager;
        }

        @Bean
        public Ticker ticker() {
            // Возвращаем мокируемый Ticker
            return Ticker.systemTicker();
        }
    }

    @Test
    public void cacheShouldExpireAfterConfiguredTime() {
        String cacheName = "testCache";
        var cache = cacheManager.getCache(cacheName);
        assertNotNull(cache);

        cache.put("key", "value");

        long timeAdvance = TimeUnit.SECONDS.toNanos(61);
        when(ticker.read()).thenReturn(timeAdvance);

        assertNull(cache.get("key"));
    }
}
