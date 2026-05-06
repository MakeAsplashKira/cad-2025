package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private ProductRepository productRepository;
    @Mock private OrderDetailRepository orderDetailRepository;

    @InjectMocks private OrderService orderService;

    @Test
    void createOrder_success() {
        Customer customer = new Customer("Иван Иванов", "ivan@test.com", "+7999", "Москва");
        customer.setCustomerId(1L);

        Category category = new Category("Корма", "Описание");
        Product product = new Product("Корм", "Описание", category, new BigDecimal("1500"), 50, "url");
        product.setProductId(1L);

        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setCustomer(customer);
        savedOrder.setStatus("NEW");
        savedOrder.setShippingAddress("Москва, ул. Тестовая, 1");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.createOrder(1L, "Москва, ул. Тестовая, 1",
                List.of(new OrderItemRequest(1L, 2)));

        assertThat(result).isNotNull();
        assertThat(result.getCustomer().getName()).isEqualTo("Иван Иванов");
        assertThat(result.getStatus()).isEqualTo("NEW");
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderDetailRepository, times(1)).save(any(OrderDetail.class));
    }

    @Test
    void createOrder_customerNotFound_throwsException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.createOrder(99L, "Адрес", List.of(new OrderItemRequest(1L, 1)))
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Customer not found");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_productNotFound_throwsException() {
        Customer customer = new Customer("Иван", "ivan@test.com", "+7999", "Москва");
        customer.setCustomerId(1L);
        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setCustomer(customer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.createOrder(1L, "Адрес", List.of(new OrderItemRequest(99L, 1)))
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Product not found");
    }

    @Test
    void getOrderById_success() {
        Customer customer = new Customer("Мария", "maria@test.com", "+7111", "СПб");
        customer.setCustomerId(2L);
        Order order = new Order(customer, "СПб, Невский пр., 1");
        order.setOrderId(1L);

        when(orderRepository.findByIdWithCustomer(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getCustomer().getName()).isEqualTo("Мария");
    }

    @Test
    void getOrderById_notFound_throwsException() {
        when(orderRepository.findByIdWithCustomer(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void updateOrder_success() {
        Customer customer = new Customer("Сергей", "sergey@test.com", "+7222", "Казань");
        Order order = new Order(customer, "Казань, ул. Баумана, 1");
        order.setOrderId(1L);
        order.setStatus("NEW");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.updateOrder(1L, "SHIPPED", "Казань, ул. Баумана, 5");

        assertThat(result.getStatus()).isEqualTo("SHIPPED");
        assertThat(result.getShippingAddress()).isEqualTo("Казань, ул. Баумана, 5");
    }

    @Test
    void updateOrder_notFound_throwsException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(99L, "SHIPPED", "Адрес"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Order not found");
    }

    @Test
    void deleteOrder_callsRepository() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllOrders_returnsOrders() {
        Customer customer = new Customer("Тест", "test@test.com", "+7000", "Тест");
        Order order1 = new Order(customer, "Адрес 1");
        Order order2 = new Order(customer, "Адрес 2");

        when(orderRepository.findAllWithCustomer()).thenReturn(List.of(order1, order2));

        List<Order> result = orderService.getAllOrders();

        assertThat(result).hasSize(2);
        verify(orderRepository).findAllWithCustomer();
    }
}
