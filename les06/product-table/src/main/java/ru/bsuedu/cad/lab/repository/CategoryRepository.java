package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    @Query(value = """
    SELECT c.category_id, c.name, c.description, COUNT(p.product_id) as product_count
    FROM CATEGORIES c
    JOIN PRODUCTS p ON c.category_id = p.category_id
    GROUP BY c.category_id, c.name, c.description
    HAVING COUNT(p.product_id) > 1
    """, nativeQuery = true)
    List<Object[]> findCategoriesWithMoreThanOneProduct();
}
