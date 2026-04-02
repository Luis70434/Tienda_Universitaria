package edu.unimagdalena.ecommerce.repositories;

import edu.unimagdalena.ecommerce.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    // Productos más vendidos por período
    @Query("SELECT oi.product.id, oi.product.name, SUM(oi.quantity) as totalVendidos " +
            "FROM OrderItem oi WHERE oi.order.createdAt BETWEEN :startDate AND :endDate " +
            "AND oi.order.status IN ('PAID', 'SHIPPED', 'DELIVERED') " +
            "GROUP BY oi.product.id, oi.product.name " +
            "ORDER BY totalVendidos DESC")
    List<Object[]> findBestSellingProducts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Top de categorías por volumen de ventas
    @Query("SELECT p.category.name, SUM(oi.quantity) as totalVendidos " +
            "FROM OrderItem oi JOIN oi.product p " +
            "WHERE oi.order.status IN ('PAID', 'SHIPPED', 'DELIVERED') " +
            "GROUP BY p.category.name " +
            "ORDER BY totalVendidos DESC")
    List<Object[]> findTopCategoriesBySales();
}