package edu.unimagdalena.ecommerce.api.controllers;

import edu.unimagdalena.ecommerce.api.dto.ReportDtos;
import edu.unimagdalena.ecommerce.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/best-selling-products")
    public ResponseEntity<List<ReportDtos.BestSellingProductResponse>> getBestSellingProducts(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(reportService.getBestSellingProducts(limit));
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<List<ReportDtos.MonthlyIncomeResponse>> getMonthlyIncome() {
        return ResponseEntity.ok(reportService.getMonthlyIncome());
    }

    @GetMapping("/top-customers")
    public ResponseEntity<List<ReportDtos.TopCustomerResponse>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(reportService.getTopBillingCustomers(limit));
    }

    @GetMapping("/low-stock-products")
    public ResponseEntity<List<ReportDtos.LowStockProductResponse>> getLowStockProducts() {
        return ResponseEntity.ok(reportService.getLowStockProducts());
    }
}
