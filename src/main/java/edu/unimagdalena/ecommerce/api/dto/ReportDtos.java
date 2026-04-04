package edu.unimagdalena.ecommerce.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class ReportDtos {
    public record BestSellingProductResponse(
            UUID productId,
            String productName,
            Long totalSold
    ) implements Serializable {}

    public record MonthlyIncomeResponse(
            Integer year,
            Integer month,
            BigDecimal totalIncome
    ) implements Serializable {}

    public record TopCustomerResponse(
            UUID customerId,
            String firstName,
            String lastName,
            BigDecimal totalBilled
    ) implements Serializable {}

    public record LowStockProductResponse(
            UUID productId,
            String sku,
            String productName,
            Integer availableQuantity,
            Integer minimumStock
    ) implements Serializable {}
}
