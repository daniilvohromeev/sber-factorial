package org.main.sberfactorial.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.logging.Logger;

/**
 * Реализует {@link FactorialService} который вычисляет факториалы
 */
@Service
public class DefaultFactorialService implements FactorialService {
    private static final Logger logger = Logger.getLogger(DefaultFactorialService.class.getName());

    private final CacheManager cacheManager;

    @Value("${spring.factorial.max-value}")
    private int max;

    public DefaultFactorialService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public BigInteger calculateFactorial(int number) {
        logger.info("Вычисление факториала для числа: " + number);

        if (number < 0 || number > max) {
            logger.severe("Попытка вычислить факториал для недопустимого числа.");
            throw new IllegalArgumentException("Число должно быть между 0 и " + max);
        }

        Cache cache = cacheManager.getCache("factorials");
        if (cache == null) {
            logger.severe("Кэш не доступен");
            return null;
        }

        // Пытаемся извлечь значение факториала из кэша если он есть просто вернем
        BigInteger cachedFactorial = cache.get(number, BigInteger.class);
        if (cachedFactorial != null) {
            logger.info("Факториал числа " + number + " найден в кэше");
            return cachedFactorial;
        }

        BigInteger factorial = BigInteger.ONE;
        int start = 1;

        // Находим ближайший факториал к запрашиваемому числу
        for (int i = number - 1; i > 0; i--) {
            cachedFactorial = cache.get(i, BigInteger.class);
            if (cachedFactorial != null) {
                factorial = cachedFactorial;
                start = i + 1;
                break;
            }
        }

        // вычисляем от ближайшего известного числа если такое имеется
        for (int i = start; i <= number; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
            cache.put(i, factorial); // Кэшируем новые вычисления
        }

        logger.info("Факториал числа " + number + " вычислен");
        return factorial;
    }
}
