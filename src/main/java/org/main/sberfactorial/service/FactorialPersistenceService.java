package org.main.sberfactorial.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.main.sberfactorial.repository.FactorialRepository;
import org.main.sberfactorial.model.Factorial;

import java.math.BigInteger;
import java.util.Optional;

@Service
@Slf4j
public class FactorialPersistenceService {
    private final CacheManager cacheManager;
    private final FactorialRepository factorialRepository;
    private final String cacheName;

    public FactorialPersistenceService(CacheManager cacheManager,
                                       FactorialRepository factorialRepository,
                                       @Value("${factorial.cache-name}") String cacheName) {
        this.cacheManager = cacheManager;
        this.factorialRepository = factorialRepository;
        this.cacheName = cacheName;
    }

    public Optional<BigInteger> getFromCacheOrDatabase(Long key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new IllegalStateException("Cache '" + cacheName + "' is not available");
        }

        BigInteger result = cache.get(key, BigInteger.class);
        if (result != null) {
            return Optional.of(result);
        }

        // Если в кэше нет, ищем в базе данных
        Optional<BigInteger> dbResult = factorialRepository.findById(key).map(Factorial::getResult);
        // Сохраняем значение в кэш, если оно найдено в базе данных
        dbResult.ifPresent(bigInteger -> cache.put(key, bigInteger));

        return dbResult;
    }

    public Optional<Long> findClosestFromCache(Long key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new IllegalStateException("Cache '" + cacheName + "' is not available");
        }
        for (long i = key - 1; i > 0; i--) {
            if (cache.get(i, BigInteger.class) != null) {
                return Optional.of(i); // Возвращает ключ найденного факториала
            }
        }
        return Optional.empty(); // Возвращает пустой Optional, если ближайший факториал не найден
    }

    public void saveToCacheAndDatabase(Long key, BigInteger result) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new IllegalStateException("Cache '" + cacheName + "' is not available");
        }
        cache.put(key, result);
        log.info("Save to db: {}", result.toString());
        factorialRepository.save(new Factorial(key, result));
    }
}
