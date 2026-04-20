package ru.bsuedu.cad.lab.provider;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.parser.CSVParser;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;

import java.util.List;

@Component
public class ConcreteProductProvider {
    private final ResourceFileReader reader;
    private final CSVParser parser;

    public ConcreteProductProvider(ResourceFileReader reader, CSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    public List<Product> getProducts() {
        try {
            List<String> lines = reader.readLines("product.csv");
            return parser.parse(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return  List.of();
        }
    }
}
