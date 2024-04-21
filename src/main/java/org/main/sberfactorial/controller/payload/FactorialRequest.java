package org.main.sberfactorial.controller.payload;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class FactorialRequest {
    @Min(1)
    @Max(100)
    private Long factorial_num;
}
