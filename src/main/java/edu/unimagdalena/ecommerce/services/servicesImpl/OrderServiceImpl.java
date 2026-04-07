package edu.unimagdalena.ecommerce.services.servicesImpl;



import edu.unimagdalena.ecommerce.api.dto.OrderDtos.*;
import edu.unimagdalena.ecommerce.entities.*;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import edu.unimagdalena.ecommerce.repositories.*;
import edu.unimagdalena.ecommerce.services.OrderService;
import edu.unimagdalena.ecommerce.services.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final OrderMapper mapper;

    @Override
    public OrderResponse create(CreateOrderRequest req) {
        //  Validar Cliente ---
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new RuntimeException("Cliente no existe"));

        //  Validar que la dirección pertenezca al cliente ---
        Address address = customer.getAddresses().stream()
                .filter(a -> a.getId().equals(req.addressId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("La dirección no pertenece al cliente"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(address);
        order.setStatus(OrderStatus.CREATED); //  Estado inicial
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(new HashSet<>());

        BigDecimal totalPedido = BigDecimal.ZERO;

        //  Procesar Ítems ---
        for (CreateOrderItemRequest itemReq : req.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Producto no existe"));

            if (!product.getActive()) throw new RuntimeException("Producto no activo");
            if (itemReq.quantity() <= 0) throw new RuntimeException("Cantidad inválida");

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setUnitPrice(product.getPrice()); // Regla 6.2: Precio del momento

            BigDecimal subtotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setSubtotal(subtotal);

            totalPedido = totalPedido.add(subtotal);
            order.getItems().add(item);
        }

        order.setTotalAmount(totalPedido); //  Cálculo automático del total
        return mapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse pay(UUID id) {
        Order order = orderRepo.findById(id).orElseThrow();

        //  Validar estado ---
        if (order.getStatus() != OrderStatus.CREATED)
            throw new RuntimeException("Solo pedidos en CREATED pueden pagarse");

        //  Validar Stock (Todo o nada) ---
        for (OrderItem item : order.getItems()) {
            Inventory inv = item.getProduct().getInventory();
            if (inv.getAvailableQuantity() < item.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para: " + item.getProduct().getName());
            }
        }

        // --- Regla 6.3: Descontar Inventario ---
        for (OrderItem item : order.getItems()) {
            Inventory inv = item.getProduct().getInventory();
            inv.setAvailableQuantity(inv.getAvailableQuantity() - item.getQuantity());
        }

        order.setStatus(OrderStatus.PAID);
        return mapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse cancel(UUID id, CancelOrderRequest req) {
        Order order = orderRepo.findById(id).orElseThrow();

        //  Lógica de cancelación ---
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.SHIPPED) {
            throw new RuntimeException("No se puede cancelar un pedido enviado o entregado");
        }

        //  Revertir stock si ya estaba pagado ---
        if (order.getStatus() == OrderStatus.PAID) {
            for (OrderItem item : order.getItems()) {
                Inventory inv = item.getProduct().getInventory();
                inv.setAvailableQuantity(inv.getAvailableQuantity() + item.getQuantity());
            }
        }

        order.setStatus(OrderStatus.CANCELLED);

        return mapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse ship(UUID id) {
        Order order = orderRepo.findById(id).orElseThrow();
        if (order.getStatus() != OrderStatus.PAID) throw new RuntimeException("Solo pedidos PAID se envían");
        order.setStatus(OrderStatus.SHIPPED);
        return mapper.toResponse(orderRepo.save(order));
    }

    @Override
    public OrderResponse deliver(UUID id) {
        Order order = orderRepo.findById(id).orElseThrow();
        if (order.getStatus() != OrderStatus.SHIPPED) throw new RuntimeException("Solo pedidos SHIPPED se entregan");
        order.setStatus(OrderStatus.DELIVERED);
        return mapper.toResponse(orderRepo.save(order));
    }

    @Override public OrderResponse get(UUID id) { return orderRepo.findById(id).map(mapper::toResponse).orElseThrow(); }
    @Override
    public List<OrderResponse> list() {
        return orderRepo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
    @Override public List<OrderResponse> listByCustomer(UUID customerId) { return orderRepo.findByCustomerId(customerId).stream().map(mapper::toResponse).toList(); }
}