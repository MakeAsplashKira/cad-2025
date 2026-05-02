package ru.bsuedu.cad.lab.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.io.PrintWriter;

public class OrderFormServlet extends HttpServlet {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        var context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        customerRepository = context.getBean(CustomerRepository.class);
        productRepository = context.getBean(ProductRepository.class);
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
        out.println("<head><title>Create Order</title>");
        out.println("<style>");
        out.println("form { width: 50%; margin: 20px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; }");
        out.println("label { display: block; margin-top: 10px; }");
        out.println("select, input { width: 100%; padding: 8px; margin-top: 5px; }");
        out.println("button { margin-top: 20px; padding: 10px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println(".product-row { margin: 10px 0; padding: 10px; border: 1px solid #eee; }");
        out.println("</style>");
        out.println("<script>");
        out.println("let productIndex = 0;");
        out.println("function addProduct() {");
        out.println("  const container = document.getElementById('products-container');");
        out.println("  const div = document.createElement('div');");
        out.println("  div.className = 'product-row';");
        out.println("  div.innerHTML = `");
        out.println("    <select name='productId' required>");
        out.println("      <option value=''>Select product</option>");

        for (Product product : productRepository.findAll()) {
            out.println("      <option value='" + product.getProductId() + "'>"
                    + product.getName() + " - " + product.getPrice() + " RUB</option>");
        }

        out.println("    </select>");
        out.println("    <input type='number' name='quantity' placeholder='Quantity' min='1' required>");
        out.println("    <button type='button' onclick='this.parentElement.remove()'>Remove</button>`;");
        out.println("  container.appendChild(div);");
        out.println("}");
        out.println("</script>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h1 style='text-align:center;'>Create New Order</h1>");

        out.println("<form action='/app/create-order' method='POST'>");

        // Customer selection
        out.println("<label>Customer:</label>");
        out.println("<select name='customerId' required>");
        out.println("<option value=''>Select customer</option>");
        for (Customer customer : customerRepository.findAll()) {
            out.println("<option value='" + customer.getCustomerId() + "'>"
                    + customer.getName() + " - " + customer.getEmail() + "</option>");
        }
        out.println("</select>");

        out.println("<label>Shipping address:</label>");
        out.println("<input type='text' name='shippingAddress' required>");

        out.println("<label>Products:</label>");
        out.println("<div id='products-container'>");
        out.println("<div class='product-row'>");
        out.println("<select name='productId' required>");
        out.println("<option value=''>Select product</option>");
        for (Product product : productRepository.findAll()) {
            out.println("<option value='" + product.getProductId() + "'>"
                    + product.getName() + " - " + product.getPrice() + " RUB</option>");
        }
        out.println("</select>");
        out.println("<input type='number' name='quantity' placeholder='Quantity' min='1' required>");
        out.println("<button type='button' onclick='this.parentElement.remove()'>Remove</button>");
        out.println("</div>");
        out.println("</div>");

        out.println("<button type='button' onclick='addProduct()'>➕ Add product</button>");
        out.println("<button type='submit'>✅ Create order</button>");

        out.println("</form>");

        out.println("<div style='text-align:center; margin-top:20px;'>");
        out.println("<button onclick=\"window.location.href='/app/orders'\">← Back to orders</button>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }
}