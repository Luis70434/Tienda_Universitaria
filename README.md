# API de Comercio Electrónico para Tienda Universitaria

## 1. Descripción general

Este proyecto corresponde al desarrollo de una **API REST para una tienda universitaria**, orientada a la gestión de clientes, direcciones, categorías, productos, inventario, pedidos, detalle de pedidos e historial de estados. La solución busca resolver problemas de sobreventa, errores de inventario, falta de trazabilidad en el ciclo del pedido y limitaciones en la consulta de reportes comerciales. :contentReference[oaicite:1]{index=1}

La aplicación fue construida siguiendo una **arquitectura por capas**, con persistencia en base de datos relacional, validaciones de negocio, manejo global de excepciones y pruebas automáticas para repositories, services y controllers. 

---

## 2. Objetivo del proyecto

Diseñar e implementar un backend para ventas e inventario que permita:

- registrar clientes, direcciones, categorías y productos;
- administrar inventario y stock mínimo;
- crear pedidos con múltiples ítems;
- calcular automáticamente subtotales y total;
- gestionar transiciones de estado del pedido;
- consultar información operativa y generar reportes. :contentReference[oaicite:3]{index=3}

---

## 3. Stack tecnológico

El proyecto usa el stack obligatorio definido en el enunciado:

- **Java 21**
- **Spring Boot 4**
- **PostgreSQL**
- **Testcontainers**
- **JUnit 5**
- **Mockito** :contentReference[oaicite:4]{index=4}

Además, en la implementación se emplean componentes habituales del ecosistema Spring:

- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Lombok
- MapStruct

---

## 4. Arquitectura por capas

El proyecto está organizado siguiendo separación de responsabilidades por capas:

### 4.1 Entities
Representan el modelo del dominio y el mapeo a la base de datos.

Entidades principales:
- `Customer`
- `Address`
- `Category`
- `Product`
- `Inventory`
- `Order`
- `OrderItem`
- `OrderStatusHistory` :contentReference[oaicite:5]{index=5}

### 4.2 Repositories
Encapsulan el acceso a datos usando Spring Data JPA, tanto con consultas derivadas como con JPQL.

Repositories principales:
- `CustomerRepository`
- `AddressRepository`
- `CategoryRepository`
- `ProductRepository`
- `InventoryRepository`
- `OrderRepository`
- `OrderItemRepository`
- `OrderStatusHistoryRepository` :contentReference[oaicite:6]{index=6}

### 4.3 Services
Contienen la lógica de negocio del sistema.

Services principales:
- `CustomerService`
- `AddressService`
- `CategoryService`
- `ProductService`
- `InventoryService`
- `OrderService`
- `ReportService` :contentReference[oaicite:7]{index=7}

### 4.4 Controllers
Exponen la API REST y reciben/retornan DTOs.

Controllers principales:
- `CustomerController`
- `AddressController`
- `CategoryController`
- `ProductController`
- `OrderController`
- `ReportController` :contentReference[oaicite:8]{index=8}

### 4.5 DTOs
Se utilizan para desacoplar el modelo interno de las solicitudes y respuestas HTTP.

DTOs request:
- `CreateCustomerRequest`
- `UpdateCustomerRequest`
- `CreateAddressRequest`
- `CreateCategoryRequest`
- `CreateProductRequest`
- `UpdateProductRequest`
- `UpdateInventoryRequest`
- `CreateOrderRequest`
- `CreateOrderItemRequest`
- `CancelOrderRequest`

DTOs response:
- `CustomerResponse`
- `AddressResponse`
- `CategoryResponse`
- `ProductResponse`
- `InventoryResponse`
- `OrderItemResponse`
- `OrderResponse`
- `BestSellingProductResponse`
- `MonthlyIncomeResponse`
- `TopCustomerResponse`
- `LowStockProductResponse` :contentReference[oaicite:9]{index=9}

### 4.6 Exceptions
Se implementó manejo global de errores mediante:
- `ResourceNotFoundException`
- `BusinessException`
- `ConflictException`
- `ValidationException`
- `GlobalExceptionHandler` :contentReference[oaicite:10]{index=10}

---

## 5. Modelo de datos

