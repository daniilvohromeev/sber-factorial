package org.main.sberfactorial.component;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.main.sberfactorial.service.FactorialCalculationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@ConditionalOnProperty(name = "factorial.cache.loader.enabled", havingValue = "true")
public class FactorialCacheLoader implements CacheLoader<Long, BigInteger> {
    private final FactorialCalculationService factorialService;
    public FactorialCacheLoader(FactorialCalculationService factorialService) {
        this.factorialService = factorialService;
    }

    @Override
    public BigInteger load(Long key) {
        return factorialService.calculateFactorial(key);
    }
}