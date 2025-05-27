package Servlet;

import Beans.DBConnection;
import Beans.Users;
import Utils.DBUsers;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/User")
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    public UserServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String setting = req.getParameter("setting");
        logger.info("Received GET request with setting parameter: {}", setting != null ? setting : "none");

        if (setting != null) {
            logger.debug("Forwarding to editProfile.jsp");
            req.getRequestDispatcher("/editProfile.jsp").forward(req, resp);
        } else {
            logger.debug("Forwarding to profile.jsp");
            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        logger.info("Received POST request with action: {}", action != null ? action : "none");

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            logger.warn("No user found in session for action: {}", action);
            response.sendRedirect("login.jsp");
            return;
        }

        logger.debug("Processing user update for user ID: {}", user.getUserId());

        // Lấy thông tin người dùng từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Tạo đối tượng user
        Users users = new Users(user.getUserId(), username, user.getPassword(), email, phone, address, user.getRole());

        try (Connection conn = DBConnection.getConnection()) {
            if ("update".equals(action)) {
                logger.debug("Updating user information: username={}, email={}, phone={}, address={}",
                    username, email, phone, address);
                DBUsers.updateUser(conn, users);
                request.getSession().setAttribute("user", users);
                logger.info("Successfully updated user ID: {}", user.getUserId());
                response.sendRedirect("profile.jsp");
            } else {
                logger.warn("Invalid action: {}", action);
                request.setAttribute("errorMessage", "Invalid action.");
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error processing user update for user ID: {}, action: {}", user.getUserId(), action, e);
            request.setAttribute("errorMessage", "Error updating user profile: " + e.getMessage());
            response.sendRedirect("error.jsp");
        }
    }
}