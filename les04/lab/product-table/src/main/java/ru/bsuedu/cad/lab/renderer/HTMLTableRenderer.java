package ru.bsuedu.cad.lab.renderer;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class HTMLTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Override
    public void render() {
        List<Product> products = provider.getProducts();
        String fileName = "products.html";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generateHtml(products));
            System.out.println("HTML-отчёт создан: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении HTML-файла: " + e.getMessage());
        }
    }

    private String generateHtml(List<Product> products) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Товары интернет-магазина</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("        h1 { color: #333; text-align: center; }\n");
        html.append("        table { border-collapse: collapse; width: 100%; margin-top: 20px; }\n");
        html.append("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        html.append("        th { background-color: #4CAF50; color: white; }\n");
        html.append("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        html.append("    <h1>Список товаров</h1>\n");
        html.append("    <table>\n");
        html.append("        <tr>\n");
        html.append("            <th>ID</th>\n");
        html.append("            <th>Название</th>\n");
        html.append("            <th>Описание</th>\n");
        html.append("            <th>Категория</th>\n");
        html.append("            <th>Цена</th>\n");
        html.append("            <th>Количество</th>\n");
        html.append("        </tr>\n");

        for (Product product : products) {
            html.append("        <tr>\n");
            html.append(String.format("            <td>%s</td>\n", String.valueOf(product.getProductId())));
            html.append(String.format("            <td>%s</td>\n", product.getName()));
            html.append(String.format("            <td>%s</td>\n", product.getDescription()));
            html.append(String.format("            <td>%s</td>\n", String.valueOf(product.getCategoryId())));
            html.append(String.format("            <td>%.2f ₽</td>\n", product.getPrice()));
            html.append(String.format("            <td>%d</td>\n", product.getStockQuantity()));
            html.append("        </tr>\n");
        }

        html.append("    </table>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }


}