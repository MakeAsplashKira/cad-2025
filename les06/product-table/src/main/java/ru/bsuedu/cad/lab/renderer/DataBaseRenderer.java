package ru.bsuedu.cad.lab.renderer;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.provider.CategoryProvider;
import ru.bsuedu.cad.lab.provider.ProductProvider;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Primary
@Component
public class DataBaseRenderer implements Renderer {

    private final ProductProvider productProvider;
    private final CategoryProvider categoryProvider;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public DataBaseRenderer(ProductProvider productProvider,
                            CategoryProvider categoryProvider,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository) {
        this.productProvider = productProvider;
        this.categoryProvider = categoryProvider;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void render() {
        System.out.println("Сохранение в БД");

        List<Category> categories = categoryProvider.getCategories();
        System.out.println("Загружено категорий из CSV: " + categories.size());
        if(categoryRepository.count() == 0) categoryRepository.saveAll(categories);

        List<Product> products = productProvider.getProducts();
        System.out.println("Загружено продуктов из CSV: " + products.size());
        if (productRepository.count() == 0) productRepository.saveAll(products);

        System.out.println("Всего категорий в БД: " + categoryRepository.count());
        System.out.println("Всего продуктов в БД: " + productRepository.count());
    }
}