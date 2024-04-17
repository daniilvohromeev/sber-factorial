package org.main.sberfactorial.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class FactorialResponse {
    private BigInteger result;
}
