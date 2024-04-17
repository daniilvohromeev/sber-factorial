package org.main.sberfactorial.service;

import org.main.sberfactorial.configuration.properties.FactorialProperties;
import org.main.sberfactorial.exception.CacheInvalidException;
import org.main.sberfactorial.exception.InvalidFactorialArgumentException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Реализует {@link FactorialService} который вычисляет факториалы
 */
@Service
public class DefaultFactorialService implements FactorialService {
    private static final Logger logger = Logger.getLogger(DefaultFactorialService.class.getName());
    private final CacheManager cacheManager;
    private final FactorialProperties properties;

    public DefaultFactorialService(CacheManager cacheManager, FactorialProperties properties) {
        this.cacheManager = cacheManager;
        this.properties = properties;
    }

    @Override
    public BigInteger calculateFactorial(int number) {
        logger.info("Вычисление факториала для числа: " + number);
        if (number < 0 || number > properties.getMaxValue()) {
            logger.severe("Попытка вычислить факториал для недопустимого числа.");
            throw new InvalidFactorialArgumentException("Число должно быть между 0 и " + properties.getMaxValue());
        }

        return Optional.ofNullable(cacheManager.getCache(properties.getCacheName()))
                .map(cache -> calculateCachedFactorial(cache, number))
                .orElseThrow(() -> new CacheInvalidException("Кэш не существует"));
    }

    private BigInteger calculateCachedFactorial(Cache cache, int number) {
        return Optional.ofNullable(cache.get(number, BigInteger.class))
                .orElseGet(() -> computeAndCacheFactorial(cache, number));
    }

    private BigInteger computeAndCacheFactorial(Cache cache, int number) {
        int start = 1;
        BigInteger factorial = BigInteger.ONE;

        for (int i = number - 1; i >= 1; i--) {
            BigInteger cachedFactorial = cache.get(i, BigInteger.class);
            if (cachedFactorial != null) {
                factorial = cachedFactorial;
                start = i + 1;
                break;
            }
        }

        for (int i = start; i <= number; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
            cache.put(i, factorial);
        }
        logger.info("Факториал числа " + number + " вычислен");
        return factorial;
    }
}
