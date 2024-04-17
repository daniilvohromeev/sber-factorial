package org.main.sberfactorial.controller;

import jakarta.validation.Valid;
import org.main.sberfactorial.controller.payload.FactorialRequest;
import org.main.sberfactorial.controller.payload.FactorialResponse;
import org.main.sberfactorial.service.FactorialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
/**
 * Контроллер для обработки запросов на вычисление факториала чисел.
 * <p>
 * Этот контроллер предоставляет REST API для вычисления факториала заданного числа.
 * В качестве входных данных используется объект {@link FactorialRequest}.
 * Результатом выполнения является объект {@link FactorialResponse},
 * содержащий вычисленный факториал.
 * </p>
 */
@RestController
public class FactorialResource {
    final FactorialService factorialService;

    public FactorialResource(FactorialService factorialService) {
        this.factorialService = factorialService;
    }
    /**
     * Обрабатывает POST-запросы по URL "/factorial" для вычисления факториала числа.
     * <p>
     * Метод принимает объект {@link FactorialRequest}, который содержит число, факториал которого необходимо вычислить.
     * возвращает объект {@link FactorialResponse} с результатом.
     * </p>
     * @param factorialRequest объект запроса с данными для вычисления факториала.
     * @return объект {@link FactorialResponse} содержащий результат вычисления факториала.
     */
    @PostMapping("/factorial")
    public FactorialResponse factorial(@Valid @RequestBody FactorialRequest factorialRequest) {
        BigInteger result = factorialService.calculateFactorial(factorialRequest.getNumber());
        return new FactorialResponse(result);
    }
}
