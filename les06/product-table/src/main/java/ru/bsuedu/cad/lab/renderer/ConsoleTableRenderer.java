package ru.bsuedu.cad.lab.renderer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.provider.ProductProvider;

import java.util.List;

@Component
@AllArgsConstructor
public class ConsoleTableRenderer implements Renderer {
    private final ProductProvider provider;

    @Override
    public void render() {

        List<Product> products = provider.getProducts();

        System.out.println(
                "+------------" +
                "+----------------------------------" +
                "+-----------------" +
                "+----------" +
                "+--------+"
        );
        System.out.printf(
                "| %-10s | %-30s | %-15s | %-8s | %-6s |\n",
                "ID", "Название", "Цена", "Категория", "Остаток"
        );
        System.out.println("+------------+----------------------------------+-----------------+----------+--------+");

        for (Product p : products) {
            System.out.printf("| %-10s | %-30s | %-15.2f | %-8s | %-6d |\n",
                    p.getProductId(),
                    truncate(p.getName(), 30),
                    p.getPrice(),
                    p.getCategory(),
                    p.getStockQuantity()
            );
        }
        System.out.println("+------------+----------------------------------+-----------------+----------+--------+");
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
