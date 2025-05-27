package Utils;

import Beans.Orders;
import Beans.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBOrder {

    private Connection conn;

    public DBOrder(Connection connection) {
        this.conn = connection;
    }

    public Orders getOrderById(int orderId) throws SQLException {
        Orders order = null;
        String sql = "SELECT o.order_id, u.username AS customer_name, o.total_amount, o.payment_status, o.delivery_status, o.created_at, o.updated_at " +
                     "FROM Orders o " +
                     "JOIN Users u ON o.customer_id = u.user_id " +
                     "WHERE o.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);  // Gán giá trị orderId vào câu truy vấn
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    order = new Orders(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"), // Lấy tên khách hàng
                        rs.getDouble("total_amount"),
                        rs.getBoolean("payment_status"),
                        rs.getString("delivery_status"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                    );
                }
            }
        }
        return order;  // Trả về đối tượng đơn hàng hoặc null nếu không tìm thấy
    }

 // Lấy tất cả các đơn hàng với tên khách hàng
    public List<Orders> getAllOrders() throws SQLException {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.order_id, u.username AS customer_name, o.total_amount, o.payment_status, o.delivery_status, o.created_at, o.updated_at " +
                     "FROM Orders o " +
                     "JOIN Users u ON o.customer_id = u.user_id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Orders order = new Orders(
                    rs.getInt("order_id"),
                    rs.getString("customer_name"), // Lấy tên khách hàng thay vì customerId
                    rs.getDouble("total_amount"),
                    rs.getBoolean("payment_status"),
                    rs.getString("delivery_status"),
                    rs.getDate("created_at"),
                    rs.getDate("updated_at")
                );
                ordersList.add(order);
            }
        }
        return ordersList;
    }
    public List<Orders> getOrdersByCustomerId(int customerId) throws SQLException {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.order_id, u.username AS customer_name, o.total_amount, o.payment_status, o.delivery_status, o.created_at, o.updated_at " +
                     "FROM Orders o " +
                     "JOIN Users u ON o.customer_id = u.user_id " +
                     "WHERE o.customer_id = ?";  // Tìm theo customer_id

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);  // Gán giá trị customerId vào câu truy vấn
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Orders order = new Orders(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),  // Lấy tên khách hàng
                        rs.getDouble("total_amount"),
                        rs.getBoolean("payment_status"),
                        rs.getString("delivery_status"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                    );
                    ordersList.add(order);
                }
            }
        }
        return ordersList;
    }
    // Tìm kiếm đơn hàng theo tên khách hàng
    public List<Orders> searchOrders(String customerName) throws SQLException {
        List<Orders> ordersList = new ArrayList<>();
        String sql = "SELECT o.order_id, u.username AS customer_name, o.total_amount, o.payment_status, o.delivery_status, o.created_at, o.updated_at " +
                     "FROM Orders o " +
                     "JOIN Users u ON o.customer_id = u.user_id " +
                     "WHERE u.username LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + customerName + "%");  // Tìm kiếm tên khách hàng
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Orders order = new Orders(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"), // Tên khách hàng
                        rs.getDouble("total_amount"),
                        rs.getBoolean("payment_status"),
                        rs.getString("delivery_status"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")
                    );
                    ordersList.add(order);
                }
            }
        }
        return ordersList;
    }
    public boolean markOrderAsPaid(int orderId) throws SQLException {
        String sql = "UPDATE Orders SET payment_status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, true);  // Đặt trạng thái thanh toán thành true (đã thanh toán)
            stmt.setInt(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu đã cập nhật thành công, ngược lại là false
        }
    }

}
