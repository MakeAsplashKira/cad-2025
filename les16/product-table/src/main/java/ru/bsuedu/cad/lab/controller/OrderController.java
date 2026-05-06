package ru.bsuedu.cad.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bsuedu.cad.lab.service.OrderItemRequest;
import ru.bsuedu.cad.lab.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customers", orderService.getAllCustomers());
        model.addAttribute("products", orderService.getAllProducts());
        return "orders/form";
    }

    @PostMapping
    public String createOrder(
            @RequestParam Long customerId,
            @RequestParam String shippingAddress,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            RedirectAttributes redirectAttributes) {
        try {
            List<OrderItemRequest> items = new ArrayList<>();
            if (productIds != null) {
                for (int i = 0; i < productIds.size(); i++) {
                    Long productId = productIds.get(i);
                    Integer qty = (quantities != null && i < quantities.size()) ? quantities.get(i) : 1;
                    if (productId != null && productId > 0 && qty != null && qty > 0) {
                        items.add(new OrderItemRequest(productId, qty));
                    }
                }
            }
            if (!items.isEmpty()) {
                orderService.createOrder(customerId, shippingAddress, items);
                redirectAttributes.addFlashAttribute("success", "Заказ успешно создан");
            } else {
                redirectAttributes.addFlashAttribute("error", "Выберите хотя бы один товар");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "orders/edit";
    }

    @PostMapping("/{id}")
    public String updateOrder(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String shippingAddress,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrder(id, status, shippingAddress);
            redirectAttributes.addFlashAttribute("success", "Заказ обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Заказ удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/orders";
    }
}
