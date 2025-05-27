package Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import Beans.Products;
import Beans.DBConnection;
import Utils.DBProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/Cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);

    public CartServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info("GET request received from IP: {}, action: {}", remoteIp, request.getParameter("action"));

        // Khai báo HttpSession
        HttpSession session = request.getSession();

        try (Connection connection = DBConnection.getConnection()) {
            DBProduct dbProduct = new DBProduct(connection);
            String action = request.getParameter("action");
            String productIdStr = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");

            if (action == null || productIdStr == null) {
                logger.warn("Yêu cầu không hợp lệ từ IP: {}, action: {}, productId: {}", remoteIp, action, productIdStr);
                session.setAttribute("error", "Yêu cầu không hợp lệ.");
                response.sendRedirect("cart.jsp");
                return;
            }

            Beans.Cart cart = (Beans.Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Beans.Cart();
                session.setAttribute("cart", cart);
                logger.info("Tạo giỏ hàng mới cho session: {}, IP: {}", session.getId(), remoteIp);
            } else {
                cart.cleanCart(); // Làm sạch dữ liệu không hợp lệ
            }

            try {
                int productId = Integer.parseInt(productIdStr);
                Products product = dbProduct.getProductById(productId);

                if (product == null || !product.getStatus()) {
                    logger.warn("Sản phẩm không tồn tại hoặc không khả dụng cho ID: {}, IP: {}", productId, remoteIp);
                    session.setAttribute("error", "Sản phẩm không tồn tại hoặc không khả dụng.");
                    response.sendRedirect("cart.jsp");
                    return;
                }

                switch (action) {
                    case "add":
                        int addQuantity = (quantityStr != null) ? Integer.parseInt(quantityStr) : 1;
                        if (addQuantity >= 1 && addQuantity <= product.getStock()) {
                            cart.addItem(new Beans.CartItem(productId, addQuantity, product));
                            logger.info("Đã thêm sản phẩm ID: {} với số lượng: {} vào giỏ hàng, IP: {}", productId, addQuantity, remoteIp);
                            session.setAttribute("message", "Đã thêm sản phẩm vào giỏ hàng.");
                        } else {
                            logger.warn("Số lượng không hợp lệ: {} cho sản phẩm ID: {}, tồn kho: {}, IP: {}", addQuantity, productId, product.getStock(), remoteIp);
                            session.setAttribute("error", "Số lượng không hợp lệ hoặc vượt quá tồn kho.");
                            response.sendRedirect("cart.jsp");
                            return;
                        }
                        break;

                    case "edit":
                        int updatedQuantity = Integer.parseInt(quantityStr);
                        if (updatedQuantity >= 1 && updatedQuantity <= product.getStock()) {
                            cart.updateItemQuantity(productId, updatedQuantity);
                            logger.info("Đã cập nhật sản phẩm ID: {} với số lượng: {} trong giỏ hàng, IP: {}", productId, updatedQuantity, remoteIp);
                            session.setAttribute("message", "Đã cập nhật số lượng sản phẩm.");
                        } else {
                            logger.warn("Số lượng không hợp lệ: {} cho sản phẩm ID: {}, tồn kho: {}, IP: {}", updatedQuantity, productId, product.getStock(), remoteIp);
                            session.setAttribute("error", "Số lượng không hợp lệ hoặc vượt quá tồn kho.");
                            response.sendRedirect("cart.jsp");
                            return;
                        }
                        break;

                    case "remove":
                        cart.removeItem(productId);
                        logger.info("Đã xóa sản phẩm ID: {} khỏi giỏ hàng, IP: {}", productId, remoteIp);
                        session.setAttribute("message", "Đã xóa sản phẩm khỏi giỏ hàng.");
                        break;

                    default:
                        logger.warn("Hành động không hợp lệ: {} từ IP: {}", action, remoteIp);
                        session.setAttribute("error", "Hành động không hợp lệ.");
                        response.sendRedirect("cart.jsp");
                        return;
                }

                session.setAttribute("cart", cart);
            } catch (SQLException e) {
                logger.error("Lỗi cơ sở dữ liệu khi xử lý hành động giỏ hàng: {}, productId: {}, IP: {}, lỗi: {}", action, productIdStr, remoteIp, e.getMessage());
                session.setAttribute("error", "Lỗi cơ sở dữ liệu.");
                response.sendRedirect("error.jsp");
            } catch (NumberFormatException e) {
                logger.error("Định dạng số không hợp lệ cho productId: {} hoặc quantity: {}, IP: {}, lỗi: {}", productIdStr, quantityStr, remoteIp, e.getMessage());
                session.setAttribute("error", "Dữ liệu không hợp lệ.");
                response.sendRedirect("cart.jsp");
            } catch (IllegalArgumentException e) {
                logger.warn("Lỗi khi xử lý giỏ hàng: {}, productId: {}, IP: {}, lỗi: {}", action, productIdStr, remoteIp, e.getMessage());
                session.setAttribute("error", e.getMessage());
                response.sendRedirect("cart.jsp");
            }

            response.sendRedirect("cart.jsp");

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Không thể kết nối cơ sở dữ liệu từ IP: {}, lỗi: {}", remoteIp, e.getMessage());
            session.setAttribute("error", "Lỗi kết nối cơ sở dữ liệu.");
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info("POST request received from IP: {}, action: {}", remoteIp, request.getParameter("action"));
        doGet(request, response);
    }
}