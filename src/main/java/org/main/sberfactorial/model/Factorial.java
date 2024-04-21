package org.main.sberfactorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factorial {
    @Id
    private Long id;

    @Column(columnDefinition = "NUMERIC(1000,0)")
    private BigInteger result;
}
