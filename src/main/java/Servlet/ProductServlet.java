package Servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;

import Beans.Products;
import Beans.Categories;
import Beans.DBConnection;
import Utils.DBCategories;
import Utils.DBProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Product")
@MultipartConfig
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);

    public ProductServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String productId = request.getParameter("productId");
        String searchQuery = request.getParameter("search");
        String remoteIp = request.getRemoteAddr();

        logger.info("Received GET request from IP: {}, action: {}, productId: {}, searchQuery: {}",
                remoteIp, action, productId, searchQuery);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to GET /Product, action: {}", remoteIp, action);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Beans.Users user = (Beans.Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access GET /Product, action: {}",
                    user.getRole(), user.getUsername(), action);
            response.sendRedirect("error.jsp?message=AccessDenied");
            return;
        }

        if (action == null) {
            action = "";
        }

        try (Connection conn = DBConnection.getConnection()) {
            DBProduct dbProduct = new DBProduct(conn);

            if (action.equals("add")) {
                logger.debug("Fetching categories for add product page");
                List<Categories> categoriesList = DBCategories.getAllCategories();
                request.setAttribute("categoriesList", categoriesList);
                logger.info("Retrieved {} categories for add product page", categoriesList.size());
                getServletContext().getRequestDispatcher("/addProduct.jsp").forward(request, response);

            } else if (action.equals("edit")) {
                if (productId != null && !productId.isEmpty()) {
                    logger.debug("Fetching product with ID: {}", productId);
                    Products product = dbProduct.getProductById(Integer.parseInt(productId));
                    if (product == null) {
                        logger.warn("Product not found for ID: {}", productId);
                        response.sendRedirect("Product?status=error");
                        return;
                    }
                    request.setAttribute("product", product);
                    List<Categories> categoriesList = DBCategories.getAllCategories();
                    request.setAttribute("categoriesList", categoriesList);
                    logger.info("Retrieved product ID: {} and {} categories for edit page", productId, categoriesList.size());
                    getServletContext().getRequestDispatcher("/editProduct.jsp").forward(request, response);
                } else {
                    logger.warn("No product ID provided for edit action");
                    response.sendRedirect("Product");
                }

            } else if (action.equals("delete")) {
                if (productId != null && !productId.isEmpty()) {
                    logger.info("Deleting product with ID: {}", productId);
                    dbProduct.deleteProduct(Integer.parseInt(productId));
                    logger.info("Successfully deleted product ID: {}", productId);
                } else {
                    logger.warn("No product ID provided for delete action");
                }
                response.sendRedirect("Product");

            } else if (searchQuery != null && !searchQuery.isEmpty()) {
                logger.debug("Searching products with query: {}", searchQuery);
                List<Products> productList = dbProduct.searchProductsByName(searchQuery);
                request.setAttribute("productList", productList);
                logger.info("Retrieved {} products for search query: {}", productList.size(), searchQuery);
                getServletContext().getRequestDispatcher("/admin_home.jsp").forward(request, response);

            } else {
                logger.debug("Fetching all products");
                List<Products> productList = dbProduct.getAllProducts();
                request.setAttribute("productList", productList);
                logger.info("Retrieved {} products for product list", productList.size());
                getServletContext().getRequestDispatcher("/admin_home.jsp").forward(request, response);
            }

        } catch (Exception e) {
            logger.error("Error processing GET request with action: {}, productId: {}, searchQuery: {}",
                    action, productId, searchQuery, e);
            throw new ServletException("Error processing request", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String productId = request.getParameter("productId");
        String remoteIp = request.getRemoteAddr();

        logger.info("Received POST request from IP: {}, action: {}, productId: {}", remoteIp, action, productId);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to POST /Product, action: {}", remoteIp, action);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Beans.Users user = (Beans.Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access POST /Product, action: {}",
                    user.getRole(), user.getUsername(), action);
            response.sendRedirect("error.jsp?message=AccessDenied");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            DBProduct dbProduct = new DBProduct(conn);
            Products product = new Products();

            // Lấy thông tin từ form
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String size = request.getParameter("size");
            String stockStr = request.getParameter("stock");
            String categoryIdStr = request.getParameter("categoryId");
            String statusStr = request.getParameter("status");

            logger.debug("Processing product data: name={}, price={}, stock={}, categoryId={}",
                    name, priceStr, stockStr, categoryIdStr);

            // Kiểm tra giá trị đầu vào
            if (name == null || name.trim().isEmpty() || description == null || description.trim().isEmpty() ||
                    priceStr == null || stockStr == null || categoryIdStr == null || statusStr == null) {
                logger.warn("Missing required fields in form data from IP: {}", remoteIp);
                response.sendRedirect(action.equals("add") ? "addProduct.jsp?status=missing_fields" :
                        "editProduct.jsp?status=missing_fields&productId=" + productId);
                return;
            }

            try {
                int price = Integer.parseInt(priceStr);
                int stock = Integer.parseInt(stockStr);
                int categoryId = Integer.parseInt(categoryIdStr);

                // Kiểm tra giá và số lượng tồn kho âm
                if (price < 0 || stock < 0) {
                    logger.warn("Invalid price: {} or stock: {} from IP: {}", price, stock, remoteIp);
                    response.sendRedirect(action.equals("add") ? "addProduct.jsp?status=invalid_values" :
                            "editProduct.jsp?status=invalid_values&productId=" + productId);
                    return;
                }

                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setSize(size);
                product.setStock(stock);
                product.setCategoryId(categoryId);
                product.setStatus(Boolean.parseBoolean(statusStr));
            } catch (NumberFormatException e) {
                logger.warn("Invalid number format in form data: price={}, stock={}, categoryId={} from IP: {}",
                        priceStr, stockStr, categoryIdStr, remoteIp);
                response.sendRedirect(action.equals("add") ? "addProduct.jsp?status=invalid_format" :
                        "editProduct.jsp?status=invalid_format&productId=" + productId);
                return;
            }

            // Kiểm tra và gán thuộc tính ảnh
            Part imagePart = request.getPart("image");
            if (imagePart != null && imagePart.getSize() > 0) {
                logger.debug("Processing image upload for product");
                InputStream imageInputStream = imagePart.getInputStream();
                byte[] imageBytes = imageInputStream.readAllBytes();
                product.setImage(imageBytes);
                logger.info("Image uploaded successfully, size: {} bytes", imageBytes.length);
            } else if (action.equals("edit")) {
                // Giữ ảnh cũ nếu không tải ảnh mới khi chỉnh sửa
                Products existingProduct = dbProduct.getProductById(Integer.parseInt(productId));
                if (existingProduct != null) {
                    product.setImage(existingProduct.getImage());
                }
            }

            if (productId != null && !productId.isEmpty()) {
                try {
                    product.setProductId(Integer.parseInt(productId));
                } catch (NumberFormatException e) {
                    logger.warn("Invalid product ID format: {} from IP: {}", productId, remoteIp);
                    response.sendRedirect("editProduct.jsp?status=invalid_format&productId=" + productId);
                    return;
                }
            }

            if (action.equals("add")) {
                logger.info("Adding new product: {}", name);
                dbProduct.addProduct(product);
                logger.info("Successfully added product: {}", name);
                response.sendRedirect("Product?status=add_success");
            } else if (action.equals("edit")) {
                logger.info("Updating product ID: {}", productId);
                dbProduct.updateProduct(product);
                logger.info("Successfully updated product ID: {}", productId);
                response.sendRedirect("Product?status=edit_success");
            } else {
                logger.warn("Invalid action: {} from IP: {}", action, remoteIp);
                response.sendRedirect(action.equals("add") ? "addProduct.jsp?status=invalid_action" :
                        "editProduct.jsp?status=invalid_action&productId=" + productId);
                return;
            }

        } catch (Exception e) {
            logger.error("Error processing POST request with action: {}, productId: {} from IP: {}",
                    action, productId, remoteIp, e);
            response.sendRedirect(action.equals("add") ? "addProduct.jsp?status=error" :
                    "editProduct.jsp?status=error&productId=" + productId);
        }
    }
}