## Отчет о лаботаротоной работе №4.  Технологии работы с базами данных. JPA. Spring Data

#### Ход работы

#### Задание 1-3. Созданий новое приложение или скопируйте результат выполнения лабораторной работы №3 в директорию /les08/lab/. Изменений будет много, возможно для вас будет проще создать проект заново.

Скопировал папку product-table из директории /les06 в /less08. Удалил ненужные классы.
Установил нужные зависимости (H2, HikariCP).

#### Задание 4. В пакете ru.bsuedu.cad.lab.entity создайте JPA сущности.

```java
//Product.java
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails =  new ArrayList<>();

    public Product(String name, String description, Category category, BigDecimal price, Integer stockQuantity, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }
}

//Category.java
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "CATEGORIES")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
//Customer.java
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name="CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Customer(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}

//Order.java
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "ORDERS")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "status", length = 50)
    private String status = "NEW";

    @Column(name = "shipping_address", length = 255)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order(Customer customer, String shippingAddress) {
        this.customer = customer;
        this.shippingAddress = shippingAddress;
    }

}

//OrderDetail.java
package ru.bsuedu.cad.lab.entity;

@Entity
@Table(name = "ORDER_DETAILS")
@Setter
@Getter
@NoArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    public OrderDetail(Order order, Product product, Integer quantity, BigDecimal price) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
```
#### Задание 5. В пакете ru.bsuedu.cad.lab.repository реализуйте репозитории для каждой сущности. Репозитории содержать методы по созданию, получение записи по идентификатору и получения всех записей для каждой сущности.

Пример одного из репозиториев представлен в коде ниже.

```java
package ru.bsuedu.cad.lab.repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

```

#### Задание 6. В пакете ru.bsuedu.cad.lab.service создайте сервисы для создания заказа и получению списка всех заказов.

```java
package ru.bsuedu.cad.lab.service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Transactional
    public Order createOrder(Long customerId, String shippingAddress, List<OrderItemRequest> items) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setStatus("NEW");

        Order savedOrder = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setPrice(product.getPrice());

            orderDetailRepository.save(orderDetail);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        savedOrder.setTotalPrice(totalPrice);
        Order finalOrder = orderRepository.save(savedOrder);


        return finalOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrdersWithCustomer() {
        return orderRepository.findAllWithCustomer();
    }
}

```

#### Задание 7. В пакете ru.bsuedu.cad.lab.app реализуйте клиент для сервиса создания заказа, который создает новый заказ. Создание заказа должно выполняться в рамках транзакции. Выведите информацию о создании заказа в лог. Докажите, что заказ сохранился в базе данных.


```java
package ru.bsuedu.cad.lab.app;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);

        OrderService orderService = context.getBean(OrderService.class);

        logger.info("Создание нового заказа...");

        Long customerId = 1L;
        String shippingAddress = "г. Москва, ул. Примерная, д. 1";

        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(1L, 2),
                new OrderItemRequest(3L, 1)
        );

        try {
            var order = orderService.createOrder(customerId, shippingAddress, items);

            logger.info("Заказ успешно создан!");
            logger.info("ID заказа: {}", order.getOrderId());
            logger.info("Клиент: {}", order.getCustomer().getName());
            logger.info("Сумма заказа: {} ₽", order.getTotalPrice());
            logger.info("Статус: {}", order.getStatus());
            logger.info("Адрес доставки: {}", order.getShippingAddress());

            logger.info("ПРОВЕРКА: вывод заказа из БД");
            orderService.getAllOrdersWithCustomer().forEach(o -> {
                logger.info("Заказ #{} | Клиент: {} | Сумма: {} ₽ | Статус: {}",
                        o.getOrderId(),
                        o.getCustomer().getName(),
                        o.getTotalPrice(),
                        o.getStatus());
            });

        } catch (Exception e) {
            logger.error("Ошибка при создании заказа: {}", e.getMessage(), e);
        }

        ((AnnotationConfigApplicationContext) context).close();
    }
}
```

Пример вывода:
13:25:41.587 [main] INFO  r.b.cad.lab.service.DataInitializer - Загружено категорий: 10
13:25:41.641 [main] INFO  r.b.cad.lab.service.DataInitializer - Загружено продуктов: 10
13:25:41.656 [main] INFO  r.b.cad.lab.service.DataInitializer - Загружено клиентов: 10
13:25:41.875 [main] INFO  ru.bsuedu.cad.lab.app.App - Создание нового заказа...
13:25:41.898 [main] INFO  ru.bsuedu.cad.lab.app.App - Заказ успешно создан!
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - ID заказа: 1
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - Клиент: Алексей Иванов
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - Сумма заказа: 3500.00 ₽
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - Статус: NEW
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - Адрес доставки: г. Москва, ул. Примерная, д. 1
13:25:41.899 [main] INFO  ru.bsuedu.cad.lab.app.App - ПРОВЕРКА: вывод заказа из БД
13:25:41.930 [main] INFO  ru.bsuedu.cad.lab.app.App - Заказ #1 | Клиент: Алексей Иванов | Сумма: 3500.00 ₽ | Статус: NEW



## Выводы
 Получил базовое понимание технологии работы с базами данных JPA, Spring Data.