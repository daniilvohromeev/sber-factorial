package org.main.sberfactorial.controller.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FactorialRequest {
    @JsonProperty("factorial_num")
    private Integer number;
}
