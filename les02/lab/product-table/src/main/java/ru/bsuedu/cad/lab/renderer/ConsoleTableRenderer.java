package ru.bsuedu.cad.lab.renderer;

import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.model.Product;

import java.util.List;

public class ConsoleTableRenderer {
    public void render(List<Product> products) {
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
                    p.getCategoryId(),
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
