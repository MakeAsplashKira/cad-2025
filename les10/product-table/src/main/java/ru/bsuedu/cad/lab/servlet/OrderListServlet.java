package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;

public class OrderListServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Order List</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; width: 80%; margin: 20px auto; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("button { padding: 10px; background-color: #008CBA; color: white; ");
        out.println("        border: none; border-radius: 4px; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1 style='text-align:center;'>Order List</h1>");

        out.println("<div style='text-align:center; margin:20px;'>");
        out.println("<button onclick=\"window.location.href='/app/order-form'\">");
        out.println("➕ Create New Order");
        out.println("</button>");
        out.println("</div>");

        // Orders table
        out.println("<table>");
        out.println("<thead>");
        out.println("<tr><th>Order ID</th><th>Customer</th><th>Date</th><th>Total</th><th>Status</th><th>Address</th></tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (Order order : orderService.getAllOrdersWithCustomer()) {
            out.println("<tr>");
            out.println("<td>" + order.getOrderId() + "</td>");
            out.println("<td>" + order.getCustomer().getName() + "</td>");
            out.println("<td>" + order.getOrderDate() + "</td>");
            out.println("<td>" + order.getTotalPrice() + " RUB</td>");
            out.println("<td>" + order.getStatus() + "</td>");
            out.println("<td>" + order.getShippingAddress() + "</td>");
            out.println("</tr>");
        }

        out.println("</tbody>");
        out.println("</table>");

        out.println("</body>");
        out.println("</html>");
    }
}