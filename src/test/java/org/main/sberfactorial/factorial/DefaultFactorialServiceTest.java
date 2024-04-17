package org.main.sberfactorial.factorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.main.sberfactorial.service.DefaultFactorialService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import org.main.sberfactorial.configuration.properties.FactorialProperties;
import org.main.sberfactorial.exception.InvalidFactorialArgumentException;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DefaultFactorialServiceTest {

    @Mock
    private CacheManager cacheManager;
    @Mock
    private FactorialProperties properties;
    @Mock
    private Cache cache;

    @InjectMocks
    private DefaultFactorialService service;

    @BeforeEach
    void setUp() {

        lenient().when(properties.getMaxValue()).thenReturn(100);
        lenient().when(properties.getCacheName()).thenReturn("factorials");
        lenient().when(cacheManager.getCache(any())).thenReturn(cache);
    }

    @Test
    void calculateFactorial_validInput_returnsFactorial() {

        when(cache.get(4, BigInteger.class)).thenReturn(BigInteger.valueOf(24));
        when(cache.get(5, BigInteger.class)).thenReturn(null);

        // Действие
        BigInteger result = service.calculateFactorial(5);

        assertNotNull(result);
        assertEquals(BigInteger.valueOf(120), result);
        verify(cache).put(eq(5), eq(BigInteger.valueOf(120)));
        verify(cache, times(1)).get(4, BigInteger.class);
        verify(cache, times(1)).get(5, BigInteger.class);
    }

    @Test
    void calculateFactorial_cachedValue_returnsCachedFactorial() {

        when(cache.get(5, BigInteger.class)).thenReturn(BigInteger.valueOf(120));

        BigInteger result = service.calculateFactorial(5);

        assertNotNull(result);
        assertEquals(BigInteger.valueOf(120), result);
        verify(cache, never()).put(anyInt(), any());
        verify(cache).get(5, BigInteger.class);
    }

    @Test
    void calculateFactorial_invalidInput_throwsException() {

        Exception exception = assertThrows(InvalidFactorialArgumentException.class, () -> service.calculateFactorial(-1));

        assertTrue(exception.getMessage().contains("Число должно быть между 0 и 100"));
    }

    @Test
    void calculateFactorial_aboveMax_throwsException() {
        when(properties.getMaxValue()).thenReturn(10);

        Exception exception = assertThrows(InvalidFactorialArgumentException.class, () -> service.calculateFactorial(11));

        assertTrue(exception.getMessage().contains("Число должно быть между 0 и 10"));
    }
}
