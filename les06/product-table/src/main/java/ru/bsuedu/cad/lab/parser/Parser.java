package ru.bsuedu.cad.lab.parser;

import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Product;

import java.util.List;

public interface Parser {
    List<Product> parseProducts(List<String> lines);
    List<Category> parseCategories(List<String> lines);
}
