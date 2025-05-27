package Servlet;

import Beans.Categories;
import Beans.Products;
import Beans.DBConnection;
import Utils.DBCategories;
import Utils.DBProduct;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ProductListServlet
 */
@WebServlet("/ProductList")
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductListServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        logger.info("Received request for product list with search query: {}", request.getParameter("search"));

        String searchQuery = request.getParameter("search");
        List<Products> productList = null;
        List<Categories> categoriesList = null;

        try {
            categoriesList = DBCategories.getAllCategories();
            logger.debug("Retrieved {} categories", categoriesList.size());
            request.setAttribute("categoriesList", categoriesList);

            try (Connection connection = DBConnection.getConnection()) {
                DBProduct dbProduct = new DBProduct(connection);

                if (searchQuery != null && !searchQuery.isEmpty()) {
                    // Tìm kiếm sản phẩm theo tên
                    logger.debug("Searching products with query: {}", searchQuery);
                    productList = dbProduct.searchProductsByName(searchQuery);
                } else {
                    // Lấy tất cả sản phẩm nếu không có tìm kiếm
                    logger.debug("Fetching all products");
                    productList = dbProduct.getAllProducts();
                }
                logger.info("Successfully retrieved {} products", productList != null ? productList.size() : 0);
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Error retrieving products with search query: {}", searchQuery, e);
                request.setAttribute("errorMessage", "Cannot retrieve products.");
            }
        } catch (Exception e) {
            logger.error("Error retrieving categories", e);
            request.setAttribute("errorMessage", "Cannot retrieve categories.");
        }

        // Đặt danh sách sản phẩm vào request
        request.setAttribute("productList", productList);

        // Chuyển hướng đến trang hiển thị
        logger.debug("Forwarding to user_home.jsp");
        request.getRequestDispatcher("user_home.jsp").forward(request, response);
    }
}