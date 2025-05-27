package Utils;

import Beans.Review;
import Beans.SQLServerConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BDReview {
	private Connection connection;

    // Constructor to establish a connection to the database

    public BDReview(Connection conn) {
        this.connection = conn;
    }
    // Phương thức để kiểm tra sản phẩm đã thanh toán
    public boolean isProductPaid(int productId, int userId, int orderId) throws SQLException {
        String query = "SELECT payment_status FROM Orders o " +
                       "JOIN OrderItems oi ON o.order_id = oi.order_id " +
                       "WHERE oi.product_id = ? AND o.customer_id = ? AND o.order_id = ? AND o.payment_status = 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, userId);
            stmt.setInt(3, orderId);  // Thêm orderId vào điều kiện
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean addReview(Review review) {
        String sql = "INSERT INTO ProductReviews (product_id, user_id, rating, comment, order_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getProductId());
            stmt.setInt(2, review.getUserId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setInt(5, review.getOrderId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Lấy danh sách tất cả các review
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM ProductReviews";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Lấy review theo productId
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT pr.*, u.username " +
                       "FROM ProductReviews pr " +
                       "JOIN Users u ON pr.user_id = u.user_id " +
                       "WHERE pr.product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    // Kiểm tra xem sản phẩm đã được đánh giá chưa
    public boolean hasUserReviewedProduct(int productId, int userId, int orderId) {
        String query = "SELECT COUNT(*) AS review_count FROM ProductReviews pr " +
                       "WHERE pr.product_id = ? AND pr.user_id = ? AND pr.order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId); // Ràng buộc product_id
            stmt.setInt(2, userId);    // Ràng buộc user_id
            stmt.setInt(3, orderId);   // Ràng buộc order_id
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("review_count"); // Lấy số lượng đánh giá
                    System.out.println("Review count: " + count); // Debug output
                    return count > 0; // Nếu lớn hơn 0 thì đã được đánh giá
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return false; // Mặc định trả về false nếu xảy ra lỗi hoặc không có kết quả
    }


    // Helper method để chuyển ResultSet thành Review
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        int reviewId = rs.getInt("review_id");
        int productId = rs.getInt("product_id");
        int userId = rs.getInt("user_id");
        int rating = rs.getInt("rating");
        String comment = rs.getString("comment");
        String username = rs.getString("username"); // Lấy tên người dùng
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new Review(reviewId, productId, userId, username, rating, comment, createdAt);
    }
}