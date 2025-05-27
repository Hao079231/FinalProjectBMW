package Servlet;

import Beans.Categories;
import Utils.DBCategories;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Category")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServlet.class);

    public CategoryServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        String action = request.getParameter("action");
        logger.info("GET request received from IP: {}, action: {}", remoteIp, action);

        if (action == null || action.isEmpty() || "list".equals(action)) {
            // Mặc định hiển thị danh sách loại sản phẩm
            try {
                List<Categories> categoriesList = DBCategories.getAllCategories();
                logger.info("Retrieved {} categories from database, IP: {}", categoriesList.size(), remoteIp);
                request.setAttribute("categoriesList", categoriesList);
                request.getRequestDispatcher("category_list.jsp").forward(request, response);
            } catch (Exception e) {
                logger.error("Error retrieving categories from IP: {}, error: {}", remoteIp, e.getMessage());
                request.setAttribute("errorMessage", "Error retrieving categories: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else if ("edit".equals(action)) {
            // Hiển thị thông tin loại sản phẩm để chỉnh sửa
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                Categories category = DBCategories.getCategoryById(categoryId);
                if (category == null) {
                    logger.warn("Category not found for ID: {}, IP: {}", categoryId, remoteIp);
                    request.setAttribute("errorMessage", "Category not found for ID: " + categoryId);
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                logger.info("Retrieved category ID: {} for editing, IP: {}", categoryId, remoteIp);
                request.setAttribute("category", category);
                request.getRequestDispatcher("category_edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.error("Invalid category ID format from IP: {}, parameter: {}, error: {}",
                        remoteIp, request.getParameter("categoryId"), e.getMessage());
                request.setAttribute("errorMessage", "Invalid category ID: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (Exception e) {
                logger.error("Error retrieving category for editing from IP: {}, error: {}", remoteIp, e.getMessage());
                request.setAttribute("errorMessage", "Error retrieving category: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else if ("delete".equals(action)) {
            // Xóa loại sản phẩm
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                DBCategories.deleteCategoryById(categoryId);
                logger.info("Deleted category ID: {} from IP: {}", categoryId, remoteIp);
                response.sendRedirect("Category?action=list");
            } catch (NumberFormatException e) {
                logger.error("Invalid category ID format for deletion from IP: {}, parameter: {}, error: {}",
                        remoteIp, request.getParameter("categoryId"), e.getMessage());
                request.setAttribute("errorMessage", "Invalid category ID: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            } catch (Exception e) {
                logger.error("Error deleting category from IP: {}, error: {}", remoteIp, e.getMessage());
                request.setAttribute("errorMessage", "Error deleting category: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            logger.warn("Unknown action: {} from IP: {}", action, remoteIp);
            request.setAttribute("errorMessage", "Invalid action: " + action);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        String action = request.getParameter("action");
        logger.info("POST request received from IP: {}, action: {}", remoteIp, action);

        try {
            if ("add".equals(action)) {
                // Thêm loại sản phẩm mới
                String cname = request.getParameter("cname");
                if (cname == null || cname.trim().isEmpty()) {
                    logger.warn("Invalid category name for add action from IP: {}", remoteIp);
                    request.setAttribute("errorMessage", "Category name cannot be empty");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                Categories newCategory = new Categories(0, cname);
                DBCategories.addCategory(newCategory);
                logger.info("Added new category: {} from IP: {}", cname, remoteIp);
                response.sendRedirect("Category?action=list");
            } else if ("update".equals(action)) {
                // Cập nhật loại sản phẩm
                String categoryIdStr = request.getParameter("categoryId");
                String cname = request.getParameter("cname");
                if (cname == null || cname.trim().isEmpty()) {
                    logger.warn("Invalid category name for update action from IP: {}", remoteIp);
                    request.setAttribute("errorMessage", "Category name cannot be empty");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                int categoryId = Integer.parseInt(categoryIdStr);
                Categories updatedCategory = new Categories(categoryId, cname);
                DBCategories.updateCategory(updatedCategory);
                logger.info("Updated category ID: {} with name: {} from IP: {}", categoryId, cname, remoteIp);
                response.sendRedirect("Category?action=list");
            } else {
                logger.warn("Unknown action: {} from IP: {}", action, remoteIp);
                request.setAttribute("errorMessage", "Invalid action: " + action);
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid category ID format for action: {} from IP: {}, parameter: {}, error: {}",
                    action, remoteIp, request.getParameter("categoryId"), e.getMessage());
            request.setAttribute("errorMessage", "Invalid category ID: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error processing action: {} from IP: {}, error: {}", action, remoteIp, e.getMessage());
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}