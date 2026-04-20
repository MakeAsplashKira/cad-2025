package ru.bsuedu.cad.lab.parser;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParser {
    public List<Product> parse(List<String> lines) {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] columns = line.split(",");

            Product product = new Product(
                    Long.parseLong(columns[0]),
                    columns[1].trim(),
                    columns[2].trim(),
                    Integer.parseInt(columns[3]),
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
}
