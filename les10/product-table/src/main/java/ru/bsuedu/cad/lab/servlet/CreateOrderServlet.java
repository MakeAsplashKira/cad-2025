package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.service.OrderItemRequest;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        Long customerId = Long.parseLong(req.getParameter("customerId"));
        String shippingAddress = req.getParameter("shippingAddress");

        String[] productIds = req.getParameterValues("productId");
        String[] quantities = req.getParameterValues("quantity");

        List<OrderItemRequest> items = new ArrayList<>();
        for (int i = 0; i < productIds.length; i++) {
            if (productIds[i] != null && !productIds[i].isEmpty()) {
                Long productId = Long.parseLong(productIds[i]);
                Integer quantity = Integer.parseInt(quantities[i]);
                items.add(new OrderItemRequest(productId, quantity));
            }
        }

        var order = orderService.createOrder(customerId, shippingAddress, items);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}