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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Beans.OrderItem;
import Beans.Orders;
import Beans.SQLServerConnection;
import Beans.Users;
import Utils.DBOrder;
import Utils.DBOrderDetail;

/**
 * Servlet implementation class CustomerOrderServlet
 */
@WebServlet("/CustomerOrder")
public class CustomerOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CustomerOrderServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Processing GET request for CustomerOrderServlet");
        String orderIdParam = request.getParameter("order_id");
        logger.debug("Order ID parameter: {}", orderIdParam);

        // Lấy thông tin user từ session
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user"); // Lấy đối tượng người dùng từ session
        if (user == null) {
            logger.warn("No user found in session");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in.");
            return;
        }
        int customerId = user.getUserId();  // Lấy userId từ đối tượng người dùng
        logger.debug("Customer ID: {}", customerId);

        try (Connection conn = SQLServerConnection.initializeDatabase()) {
            logger.info("Database connection established");
            if (orderIdParam != null) {
                // Nếu có order_id, lấy chi tiết đơn hàng
                int orderId = Integer.parseInt(orderIdParam);
                logger.info("Fetching order details for order ID: {}", orderId);
                DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId); // Lấy chi tiết đơn hàng
                logger.debug("Retrieved {} order items", orderItems.size());

                request.setAttribute("orderItems", orderItems);
                request.getRequestDispatcher("/customerOrderDetail.jsp").forward(request, response); // Chuyển đến trang chi tiết đơn hàng
                logger.info("Forwarded to customerOrderDetail.jsp");
            } else {
                // Nếu không có order_id, lấy danh sách đơn hàng của khách hàng
                logger.info("Fetching order list for customer ID: {}", customerId);
                DBOrder dbOrder = new DBOrder(conn);
                List<Orders> ordersList = dbOrder.getOrdersByCustomerId(customerId); // Lấy danh sách đơn hàng của khách hàng
                logger.debug("Retrieved {} orders", ordersList.size());

                request.setAttribute("ordersList", ordersList);
                request.getRequestDispatcher("/customerOrders.jsp").forward(request, response); // Chuyển đến trang danh sách đơn hàng
                logger.info("Forwarded to customerOrders.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Database error occurred while processing GET request", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Processing POST request for CustomerOrderServlet");
        String orderIdParam = request.getParameter("orderId"); // Sửa "order_id" thành "orderId"
        logger.debug("Order ID parameter: {}", orderIdParam);

        if (orderIdParam != null) {
            try (Connection conn = SQLServerConnection.initializeDatabase()) {
                logger.info("Database connection established");
                int orderId = Integer.parseInt(orderIdParam);
                logger.info("Attempting to mark order ID {} as paid", orderId);

                // Cập nhật trạng thái thanh toán của đơn hàng
                DBOrder dbOrder = new DBOrder(conn);
                boolean updated = dbOrder.markOrderAsPaid(orderId);

                if (updated) {
                    logger.info("Successfully marked order ID {} as paid", orderId);
                    // Thành công, chuyển đến trang danh sách đơn hàng
                    response.sendRedirect("CustomerOrder");
                } else {
                    logger.warn("Failed to update payment status for order ID {}", orderId);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update payment status.");
                }
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Database error occurred while processing POST request", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
            } catch (NumberFormatException e) {
                logger.error("Invalid order ID format: {}", orderIdParam, e);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID format.");
            }
        } else {
            logger.warn("Missing order ID in POST request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing order ID.");
        }
    }
}