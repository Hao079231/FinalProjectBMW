package Servlet;

import jakarta.servlet.RequestDispatcher;
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

import Beans.Orders;
import Beans.Users;
import Beans.SQLServerConnection;
import Utils.DBOrder;

@WebServlet("/Order")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);

    public OrderServlet() {
        super();
        logger.debug("OrderServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info("Processing GET request for OrderServlet from IP: {}", remoteIp);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to GET /Order", remoteIp);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Users user = (Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access GET /Order",
                    user.getRole(), user.getUsername());
            response.sendRedirect("error.jsp?message=AccessDenied");
            return;
        }

        String searchQuery = request.getParameter("search");
        logger.debug("Search query: {}", searchQuery);

        Connection conn = null;
        try {
            logger.info("Establishing database connection");
            conn = SQLServerConnection.initializeDatabase();
            DBOrder dbOrder = new DBOrder(conn);

            List<Orders> ordersList;

            if (searchQuery != null && !searchQuery.isEmpty()) {
                logger.info("Searching orders by query: {}", searchQuery);
                ordersList = dbOrder.searchOrders(searchQuery);
            } else {
                logger.info("Fetching all orders");
                ordersList = dbOrder.getAllOrders();
            }

            logger.debug("Retrieved {} orders", ordersList != null ? ordersList.size() : 0);
            request.setAttribute("ordersList", ordersList);
            logger.info("Forwarding to ordersList.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/ordersList.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Database error while processing GET request from IP: {}", remoteIp, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error while processing GET request from IP: {}", remoteIp, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    logger.debug("Database connection closed");
                } catch (SQLException e) {
                    logger.error("Error closing database connection", e);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info("Processing POST request for OrderServlet from IP: {}", remoteIp);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to POST /Order", remoteIp);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Users user = (Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access POST /Order",
                    user.getRole(), user.getUsername());
            response.sendRedirect("error.jsp?message=AccessDenied");
            return;
        }

        logger.info("Delegating POST request to doGet");
        doGet(request, response);
    }
}