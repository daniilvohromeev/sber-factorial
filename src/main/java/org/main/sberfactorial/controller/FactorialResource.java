package org.main.sberfactorial.controller;

import jakarta.validation.Valid;
import org.main.sberfactorial.controller.payload.FactorialRequest;
import org.main.sberfactorial.controller.payload.FactorialResponse;
import org.main.sberfactorial.service.FactorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class FactorialResource {
    final FactorialService factorialService;

    public FactorialResource(FactorialService factorialService) {
        this.factorialService = factorialService;
    }

    @PostMapping("/factorial")
    public ResponseEntity<FactorialResponse> factorial(@Valid @RequestBody FactorialRequest factorialRequest) {
        BigInteger result = factorialService.calculateFactorial(factorialRequest.getNumber());
        return ResponseEntity.ok(new FactorialResponse(result));
    }
}
