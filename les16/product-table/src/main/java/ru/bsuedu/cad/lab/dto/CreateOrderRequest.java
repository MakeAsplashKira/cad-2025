package ru.bsuedu.cad.lab.dto;

import lombok.Data;
import ru.bsuedu.cad.lab.service.OrderItemRequest;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long customerId;
    private String shippingAddress;
    private List<OrderItemRequest> items;
}
