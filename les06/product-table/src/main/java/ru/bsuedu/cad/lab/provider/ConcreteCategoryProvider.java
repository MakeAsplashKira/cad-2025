package ru.bsuedu.cad.lab.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.parser.CSVParser;
import ru.bsuedu.cad.lab.reader.ResourceFileReader;

import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
    private final ResourceFileReader reader;
    private final CSVParser parser;

    @Value("${categories.file.name:category.csv}")
    private String fileName;

    public ConcreteCategoryProvider(ResourceFileReader reader, CSVParser parser) {
        this.reader = reader;
        this.parser = parser;
    }

    @Override
    public List<Category> getCategories() {
        try {
            List<String> lines = reader.read();
            return parser.parseCategories(lines);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
