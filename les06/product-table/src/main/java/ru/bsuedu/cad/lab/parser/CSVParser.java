package ru.bsuedu.cad.lab.parser;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Product;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParser implements Parser{
    @Override
    public List<Product> parseProducts(List<String> lines) {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(",");

            Product product = new Product(
                    columns[1].trim(),
                    columns[2].trim(),
                    Long.parseLong(columns[3]),
                    BigDecimal.valueOf(Double.parseDouble(columns[4])),
                    Integer.parseInt(columns[5]),
                    columns[6].trim(),
                    Date.valueOf(columns[7]),
                    Date.valueOf(columns[8])
            );
            products.add(product);
        }
        return products;
    }

    public List<Category> parseCategories(List<String> lines) {
        List<Category> categories = new ArrayList<>();

        for(int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(",");
            if(columns.length>=3) {
                Category category = new Category();
                category.setName(columns[1].trim());
                category.setDescription(columns[2].trim());
                categories.add(category);
            }
        }
        return categories;
    }
}
