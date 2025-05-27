package Utils;

import Beans.Cart;
import Beans.CartItem;
import Beans.Products;
import Beans.DBConnection;

import java.sql.*;
import java.util.Map;

public class DBCart {

    // Phương thức hỗ trợ lấy tồn kho sản phẩm
    private static int getProductStock(int productId) throws SQLException, ClassNotFoundException {
        String query = "SELECT stock FROM Products WHERE product_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
            return 0; // Trả về 0 nếu không tìm thấy sản phẩm hoặc không có tồn kho
        }
    }

    // Thêm sản phẩm vào giỏ hàng
    public static void addItemToCart(int userId, int productId, int quantity, String feature) throws SQLException, ClassNotFoundException {
        // Kiểm tra tồn kho
        int stock = getProductStock(productId);
        if (quantity <= 0 || quantity > stock) {
            throw new SQLException("Số lượng không hợp lệ hoặc không đủ tồn kho cho sản phẩm ID: " + productId);
        }

        String checkQuery = "SELECT quantity FROM Cart WHERE user_id = ? AND product_id = ?";
        String updateQuery = "UPDATE Cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";
        String insertQuery = "INSERT INTO Cart (user_id, product_id, quantity, feature) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, productId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Nếu sản phẩm đã có, kiểm tra tổng số lượng so với tồn kho
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantity;
                if (newQuantity > stock) {
                    throw new SQLException("Tổng số lượng vượt quá tồn kho cho sản phẩm ID: " + productId);
                }
                // Cập nhật số lượng
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setInt(2, userId);
                    updateStmt.setInt(3, productId);
                    updateStmt.executeUpdate();
                }
            } else {
                // Thêm mới mục giỏ hàng
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, productId);
                    insertStmt.setInt(3, quantity);
                    insertStmt.setString(4, feature);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

    // Lấy tất cả mục trong giỏ hàng của người dùng
    public static Cart getCartByUserId(int userId) throws SQLException, ClassNotFoundException {
        Cart cart = new Cart();
        String sql = "SELECT c.cartID, c.user_id, c.product_id, c.quantity, c.feature, " +
                "p.name AS productName, p.price, p.image, p.stock " +
                "FROM Cart c JOIN Products p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int cartID = rs.getInt("cartID");
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                String feature = rs.getString("feature");
                String name = rs.getString("productName");
                int price = rs.getInt("price");
                byte[] image = rs.getBytes("image");
                int stock = rs.getInt("stock");
                // Tạo đối tượng Products
                Products product = new Products(productId, name, null, image, price, null, stock, 0, true);

                // Tạo đối tượng CartItem và thêm vào giỏ hàng
                CartItem item = new CartItem(productId, quantity, product);
                cart.addItem(item);
            }
        }
        return cart;
    }

    // Xóa một mục khỏi giỏ hàng
    public static void removeItemFromCart(int userId, int productId) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM Cart WHERE user_id = ? AND product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public static void updateItemQuantity(int userId, int productId, int quantity) throws SQLException, ClassNotFoundException {
        // Kiểm tra tồn kho
        int stock = getProductStock(productId);
        if (quantity <= 0 || quantity > stock) {
            throw new SQLException("Số lượng không hợp lệ hoặc không đủ tồn kho cho sản phẩm ID: " + productId);
        }

        String query = "UPDATE Cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    // Tính tổng giá giỏ hàng
    public static double calculateTotalPrice(Cart cart) {
        double total = 0.0;
        for (CartItem item : cart.getItems().values()) {
            total += item.getTotal(); // Sử dụng getTotal để tôn trọng giới hạn tồn kho
        }
        return total;
    }

    // Thêm đơn hàng
    public static int addOrder(int customerId, double totalAmount, String shippingAddress) throws SQLException, ClassNotFoundException {
        String insertOrderQuery = "INSERT INTO Orders (customer_id, total_amount, shipping_address, payment_status, delivery_status, created_at, updated_at) " +
                "VALUES (?, ?, ?, 0, 'confirmed', GETDATE(), GETDATE())";
        int orderId = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {

            insertStmt.setInt(1, customerId);
            insertStmt.setDouble(2, totalAmount);
            insertStmt.setString(3, shippingAddress);

            insertStmt.executeUpdate();

            // Lấy ID của đơn hàng vừa tạo
            ResultSet rs = insertStmt.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            rs.close();
        }
        return orderId;
    }

    // Thêm chi tiết đơn hàng và giảm tồn kho
    public static void addOrderItems(int orderId, Map<Integer, CartItem> cartItems) throws SQLException, ClassNotFoundException {
        String insertOrderItemQuery = "INSERT INTO OrderItems (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        String updateStockQuery = "UPDATE Products SET stock = stock - ? WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(insertOrderItemQuery);
             PreparedStatement updateStockStmt = connection.prepareStatement(updateStockQuery)) {

            for (CartItem item : cartItems.values()) {
                // Kiểm tra tồn kho trước khi thêm chi tiết đơn hàng
                int stock = getProductStock(item.getProductId());
                if (item.getQuantity() > stock) {
                    throw new SQLException("Không đủ tồn kho cho sản phẩm ID: " + item.getProductId());
                }

                // Thêm chi tiết đơn hàng
                insertStmt.setInt(1, orderId);
                insertStmt.setInt(2, item.getProductId());
                insertStmt.setInt(3, item.getQuantity());
                insertStmt.setDouble(4, item.getTotal()); // Sử dụng getTotal để tôn trọng giới hạn tồn kho
                insertStmt.addBatch();

                // Giảm tồn kho
                updateStockStmt.setInt(1, item.getQuantity());
                updateStockStmt.setInt(2, item.getProductId());
                updateStockStmt.addBatch();
            }

            // Thực thi các lệnh batch
            insertStmt.executeBatch();
            updateStockStmt.executeBatch();
        }
    }
}