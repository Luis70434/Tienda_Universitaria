package edu.unimagdalena.ecommerce.services.servicesImpl;


import edu.unimagdalena.ecommerce.api.dto.ReportDtos.*;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import edu.unimagdalena.ecommerce.repositories.OrderRepository;
import edu.unimagdalena.ecommerce.repositories.ProductRepository;
import edu.unimagdalena.ecommerce.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    @Override
    public List<LowStockProductResponse> getLowStockProducts() {
        // Regla 6.6 disponible <= mínimo
        return productRepo.findAll().stream()
                .filter(p -> p.getInventory().getAvailableQuantity() <= p.getInventory().getMinimumStock())
                .map(p -> new LowStockProductResponse(
                        p.getId(),
                        p.getSku(),
                        p.getName(),
                        p.getInventory().getAvailableQuantity(),
                        p.getInventory().getMinimumStock()
                ))
                .toList();
    }

    @Override
    public List<BestSellingProductResponse> getBestSellingProducts(int limit) {
        return orderRepo.findAll().stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID || o.getStatus() == OrderStatus.DELIVERED)
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct(),
                        Collectors.summingLong(item -> item.getQuantity().longValue())
                ))
                .entrySet().stream()
                .map(entry -> new BestSellingProductResponse(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getValue()))
                .sorted((a, b) -> b.totalSold().compareTo(a.totalSold()))
                .limit(limit)
                .toList();
    }

    @Override
    public List<TopCustomerResponse> getTopBillingCustomers(int limit) {
        return orderRepo.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        o -> o.getCustomer(),
                        Collectors.reducing(BigDecimal.ZERO, o -> o.getTotalAmount(), BigDecimal::add)
                ))
                .entrySet().stream()
                .map(entry -> new TopCustomerResponse(
                        entry.getKey().getId(),
                        entry.getKey().getFirstName(),
                        entry.getKey().getLastName(),
                        entry.getValue()))
                .sorted((a, b) -> b.totalBilled().compareTo(a.totalBilled()))
                .limit(limit)
                .toList();
    }

    @Override
    public List<MonthlyIncomeResponse> getMonthlyIncome() {
        return orderRepo.findAll().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        o -> Map.of("year", o.getCreatedAt().getYear(), "month", o.getCreatedAt().getMonthValue()),
                        Collectors.reducing(BigDecimal.ZERO, o -> o.getTotalAmount(), BigDecimal::add)
                ))
                .entrySet().stream()
                .map(entry -> new MonthlyIncomeResponse(
                        (Integer) entry.getKey().get("year"),
                        (Integer) entry.getKey().get("month"),
                        entry.getValue()))
                .sorted((a, b) -> {
                    int yearComp = b.year().compareTo(a.year());
                    return yearComp != 0 ? yearComp : b.month().compareTo(a.month());
                })
                .toList();
    }
}