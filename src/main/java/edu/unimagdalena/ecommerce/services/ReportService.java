package edu.unimagdalena.ecommerce.services;

import edu.unimagdalena.ecommerce.api.dto.ReportDtos.*;
import java.util.List;

public interface ReportService {

    List<LowStockProductResponse> getLowStockProducts();


    List<BestSellingProductResponse> getBestSellingProducts(int limit);


    List<MonthlyIncomeResponse> getMonthlyIncome();


    List<TopCustomerResponse> getTopBillingCustomers(int limit);
}