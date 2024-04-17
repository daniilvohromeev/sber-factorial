    package org.main.sberfactorial.service;

    import org.main.sberfactorial.exception.InvalidFactorialArgumentException;

    import java.math.BigInteger;

    /**
     * Интерфейс предоставляет метод для вычисления факториала числа.
     */
    public interface FactorialService {

        /**
         * @param number неотрицательное целое число, для которого вычисляется факториал.
         * @return {@code BigInteger}, представляющий факториал числа.
         * @throws InvalidFactorialArgumentException если переданное число некорректно.
         */
        BigInteger calculateFactorial(int number);
    }
