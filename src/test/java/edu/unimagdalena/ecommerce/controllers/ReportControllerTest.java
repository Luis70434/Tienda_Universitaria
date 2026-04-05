package edu.unimagdalena.ecommerce.controllers;

import edu.unimagdalena.ecommerce.api.controllers.ReportController;
import edu.unimagdalena.ecommerce.api.dto.ReportDtos.BestSellingProductResponse;
import edu.unimagdalena.ecommerce.api.dto.ReportDtos.LowStockProductResponse;
import edu.unimagdalena.ecommerce.api.dto.ReportDtos.MonthlyIncomeResponse;
import edu.unimagdalena.ecommerce.api.dto.ReportDtos.TopCustomerResponse;
import edu.unimagdalena.ecommerce.services.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired MockMvc mvc;

    @MockitoBean
    ReportService service;

    @Test
    void bestSellingProducts_shouldReturn200() throws Exception {
        when(service.getBestSellingProducts(10)).thenReturn(
                List.of(new BestSellingProductResponse(UUID.randomUUID(), "Sudadera", 25L))
        );

        mvc.perform(get("/api/reports/best-selling-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Sudadera"))
                .andExpect(jsonPath("$[0].totalSold").value(25));
    }

    @Test
    void monthlyIncome_shouldReturn200() throws Exception {
        when(service.getMonthlyIncome()).thenReturn(
                List.of(new MonthlyIncomeResponse(2026, 4, new BigDecimal("1250000")))
        );

        mvc.perform(get("/api/reports/monthly-income"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2026))
                .andExpect(jsonPath("$[0].month").value(4))
                .andExpect(jsonPath("$[0].totalIncome").value(1250000));
    }

    @Test
    void topCustomers_shouldReturn200() throws Exception {
        when(service.getTopBillingCustomers(10)).thenReturn(
                List.of(new TopCustomerResponse(UUID.randomUUID(), "Juan", "Perez", new BigDecimal("980000")))
        );

        mvc.perform(get("/api/reports/top-customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].lastName").value("Perez"))
                .andExpect(jsonPath("$[0].totalBilled").value(980000));
    }

    @Test
    void lowStockProducts_shouldReturn200() throws Exception {
        when(service.getLowStockProducts()).thenReturn(
                List.of(new LowStockProductResponse(UUID.randomUUID(), "SKU-10", "Libro", 2, 5))
        );

        mvc.perform(get("/api/reports/low-stock-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU-10"))
                .andExpect(jsonPath("$[0].productName").value("Libro"))
                .andExpect(jsonPath("$[0].availableQuantity").value(2))
                .andExpect(jsonPath("$[0].minimumStock").value(5));
    }
}
