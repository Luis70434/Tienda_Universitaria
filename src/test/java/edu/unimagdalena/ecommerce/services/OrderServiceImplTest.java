package edu.unimagdalena.ecommerce.services;

import edu.unimagdalena.ecommerce.api.dto.OrderDtos.*;
import edu.unimagdalena.ecommerce.entities.*;
import edu.unimagdalena.ecommerce.enums.OrderStatus;
import edu.unimagdalena.ecommerce.repositories.*;
import edu.unimagdalena.ecommerce.services.mapper.OrderMapper;
import edu.unimagdalena.ecommerce.services.servicesImpl.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepo;
    @Mock private CustomerRepository customerRepo;
    @Mock private ProductRepository productRepo;
    @Mock private OrderMapper mapper;

    @InjectMocks private OrderServiceImpl service;



    @Test
    @DisplayName(" Rechazo de pago por stock insuficiente")
    void pagar_SinStock_Falla() {
        Order o = new Order(); o.setStatus(OrderStatus.CREATED);
        Inventory inv = new Inventory(); inv.setAvailableQuantity(1);
        Product p = new Product(); p.setInventory(inv);
        OrderItem item = new OrderItem(); item.setProduct(p); item.setQuantity(10);
        o.setItems(new HashSet<>(Collections.singleton(item)));

        when(orderRepo.findById(any())).thenReturn(Optional.of(o));
        assertThrows(RuntimeException.class, () -> service.pay(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Validación de transiciones de estado (No enviar si no está pagado)")
    void enviar_SinPago_Falla() {
        Order o = new Order(); o.setStatus(OrderStatus.CREATED);
        when(orderRepo.findById(any())).thenReturn(Optional.of(o));
        assertThrows(RuntimeException.class, () -> service.ship(UUID.randomUUID()));
    }

    @Test
    @DisplayName(" Al crear pedido, debe calcular el total correctamente (Precio x Cantidad)")
    void crearPedido_DebeCalcularTotalCorrectamente() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        UUID addrId = UUID.randomUUID();

        Address address = new Address();
        address.setId(addrId);
        Customer customer = new Customer();
        customer.setAddresses(Collections.singleton(address));

        Product product = new Product();
        product.setPrice(new BigDecimal("50.00")); // Precio base
        product.setActive(true);

        CreateOrderItemRequest itemReq = new CreateOrderItemRequest(UUID.randomUUID(), 3); // 3 unidades
        CreateOrderRequest request = new CreateOrderRequest(customerId, addrId, List.of(itemReq));

        when(customerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepo.findById(any())).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.create(request);

        // Assert: 50.00 * 3 = 150.00
        verify(orderRepo).save(argThat(order ->
                order.getTotalAmount().compareTo(new BigDecimal("150.00")) == 0
        ));
    }


    @Test
    @DisplayName("Al pagar, debe descontar la cantidad correcta del inventario")
    void pagarPedido_DebeDescontarInventario() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        Inventory inv = new Inventory();
        inv.setAvailableQuantity(10); // Stock inicial

        Product p = new Product();
        p.setInventory(inv);

        OrderItem item = new OrderItem();
        item.setProduct(p);
        item.setQuantity(4); // Se compran 4

        order.setItems(new HashSet<>(Collections.singleton(item)));

        when(orderRepo.findById(any())).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        // Act
        service.pay(UUID.randomUUID());

        // Assert: 10 - 4 = 6
        assertEquals(6, inv.getAvailableQuantity(), "El inventario debería haber disminuido a 6");
        assertEquals(OrderStatus.PAID, order.getStatus());
    }


    @Test
    @DisplayName(" Al cancelar un pedido pagado, debe devolver el stock al inventario")
    void cancelarPedido_DebeRestaurarInventario() {
        // Arrange
        Order order = new Order();
        order.setStatus(OrderStatus.PAID); // Ya estaba pagado (stock ya descontado)

        Inventory inv = new Inventory();
        inv.setAvailableQuantity(5); // Stock actual

        Product p = new Product();
        p.setInventory(inv);

        OrderItem item = new OrderItem();
        item.setProduct(p);
        item.setQuantity(3); // El pedido cancelado tiene 3 unidades

        order.setItems(new HashSet<>(Collections.singleton(item)));

        when(orderRepo.findById(any())).thenReturn(Optional.of(order));
        when(orderRepo.save(any())).thenReturn(order);

        // Act
        service.cancel(UUID.randomUUID(), new CancelOrderRequest("Cliente se arrepintió"));

        // Assert: 5 + 3 = 8
        assertEquals(8, inv.getAvailableQuantity(), "El inventario debería haber aumentado a 8 al restaurarse");
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}