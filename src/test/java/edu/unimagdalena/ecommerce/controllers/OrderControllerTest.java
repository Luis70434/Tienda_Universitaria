package edu.unimagdalena.ecommerce.controllers;

import edu.unimagdalena.ecommerce.api.controllers.OrderController;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.CancelOrderRequest;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.CreateOrderItemRequest;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.CreateOrderRequest;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.OrderItemResponse;
import edu.unimagdalena.ecommerce.api.dto.OrderDtos.OrderResponse;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import edu.unimagdalena.ecommerce.exceptions.BusinessException;
import edu.unimagdalena.ecommerce.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    OrderService service;

    @Test
    void create_shouldReturn201() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        var req = new CreateOrderRequest(
                customerId,
                addressId,
                List.of(new CreateOrderItemRequest(productId, 2))
        );

        var item = new OrderItemResponse(
                UUID.randomUUID(),
                productId,
                "Cuaderno",
                2,
                new BigDecimal("15000"),
                new BigDecimal("30000")
        );

        var resp = new OrderResponse(
                orderId,
                customerId,
                addressId,
                OrderStatus.CREATED,
                new BigDecimal("30000"),
                LocalDateTime.now(),
                List.of(item)
        );

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.totalAmount").value(30000));
    }

    @Test
    void create_shouldReturn400WhenValidationFails() throws Exception {
        String invalidJson = """
                {
                  "customerId": null,
                  "addressId": null,
                  "items": []
                }
                """;

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void pay_shouldReturn400WhenStockIsInsufficient() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(service.pay(orderId)).thenThrow(new BusinessException("Stock insuficiente para uno o más productos"));

        mvc.perform(put("/api/orders/{id}/pay", orderId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Stock insuficiente para uno o más productos"));
    }

    @Test
    void ship_shouldReturn200() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        var resp = new OrderResponse(
                orderId,
                customerId,
                addressId,
                OrderStatus.SHIPPED,
                new BigDecimal("30000"),
                LocalDateTime.now(),
                List.of()
        );

        when(service.ship(orderId)).thenReturn(resp);

        mvc.perform(put("/api/orders/{id}/ship", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void deliver_shouldReturn200() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        var resp = new OrderResponse(
                orderId,
                customerId,
                addressId,
                OrderStatus.DELIVERED,
                new BigDecimal("30000"),
                LocalDateTime.now(),
                List.of()
        );

        when(service.deliver(orderId)).thenReturn(resp);

        mvc.perform(put("/api/orders/{id}/deliver", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void cancel_shouldReturn200() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();

        var req = new CancelOrderRequest("Cancelado por el cliente");
        var resp = new OrderResponse(
                orderId,
                customerId,
                addressId,
                OrderStatus.CANCELLED,
                new BigDecimal("30000"),
                LocalDateTime.now(),
                List.of()
        );

        when(service.cancel(eq(orderId), any())).thenReturn(resp);

        mvc.perform(put("/api/orders/{id}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
