package ru.bsuedu.cad.lab.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bsuedu.cad.lab.config.DatabaseConfig;
import ru.bsuedu.cad.lab.service.OrderItemRequest;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.List;

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