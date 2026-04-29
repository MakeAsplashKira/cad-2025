package ru.bsuedu.cad.lab.provider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.parser.CSVParser;
import ru.bsuedu.cad.lab.parser.Parser;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcreteProductProvider implements ProductProvider {
    private final ResourceFileReader reader;
    private final Parser parser;

    @Override
    public List<Product> getProducts() {
        try {
            List<String> lines = reader.read();
            return parser.parseProducts(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return  List.of();
        }
    }
}
