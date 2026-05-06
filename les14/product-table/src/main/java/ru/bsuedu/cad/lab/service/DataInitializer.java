package ru.bsuedu.cad.lab.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.bsuedu.cad.lab.entity.*;
import ru.bsuedu.cad.lab.repository.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(CategoryRepository categoryRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        try {
            if (categoryRepository.count() == 0) loadCategories();
            if (productRepository.count() == 0) loadProducts();
            if (customerRepository.count() == 0) loadCustomers();
        } catch (Exception e) {
            logger.error("Ошибка при загрузке данных: {}", e.getMessage(), e);
        }
    }

    private void loadCategories() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/category.csv"), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Category category = new Category();
                    category.setName(parts[1].trim());
                    category.setDescription(parts[2].trim());
                    categoryRepository.save(category);
                }
            }
        }
        logger.info("Загружено категорий: {}", categoryRepository.count());
    }

    private void loadProducts() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/product.csv"), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    Product product = new Product();
                    product.setName(parts[1].trim());
                    product.setDescription(parts[2].trim());
                    Long categoryId = Long.parseLong(parts[3].trim());
                    Category category = categoryRepository.findById(categoryId).orElse(null);
                    product.setCategory(category);
                    product.setPrice(new BigDecimal(parts[4].trim()));
                    product.setStockQuantity(Integer.parseInt(parts[5].trim()));
                    product.setImageUrl(parts[6].trim());
                    product.setCreatedAt(LocalDateTime.parse(parts[7].trim() + "T00:00:00"));
                    product.setUpdatedAt(LocalDateTime.parse(parts[8].trim() + "T00:00:00"));
                    productRepository.save(product);
                }
            }
        }
        logger.info("Загружено продуктов: {}", productRepository.count());
    }

    private void loadCustomers() throws Exception {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/customer.csv"), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Customer customer = new Customer();
                    customer.setName(parts[1].trim());
                    customer.setEmail(parts[2].trim());
                    customer.setPhone(parts[3].trim());
                    customer.setAddress(parts[4].trim());
                    customerRepository.save(customer);
                }
            }
        }
        logger.info("Загружено клиентов: {}", customerRepository.count());
    }
}
