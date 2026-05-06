package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.config.DatabaseConfig;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringJUnitConfig(classes = DatabaseConfig.class)
@Transactional
class OrderServiceIntegrationTest {

    @Autowired private OrderService orderService;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    private Customer testCustomer;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        Category category = new Category("Корма", "Корма для животных");
        categoryRepository.save(category);

        testProduct = new Product("Тестовый корм", "Описание", category,
                new BigDecimal("500"), 100, "url");
        productRepository.save(testProduct);

        testCustomer = new Customer("Тест Тестович", "test@integration.com",
                "+79999999999", "Тест ул. Тестовая 1");
        customerRepository.save(testCustomer);
    }

    @Test
    void createOrder_successfullyPersisted() {
        Order order = orderService.createOrder(
                testCustomer.getCustomerId(),
                "Москва, ул. Ленина, 1",
                List.of(new OrderItemRequest(testProduct.getProductId(), 2))
        );

        assertThat(order.getOrderId()).isNotNull();
        assertThat(order.getCustomer().getName()).isEqualTo("Тест Тестович");
        assertThat(order.getStatus()).isEqualTo("NEW");
        assertThat(order.getTotalPrice()).isEqualByComparingTo(new BigDecimal("1000"));

        Order fromDb = orderRepository.findByIdWithCustomer(order.getOrderId()).orElseThrow();
        assertThat(fromDb.getShippingAddress()).isEqualTo("Москва, ул. Ленина, 1");
    }

    @Test
    void createOrder_customerNotFound_throwsException() {
        assertThatThrownBy(() ->
                orderService.createOrder(999L, "Адрес",
                        List.of(new OrderItemRequest(testProduct.getProductId(), 1)))
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Customer not found");
    }

    @Test
    void createOrder_productNotFound_throwsException() {
        assertThatThrownBy(() ->
                orderService.createOrder(testCustomer.getCustomerId(), "Адрес",
                        List.of(new OrderItemRequest(999L, 1)))
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Product not found");
    }

    @Test
    void updateOrder_changesStatusAndAddress() {
        Order order = orderService.createOrder(
                testCustomer.getCustomerId(),
                "Изначальный адрес",
                List.of(new OrderItemRequest(testProduct.getProductId(), 1))
        );

        Order updated = orderService.updateOrder(order.getOrderId(), "SHIPPED", "Новый адрес");

        assertThat(updated.getStatus()).isEqualTo("SHIPPED");
        assertThat(updated.getShippingAddress()).isEqualTo("Новый адрес");
    }

    @Test
    void deleteOrder_removesFromDatabase() {
        Order order = orderService.createOrder(
                testCustomer.getCustomerId(),
                "Адрес для удаления",
                List.of(new OrderItemRequest(testProduct.getProductId(), 1))
        );
        Long orderId = order.getOrderId();

        orderService.deleteOrder(orderId);

        assertThat(orderRepository.findById(orderId)).isEmpty();
    }

    @Test
    void getAllOrders_returnsCreatedOrders() {
        orderService.createOrder(testCustomer.getCustomerId(), "Адрес 1",
                List.of(new OrderItemRequest(testProduct.getProductId(), 1)));
        orderService.createOrder(testCustomer.getCustomerId(), "Адрес 2",
                List.of(new OrderItemRequest(testProduct.getProductId(), 2)));

        List<Order> orders = orderService.getAllOrders();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(2);
    }
}
