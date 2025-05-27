package Servlet;

import Beans.Categories;
import Utils.DBCategories;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "UserCategoryServlet", urlPatterns = {"/UserCategory"})
public class UserCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserCategoryServlet.class);

    public UserCategoryServlet() {
        super();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String action = request.getParameter("action");

        logger.info("Received request with action: {}", action != null ? action : "none");

        if (action == null || action.isEmpty() || "list".equals(action)) {
            try {
                logger.debug("Fetching all categories from database");
                // Truy vấn danh sách categories từ cơ sở dữ liệu
                List<Categories> categoriesList = DBCategories.getAllCategories();
                if (categoriesList != null && !categoriesList.isEmpty()) {
                    // Đặt danh sách categories vào request
                    request.setAttribute("categoriesList", categoriesList);
                    logger.info("Retrieved {} categories successfully", categoriesList.size());
                } else {
                    logger.warn("No categories found in database");
                    request.setAttribute("errorMessage", "No categories found.");
                }
                // Chuyển tiếp đến user_home.jsp
                logger.debug("Forwarding to user_home.jsp");
                request.getRequestDispatcher("user_home.jsp").forward(request, response);
            } catch (Exception e) {
                logger.error("Error retrieving categories: {}", e.getMessage(), e);
                request.setAttribute("errorMessage", "Error retrieving categories: " + e.getMessage());
                logger.debug("Forwarding to error.jsp due to error");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            logger.warn("Invalid or unsupported action: {}", action);
            request.setAttribute("errorMessage", "Invalid action.");
            logger.debug("Forwarding to error.jsp due to invalid action");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        logger.debug("Processing GET request");
        processRequest(request, response);
    }
}