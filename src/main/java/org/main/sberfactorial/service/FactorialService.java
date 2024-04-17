package org.main.sberfactorial.service;

import java.math.BigInteger;

/**
 * Интерфейс предоставляет метод для рекурсивного вычисления факториала числа.
 */

public interface FactorialService {

    /**
     * @param number неотрицательное целое число, для которого вычисляется факториал.
     * @return {@code BigInteger}, представляющий факториал числа.
     * @throws IllegalArgumentException если переданное число некорректно.
     */
    BigInteger calculateFactorial(int number);
}
