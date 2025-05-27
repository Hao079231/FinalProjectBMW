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

import Beans.Users;
import Beans.SQLServerConnection;
import Utils.DBUsers;

@WebServlet("/Customer")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CustomerServlet.class);

    public CustomerServlet() {
        super();
        logger.debug("CustomerServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info("Processing GET request for CustomerServlet from IP: {}", remoteIp);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to GET /Customer", remoteIp);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Users user = (Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access GET /Customer",
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
            DBUsers dbUsers = new DBUsers(); // No Connection parameter needed for constructor

            List<Users> usersList;

            if (searchQuery != null && !searchQuery.isEmpty()) {
                logger.info("Searching users by query: {}", searchQuery);
                usersList = dbUsers.searchUsersByUsername(conn, searchQuery); // Updated method name and added conn
            } else {
                logger.info("Fetching all users");
                usersList = dbUsers.getAllUsers(conn); // Added conn parameter
            }

            logger.debug("Retrieved {} users", usersList != null ? usersList.size() : 0);
            request.setAttribute("usersList", usersList);
            logger.info("Forwarding to customerList.jsp");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/customerList.jsp");
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
        logger.info("Processing POST request for CustomerServlet from IP: {}", remoteIp);

        // Kiểm tra quyền truy cập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Unauthorized access attempt from IP: {} to POST /Customer", remoteIp);
            response.sendRedirect("signinup.jsp?status=login_required");
            return;
        }
        Users user = (Users) session.getAttribute("user");
        if (!"admin".equals(user.getRole())) {
            logger.warn("Non-admin user (role: {}, username: {}) attempted to access POST /Customer",
                    user.getRole(), user.getUsername());
            response.sendRedirect("error.jsp?message=AccessDenied");
            return;
        }

        logger.info("Delegating POST request to doGet");
        doGet(request, response);
    }
}