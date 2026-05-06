package ru.bsuedu.cad.lab.dto;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private String status;
    private String shippingAddress;
}
