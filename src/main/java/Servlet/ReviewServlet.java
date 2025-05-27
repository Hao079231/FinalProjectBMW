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

import Beans.OrderItem;
import Beans.Review;
import Beans.SQLServerConnection;
import Beans.Users;
import Utils.BDReview;
import Utils.DBOrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class ReviewServlet
 */
@WebServlet("/Review")
public class ReviewServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "check";  // Mặc định là "check"
        }

        logger.info("Received POST request with action: {}", action);

        switch (action) {
            case "check":
                handleCheck(request, response);
                break;
            case "submit":
                handleSubmit(request, response);
                break;
            default:
                logger.warn("Invalid action received: {}", action);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Yêu cầu không hợp lệ");
                break;
        }
    }

    private void handleCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            logger.warn("No user found in session for review check");
            response.sendRedirect("login.jsp");
            return;
        }

        logger.debug("Checking review for user ID: {}, product ID: {}, order ID: {}",
                user.getUserId(), request.getParameter("productId"), request.getParameter("orderId"));

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            try (Connection conn = SQLServerConnection.initializeDatabase()) {
                BDReview bdReview = new BDReview(conn);

                if (!bdReview.isProductPaid(productId, user.getUserId(), orderId)) {
                    logger.info("Product ID: {} not paid for user ID: {}, order ID: {}", productId, user.getUserId(), orderId);
                    request.setAttribute("error", "Sản phẩm này chưa được thanh toán, không thể đánh giá.");
                    DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                    List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId);
                    request.setAttribute("orderItems", orderItems);
                    request.getRequestDispatcher("customerOrderDetail.jsp").forward(request, response);
                    return;
                }

                if (bdReview.hasUserReviewedProduct(productId, user.getUserId(), orderId)) {
                    logger.info("Product ID: {} already reviewed by user ID: {}, order ID: {}", productId, user.getUserId(), orderId);
                    request.setAttribute("error", "Sản phẩm này đã được đánh giá.");
                    DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                    List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId);
                    request.setAttribute("orderItems", orderItems);
                    request.getRequestDispatcher("customerOrderDetail.jsp").forward(request, response);
                } else {
                    logger.debug("No existing review found, forwarding to Review.jsp for product ID: {}, order ID: {}", productId, orderId);
                    request.setAttribute("productId", productId);
                    request.setAttribute("orderId", orderId);
                    request.getRequestDispatcher("Review.jsp").forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Error checking review status for product ID: {}, user ID: {}, order ID: {}",
                        productId, user.getUserId(), orderId, e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid productId or orderId format: productId={}, orderId={}",
                    request.getParameter("productId"), request.getParameter("orderId"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm hoặc đơn hàng không hợp lệ.");
        }
    }

    private void handleSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            logger.warn("No user found in session for review submission");
            response.sendRedirect("login.jsp");
            return;
        }

        logger.info("Processing review submission for user ID: {}, product ID: {}, order ID: {}",
                user.getUserId(), request.getParameter("productId"), request.getParameter("orderId"));

        try {
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            int productId = Integer.parseInt(request.getParameter("productId"));
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            // Kiểm tra payload nguy hiểm trong comment
            if (comment != null && (comment.toLowerCase().contains("<script") || comment.contains("onerror") || comment.contains("javascript:"))) {
                logger.warn("Dangerous input detected in comment: {}", comment);
                request.setAttribute("error", "Nhập liệu chứa mã nguy hiểm!"); // Rút ngắn thông báo
                try (Connection conn = SQLServerConnection.initializeDatabase()) {
                    DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                    List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId);
                    request.setAttribute("orderItems", orderItems);
                } catch (SQLException | ClassNotFoundException e) {
                    logger.error("Error fetching order items: {}", e.getMessage());
                }
                request.getRequestDispatcher("customerOrderDetail.jsp").forward(request, response);
                return;
            }

            // Lọc các ký tự nguy hiểm trong comment
            if (comment != null) {
                comment = comment.replaceAll("[<>\"';()]+", "");
                comment = comment.replaceAll("(?:^|\\s)javascript:", "");
            }

            try (Connection conn = SQLServerConnection.initializeDatabase()) {
                BDReview bdReview = new BDReview(conn);

                // Tạo Review mới và thêm vào cơ sở dữ liệu
                Review newReview = new Review(productId, user.getUserId(), orderId, rating, comment);
                logger.debug("Submitting review: rating={}, comment={}, productId={}, orderId={}",
                        rating, comment, productId, orderId);
                boolean success = bdReview.addReview(newReview);

                DBOrderDetail dbOrderDetail = new DBOrderDetail(conn);
                List<OrderItem> orderItems = dbOrderDetail.getOrderItems(orderId);
                request.setAttribute("orderItems", orderItems);

                if (success) {
                    logger.info("Review added successfully for product ID: {}, user ID: {}, order ID: {}",
                            productId, user.getUserId(), orderId);
                    request.setAttribute("message", "Đánh giá đã được thêm thành công.");
                } else {
                    logger.warn("Failed to add review for product ID: {}, user ID: {}, order ID: {}",
                            productId, user.getUserId(), orderId);
                    request.setAttribute("error", "Không thể thêm đánh giá. Vui lòng thử lại.");
                }

                request.getRequestDispatcher("customerOrderDetail.jsp").forward(request, response);
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Error submitting review for product ID: {}, user ID: {}, order ID: {}",
                        productId, user.getUserId(), orderId, e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid input format: rating={}, productId={}, orderId={}",
                    request.getParameter("rating"), request.getParameter("productId"), request.getParameter("orderId"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu đánh giá không hợp lệ.");
        }
    }
}