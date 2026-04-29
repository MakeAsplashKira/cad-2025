package ru.bsuedu.cad.lab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRequest.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void executeQuery() {
        logger.info("категории с кол-вом товаров > 1");

        String sql = """
            SELECT c.name, c.category_id, COUNT(p.product_id)
            FROM CATEGORIES c
            JOIN PRODUCTS p ON c.category_id = p.category_id
            GROUP BY c.category_id, c.name
            HAVING COUNT(p.product_id) > 1
            """;

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            logger.info("Категории с количеством товаров больше 1 не найдены");
        } else {
            for (Object[] row : results) {
                String name = (String) row[0];
                Long categoryId = ((Number) row[1]).longValue();
                Long productCount = ((Number) row[2]).longValue();
                logger.info("Категория: {} (ID: {}) - товаров: {}", name, categoryId, productCount);
            }
        }
    }
}