package org.main.sberfactorial.service;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class FactorialCalculationService {
    private final FactorialPersistenceService persistenceService;

    public FactorialCalculationService(FactorialPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public BigInteger calculateFactorial(Long key) {
        Optional<BigInteger> result = persistenceService.getFromCacheOrDatabase(key);
        if (result.isPresent()) {
            return result.get();
        }

        Long closestFactorial = persistenceService.findClosestFromCache(key).orElse(2L);

        BigInteger factorial =  persistenceService.getFromCacheOrDatabase(closestFactorial).orElse(BigInteger.valueOf(1L));

        for (long i = closestFactorial; i <= key; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
            persistenceService.saveToCacheAndDatabase(i, factorial);
        }

        return factorial;
    }
}
