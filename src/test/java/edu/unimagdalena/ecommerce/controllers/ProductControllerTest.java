package edu.unimagdalena.ecommerce.controllers;

import edu.unimagdalena.ecommerce.api.controllers.ProductController;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import tools.jackson.databind.ObjectMapper;
import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.InventoryResponse;
import edu.unimagdalena.ecommerce.api.dto.InventoryDtos.UpdateInventoryRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.CreateProductRequest;
import edu.unimagdalena.ecommerce.api.dto.ProductDtos.ProductResponse;
import edu.unimagdalena.ecommerce.services.InventoryService;
import edu.unimagdalena.ecommerce.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    ProductService productService;

    @MockitoBean
    InventoryService inventoryService;

    @Test
    void create_shouldReturn201() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        var req = new CreateProductRequest(
                "SKU-001",
                "Sudadera",
                "Sudadera oficial",
                new BigDecimal("85000"),
                categoryId,
                20,
                5
        );

        var resp = new ProductResponse(
                productId,
                "SKU-001",
                "Sudadera",
                "Sudadera oficial",
                new BigDecimal("85000"),
                true,
                categoryId,
                "Ropa",
                20
        );

        when(productService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.name").value("Sudadera"));
    }

    @Test
    void create_shouldReturn400WhenValidationFails() throws Exception {
        String invalidJson = """
                {
                  "sku": "",
                  "name": "",
                  "description": "x",
                  "price": 0,
                  "categoryId": null,
                  "stock": -1,
                  "minStock": -1
                }
                """;

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateInventory_shouldReturn200() throws Exception {
        UUID productId = UUID.randomUUID();

        var req = new UpdateInventoryRequest(30, 8);
        var resp = new InventoryResponse(UUID.randomUUID(), productId, 30, 8);

        when(inventoryService.update(eq(productId), any())).thenReturn(resp);

        mvc.perform(put("/api/products/{id}/inventory", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId.toString()))
                .andExpect(jsonPath("$.availableQuantity").value(30))
                .andExpect(jsonPath("$.minimumStock").value(8));
    }
}
