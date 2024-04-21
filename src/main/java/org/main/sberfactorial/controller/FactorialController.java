package org.main.sberfactorial.controller;

import org.main.sberfactorial.controller.payload.FactorialRequest;
import org.main.sberfactorial.controller.payload.FactorialResponse;
import org.main.sberfactorial.service.FactorialCalculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FactorialController {

    private final FactorialCalculationService factorialCalculationService;

    public FactorialController(FactorialCalculationService factorialCalculationService) {
        this.factorialCalculationService = factorialCalculationService;
    }

    @PostMapping("/factorial")
    public FactorialResponse getFactorial(@RequestBody FactorialRequest request) {
        return new FactorialResponse(factorialCalculationService.calculateFactorial(request.getFactorial_num()));
    }
}
