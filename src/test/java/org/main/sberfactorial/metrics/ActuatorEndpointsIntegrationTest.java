package org.main.sberfactorial.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.main.sberfactorial.controller.payload.FactorialRequest;
import org.main.sberfactorial.exception.InvalidFactorialArgumentException;
import org.main.sberfactorial.service.FactorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MvcResult;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ActuatorEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private FactorialService factorialService;

    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.spring-boot.actuator.v3+json"))
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    public void testPrometheusEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain"));
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

        MvcResult result = mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("validation_method_argument_seconds_count"), "Метрика validation_method_argument_seconds_count должна быть");
        assertTrue(responseContent.contains("validation_method_argument_seconds_sum"), "Метрика validation_method_argument_seconds_sum быть");
    }
}
