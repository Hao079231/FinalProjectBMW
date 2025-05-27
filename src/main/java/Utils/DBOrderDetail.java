package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Beans.OrderItem;

public class DBOrderDetail {
	private Connection conn;

    public DBOrderDetail(Connection conn) {
        this.conn = conn;
    }

    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
    	String sql = "SELECT oi.*, p.name AS product_name, p.image FROM OrderItems oi " +
                "JOIN Products p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = ?";
        List<OrderItem> orderItems = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("product_name"),
                        rs.getBytes("image")// Thêm tên sản phẩm vào đây
                    );
                    orderItems.add(item);
                }
            }
        }

        return orderItems;
    }


}

