package org.main.sberfactorial.factorial;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.main.sberfactorial.controller.FactorialResource;
import org.main.sberfactorial.controller.payload.FactorialRequest;
import org.main.sberfactorial.exception.InvalidFactorialArgumentException;
import org.main.sberfactorial.service.FactorialService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FactorialResource.class)
public class FactorialResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FactorialService factorialService;

    @Test
    void testFactorial() throws Exception {
        FactorialRequest request = new FactorialRequest();
        request.setNumber(5);
        BigInteger result = new BigInteger("120");

        when(factorialService.calculateFactorial(5)).thenReturn(result);

        mockMvc.perform(post("/factorial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("120"));

        verify(factorialService).calculateFactorial(5);
    }

    @Test
    void testFactorialWithNegativeNumber() throws Exception {
        FactorialRequest request = new FactorialRequest();
        request.setNumber(-1);

        when(factorialService.calculateFactorial(-1)).thenThrow(new InvalidFactorialArgumentException("Число не должно быть отрицательным"));

        mockMvc.perform(post("/factorial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Число не должно быть отрицательным"));

        verify(factorialService).calculateFactorial(-1);
    }

    @Test
    void testFactorialWithMoreThenMaxNumber() throws Exception {
        FactorialRequest request = new FactorialRequest();
        request.setNumber(120);

        when(factorialService.calculateFactorial(120)).thenThrow(new InvalidFactorialArgumentException("Число не должно быть больше max"));

        mockMvc.perform(post("/factorial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Число не должно быть больше max"));

        verify(factorialService).calculateFactorial(120);
    }
}
