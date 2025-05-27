package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Beans.OrderItem;
import Beans.Orders;
import Beans.SQLServerConnection;
import Utils.DBOrder;
import Utils.DBOrderDetail;

/**
 * Servlet implementation class OrderDetailServlet
 */
@WebServlet("/OrderDetail")
public class OrderDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailServlet.class);

    public OrderDetailServlet() {
        super();
        logger.debug("OrderDetailServlet initialized");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        logger.info("Processing GET request for OrderDetailServlet");
        String orderIdParam = request.getParameter("orderId");
        logger.debug("Order ID parameter: {}", orderIdParam);

        if (orderIdParam != null) {
            try {
                // Chuyển đổi orderId thành int
                int orderId = Integer.parseInt(orderIdParam);
                logger.info("Fetching details for order ID: {}", orderId);

                try (Connection conn = SQLServerConnection.initializeDatabase()) {
                    logger.info("Database connection established");

                    // Khởi tạo đối tượng DBOrder để lấy thông tin đơn hàng
                    DBOrder dbOrder = new DBOrder(conn);
                    Orders order = dbOrder.getOrderById(orderId);  // Lấy thông tin đơn hàng
                    logger.debug("Order retrieved: {}", order != null ? order : "null");

                    // Nếu tìm thấy đơn hàng, tiếp tục lấy chi tiết
                    if (order != null) {
                        // Lấy thông tin chi tiết đơn hàng
                        DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                        List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId);
                        logger.debug("Retrieved {} order items", orderItems != null ? orderItems.size() : 0);

                        if (orderItems != null && !orderItems.isEmpty()) {
                            // Thiết lập các thuộc tính cho JSP
                            request.setAttribute("orderItems", orderItems);
                            request.setAttribute("orderId", orderId);
                            request.setAttribute("customerName", order.getCustomerName());
                            request.setAttribute("totalAmount", order.getTotalAmount());
                            request.setAttribute("paymentStatus", order.isPaymentStatus());
                            request.setAttribute("deliveryStatus", order.getDeliveryStatus());
                            request.setAttribute("createdAt", order.getCreatedAt());
                            request.setAttribute("updatedAt", order.getUpdatedAt());
                            logger.info("Order details set for JSP rendering");
                        } else {
                            logger.warn("No order items found for order ID: {}", orderId);
                            request.setAttribute("error", "Không tìm thấy chi tiết đơn hàng.");
                        }
                    } else {
                        logger.warn("Order not found for ID: {}", orderId);
                        request.setAttribute("error", "Không tìm thấy đơn hàng với ID đã cho.");
                    }

                    // Chuyển hướng đến JSP để hiển thị chi tiết đơn hàng
                    logger.info("Forwarding to orderDetail.jsp");
                    request.getRequestDispatcher("/orderDetail.jsp").forward(request, response);
                } catch (SQLException | ClassNotFoundException e) {
                    logger.error("Database error while fetching order details for ID: {}", orderId, e);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid orderId format: {}", orderIdParam, e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId không hợp lệ");
            }
        } else {
            logger.warn("Missing orderId parameter in GET request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số orderId");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        logger.info("Processing POST request for OrderDetailServlet");
        logger.warn("POST method not supported");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method not supported for this operation.");
    }
}