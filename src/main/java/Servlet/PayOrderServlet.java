package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Beans.Cart;
import Beans.CartItem;
import Beans.Orders;
import Beans.SQLServerConnection;
import Beans.Users;
import Utils.DBCart;
import Utils.DBOrder;

/**
 * Servlet implementation class PayOrderServlet
 */
@WebServlet("/PayOrder")
public class PayOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PayOrderServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Processing POST request for PayOrderServlet");
        String action = request.getParameter("action");
        logger.debug("Action parameter: {}", action);

        if ("submit".equals(action)) {
            logger.info("Handling order submission");
            handleSubmit(request, response);
        } else {
            logger.warn("Invalid action received: {}", action);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Yêu cầu không hợp lệ.");
        }
    }

    private void handleSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Processing handleSubmit for order placement");
        HttpSession session = request.getSession();

        // Lấy thông tin người dùng từ session
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            logger.warn("No user found in session, redirecting to login");
            session.setAttribute("error", "Bạn cần đăng nhập để thực hiện thanh toán.");
            response.sendRedirect("signinup.jsp");
            return;
        }
        logger.debug("User ID: {}", user.getUserId());

        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            logger.warn("Cart is empty or null, redirecting to cart page");
            session.setAttribute("error", "Giỏ hàng của bạn hiện tại trống.");
            response.sendRedirect("cart.jsp");
            return;
        }
        logger.debug("Cart contains {} items", cart.getItems().size());

        // Lấy địa chỉ giao hàng từ yêu cầu
        String shippingAddress = request.getParameter("shippingAddress");
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            logger.warn("Missing or empty shipping address, redirecting to checkout");
            session.setAttribute("error", "Vui lòng nhập địa chỉ giao hàng.");
            response.sendRedirect("checkout.jsp");
            return;
        }
        logger.debug("Shipping address: {}", shippingAddress);

        try {
            logger.info("Adding order to database");
            // Thêm đơn hàng và chi tiết đơn hàng vào cơ sở dữ liệu
            double totalAmount = DBCart.calculateTotalPrice(cart);
            logger.debug("Total amount calculated: {}", totalAmount);
            int orderId = DBCart.addOrder(user.getUserId(), totalAmount, shippingAddress);
            logger.info("Order created with ID: {}", orderId);
            DBCart.addOrderItems(orderId, cart.getItems());
            logger.debug("Order items added for order ID: {}", orderId);

            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");
            logger.info("Cart cleared from session");

            // Chuyển hướng đến trang thông báo thành công
            session.setAttribute("message", "Đặt hàng thành công!");
            logger.info("Order placed successfully, redirecting to CustomerOrder");
            response.sendRedirect("CustomerOrder");

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Database error while placing order", e);
            session.setAttribute("error", "Đặt hàng không thành công. Vui lòng thử lại.");
            response.sendRedirect("checkout.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Processing GET request for PayOrderServlet, delegating to doPost");
        doPost(request, response);
    }
}