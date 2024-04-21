package org.main.sberfactorial.repository;

import org.main.sberfactorial.model.Factorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactorialRepository extends JpaRepository<Factorial, Long> {
}
