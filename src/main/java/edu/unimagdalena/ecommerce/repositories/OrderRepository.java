package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.Order;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Buscar pedidos por cliente (Actualizado a UUID)
    List<Order> findByCustomerId(UUID customerId);

    // Buscar pedidos por filtros combinados (Actualizado a UUID)
    @Query("SELECT o FROM Order o WHERE " +
            "(:customerId IS NULL OR o.customer.id = :customerId) AND " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(cast(:startDate as timestamp) IS NULL OR o.createdAt >= :startDate) AND " +
            "(cast(:endDate as timestamp) IS NULL OR o.createdAt <= :endDate) AND " +
            "(:minTotal IS NULL OR o.totalAmount >= :minTotal) AND " +
            "(:maxTotal IS NULL OR o.totalAmount <= :maxTotal)")
    List<Order> findOrdersByFilters(
            @Param("customerId") UUID customerId,
            @Param("status") OrderStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("minTotal") BigDecimal minTotal,
            @Param("maxTotal") BigDecimal maxTotal
    );

    // Clientes con mayor facturación (Solo pedidos pagados/entregados)
    @Query("SELECT o.customer.id, o.customer.firstName, o.customer.lastName, SUM(o.totalAmount) as total " +
            "FROM Order o WHERE o.status IN ('PAID', 'SHIPPED', 'DELIVERED') " +
            "GROUP BY o.customer.id, o.customer.firstName, o.customer.lastName " +
            "ORDER BY total DESC")
    List<Object[]> findTopCustomersByBilling();

    // Ingresos mensuales agrupados
    @Query("SELECT YEAR(o.createdAt), MONTH(o.createdAt), SUM(o.totalAmount) " +
            "FROM Order o WHERE o.status IN ('PAID', 'SHIPPED', 'DELIVERED') " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt) DESC, MONTH(o.createdAt) DESC")
    List<Object[]> findMonthlyIncome();
}