package ru.bsuedu.cad.lab.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductRepository productRepository;

    public ProductRestController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductInfo)
                .collect(Collectors.toList());
    }

    private Map<String, Object> toProductInfo(Product product) {
        Map<String, Object> info = new HashMap<>();
        info.put("productName", product.getName());
        info.put("categoryName", product.getCategory() != null ? product.getCategory().getName() : "Без категории");
        info.put("stockQuantity", product.getStockQuantity());
        return info;
    }
}