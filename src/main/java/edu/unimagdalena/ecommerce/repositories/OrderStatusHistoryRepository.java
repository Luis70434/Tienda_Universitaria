package edu.unimagdalena.ecommerce.repositories;



import edu.unimagdalena.ecommerce.entities.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory,UUID> {

    // Historial de cambios de un pedido [cite: 139]
    List<OrderStatusHistory> findByOrderIdOrderByChangeDateDesc(UUID orderId);
}