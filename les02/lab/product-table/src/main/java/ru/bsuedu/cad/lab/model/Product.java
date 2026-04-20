package ru.bsuedu.cad.lab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Product {
    Long productId; //0
    String name; //1
    String description; //2
    int categoryId; //3
    BigDecimal price; //4
    int stockQuantity; //5
    String imageUrl; //6
    Date createdAt; //7
    Date updatedAt; //8
}
