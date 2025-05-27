package Servlet;

import Beans.Products;
import Utils.DBProduct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author HP
 */
@WebServlet("/ProductByCategory")
public class ProductByCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductByCategoryServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Received request for products with categoryId: {}", request.getParameter("categoryId"));

        String categoryId = request.getParameter("categoryId");

        if (categoryId != null && !categoryId.isEmpty()) {
            try {
                // Lấy danh sách sản phẩm từ DBProduct theo categoryId
                int catId = Integer.parseInt(categoryId);
                logger.debug("Fetching products for category ID: {}", catId);
                ArrayList<Products> productList = DBProduct.findByCategoryId(catId);

                // Đưa danh sách sản phẩm vào request
                request.setAttribute("productList", productList);
                logger.info("Successfully retrieved {} products for category ID: {}", productList.size(), catId);

                // Chuyển tiếp tới trang JSP để hiển thị sản phẩm
                request.getRequestDispatcher("products.jsp").forward(request, response);
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Error retrieving products for category ID: {}", categoryId, e);
                e.printStackTrace();
                request.setAttribute("errorMessage", "There was an error retrieving the products.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.warn("Invalid category ID format: {}", categoryId);
                request.setAttribute("errorMessage", "Invalid category ID format.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            logger.warn("No category ID provided in request");
            request.setAttribute("errorMessage", "Invalid category ID.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}