El diseño mínimo de base de datos contempla las siguientes tablas:

- `customers`
- `addresses`
- `categories`
- `products`
- `inventories`
- `orders`
- `order_items`
- `order_status_history` :contentReference[oaicite:11]{index=11}

### Relaciones principales
- Un cliente tiene muchas direcciones.
- Un cliente tiene muchos pedidos.
- Una categoría tiene muchos productos.
- Un producto tiene un inventario.
- Un pedido tiene muchos ítems.
- Un pedido tiene muchos cambios de estado. :contentReference[oaicite:12]{index=12}

---

## 6. Reglas de negocio

### 6.1 Reglas para productos e inventario
- Todo producto debe tener un **SKU único** dentro del sistema.
- Todo producto debe pertenecer a una **categoría existente**.
- El precio del producto debe ser **mayor que cero**.
- El inventario disponible y el stock mínimo **no pueden ser negativos**.
- No se puede desactivar un producto que forme parte de pedidos activos, salvo que una regla adicional lo permita explícitamente. :contentReference[oaicite:13]{index=13}

### 6.2 Reglas para creación de pedidos
- Un pedido debe asociarse a un **cliente existente y activo**.
- La dirección de envío debe pertenecer al cliente del pedido.
- El pedido debe contener **al menos un ítem**.
- No se permiten cantidades menores o iguales a cero.
- Cada producto del pedido debe existir y estar activo.
- El precio unitario del ítem se toma del producto al momento de la creación.
- El subtotal del ítem se calcula como `quantity * unitPrice`.
- El total del pedido se calcula como la suma de subtotales.
- El cliente no puede enviar el total de forma manual.
- Todo pedido nuevo se crea con estado inicial `CREATED`. :contentReference[oaicite:14]{index=14}

### 6.3 Reglas para pago del pedido
- Solo un pedido en estado `CREATED` puede pasar a `PAID`.
- Antes de pagar, el sistema debe validar stock suficiente para todos los ítems.
- Si un solo ítem no tiene stock suficiente, todo el pago debe rechazarse.
- Al pasar a `PAID`, el sistema descuenta inventario disponible.
- Todo cambio de estado debe registrarse en el historial. :contentReference[oaicite:15]{index=15}

### 6.4 Reglas para envío y entrega
- Solo un pedido `PAID` puede pasar a `SHIPPED`.
- No se puede enviar un pedido cancelado.
- Solo un pedido `SHIPPED` puede pasar a `DELIVERED`.
- No se puede marcar como entregado un pedido que nunca fue despachado. :contentReference[oaicite:16]{index=16}

### 6.5 Reglas para cancelación
- Un pedido `CREATED` puede cancelarse sin reversión de stock.
- Un pedido `PAID` puede cancelarse y debe revertir el stock descontado.
- Un pedido `SHIPPED` no podrá cancelarse si esa es la regla definida en el taller.
- Un pedido `DELIVERED` no puede cancelarse.
- Toda cancelación debe dejar trazabilidad en el historial del pedido. :contentReference[oaicite:17]{index=17}

### 6.6 Reglas para consultas y reportes
- Debe poder consultarse productos con bajo stock.
- Debe poder consultarse pedidos por cliente, estado, rango de fechas, total mínimo y total máximo.
- Deben generarse reportes de productos más vendidos, ingresos mensuales, clientes con mayor facturación y productos bajo stock. :contentReference[oaicite:18]{index=18}

---

## 7. Decisiones de diseño

### 7.1 Uso de DTOs
Se emplearon DTOs para evitar exponer directamente las entidades JPA en la API REST. Esto mejora el desacoplamiento, el control sobre la información enviada y la validación de datos de entrada.

### 7.2 Separación entre controller, service y repository
La lógica se distribuyó de la siguiente manera:

- **Controller**: recibe solicitudes HTTP y devuelve respuestas.
- **Service**: aplica reglas de negocio y coordina operaciones.
- **Repository**: realiza acceso a base de datos.

Esta separación mejora mantenibilidad, claridad y testabilidad.

### 7.3 Manejo global de excepciones
Se implementó un `GlobalExceptionHandler` para transformar excepciones de dominio y validación en respuestas HTTP coherentes. Esto evita duplicación de manejo de errores en los controladores.

