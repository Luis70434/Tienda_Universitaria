package edu.unimagdalena.ecommerce.controllers;

import edu.unimagdalena.ecommerce.api.controllers.CustomerController;
import edu.unimagdalena.ecommerce.api.dto.CustomerDtos;
import edu.unimagdalena.ecommerce.enums.CustomerStatus;
import edu.unimagdalena.ecommerce.services.CustomerService;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.UUID;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockitoBean
    CustomerService service;

    @Test
    void create_shouldReturn201() throws Exception {
        UUID id = UUID.randomUUID();

        var req = new CustomerDtos.CreateCustomerRequest("Juan", "Perez", "juan@test.com");
        var resp = new CustomerDtos.CustomerResponse(
                id,
                "Juan",
                "Perez",
                "juan@test.com",
                CustomerStatus.ACTIVE,
                LocalDateTime.now()
        );

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void create_shouldReturn400WhenValidationFails() throws Exception {
        String invalidJson = """
                {
                  "firstName": "",
                  "lastName": "",
                  "email": "correo-malo"
                }
                """;

        mvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();

        var req = new CustomerDtos.UpdateCustomerRequest(
                "Ana",
                "Gomez",
                "ana@test.com",
                CustomerStatus.ACTIVE
        );

        var resp = new CustomerDtos.CustomerResponse(
                id,
                "Ana",
                "Gomez",
                "ana@test.com",
                CustomerStatus.ACTIVE,
                LocalDateTime.now()
        );

        when(service.update(eq(id), any())).thenReturn(resp);

        mvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }
}
