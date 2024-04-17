package org.main.sberfactorial.controller.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FactorialRequest {
    @JsonProperty("factorial_num")
    @NotNull(message = "Число для вычисления факториала не предоставлено или ключ в JSON некорректен ('factorial_num')")
    private Integer number;
}