### 7.4 Uso de historial de estados
Se decidió registrar cada transición del pedido en `OrderStatusHistory` para garantizar trazabilidad del ciclo del pedido, especialmente en procesos como pago, envío, entrega y cancelación. Esta decisión está alineada con el enunciado funcional del taller. 

### 7.5 Validaciones tempranas
Las validaciones se aplican en dos niveles:

- **Validaciones estructurales** mediante anotaciones en DTOs (`@NotNull`, `@NotBlank`, `@Email`, `@Min`, `@DecimalMin`).
- **Validaciones de negocio** dentro de services, por ejemplo: cliente activo, stock suficiente, estado válido del pedido, categoría existente o SKU único.

### 7.6 Actualización de inventario
La actualización del inventario se expone mediante el endpoint de productos:
`PUT /api/products/{id}/inventory`, de acuerdo con los endpoints mínimos del documento. :contentReference[oaicite:20]{index=20}

---

## 8. Endpoints implementados

### Clientes
- `POST /api/customers`
- `GET /api/customers/{id}`
- `GET /api/customers`
- `PUT /api/customers/{id}`

### Direcciones
- `POST /api/customers/{customerId}/addresses`
- `GET /api/customers/{customerId}/addresses`

### Categorías
- `POST /api/categories`
- `GET /api/categories`

### Productos
- `POST /api/products`
- `GET /api/products/{id}`
- `GET /api/products`
- `PUT /api/products/{id}`
- `PUT /api/products/{id}/inventory`

### Pedidos
- `POST /api/orders`
- `GET /api/orders/{id}`
- `GET /api/orders`
- `PUT /api/orders/{id}/pay`
- `PUT /api/orders/{id}/ship`
- `PUT /api/orders/{id}/deliver`
- `PUT /api/orders/{id}/cancel`

### Reportes
- `GET /api/reports/best-selling-products`
- `GET /api/reports/monthly-income`
- `GET /api/reports/top-customers`
- `GET /api/reports/low-stock-products` 

---

## 9. Consultas de repositorio

El proyecto contempla consultas derivadas y consultas de agregación/JPQL.

### Consultas derivadas mínimas
- Buscar producto por SKU.
- Buscar productos activos por categoría.
- Buscar pedidos por cliente.
- Buscar productos con bajo stock. :contentReference[oaicite:22]{index=22}

### Consultas JPQL / agregación
- Pedidos por filtros combinados.
- Productos más vendidos por período.
- Ingresos mensuales agrupados.
- Clientes con mayor facturación.
- Productos con stock insuficiente respecto al mínimo.
- Historial de cambios de un pedido.
- Top de categorías por volumen de ventas. :contentReference[oaicite:23]{index=23}

---

## 10. Estrategia de pruebas

El proyecto contempla tres niveles de pruebas:

### 10.1 Repository integration tests
Se prueban consultas reales sobre PostgreSQL con Testcontainers.  
Pruebas mínimas esperadas:
- `ProductRepositoryIntegrationTest`
- `InventoryRepositoryIntegrationTest`
- `OrderRepositoryIntegrationTest`
- `CustomerRepositoryIntegrationTest` :contentReference[oaicite:24]{index=24}

### 10.2 Service unit tests
Se valida la lógica de negocio con Mockito.  
Pruebas mínimas esperadas:
- `OrderServiceImplTest`
- `InventoryServiceImplTest`
- `ProductServiceImplTest` :contentReference[oaicite:25]{index=25}

### 10.3 Controller tests
Se validan endpoints, respuestas HTTP, errores de validación y escenarios de negocio con `MockMvc`.  
Pruebas mínimas esperadas:
- `OrderControllerTest`
- `ProductControllerTest`
- `CustomerControllerTest`
- `ReportControllerTest` :contentReference[oaicite:26]{index=26}

---

## 11. Forma de ejecución

### 11.1 Requisitos previos
- Java 21 instalado
- Maven instalado
- PostgreSQL disponible
- Docker activo para ejecutar pruebas con Testcontainers

### 11.2 Clonar el proyecto
```bash
git clone <URL_DEL_REPOSITORIO>
cd ecommerce
