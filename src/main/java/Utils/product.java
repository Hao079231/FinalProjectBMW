package Utils;

import Beans.Products;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class product {

    // Thêm sản phẩm
    public static void insert(Connection conn, Products product) {
        String query = "INSERT INTO Products (name, description, price, size, stock, category_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = conn.prepareStatement(query)) {
            stm.setString(1, product.getName());
            stm.setString(2, product.getDescription());
            stm.setInt(3, product.getPrice());
            stm.setString(4, product.getSize());
            stm.setInt(5, product.getStock());
            stm.setInt(6, product.getCategoryId());
            stm.setBoolean(7, product.getStatus());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lấy thông tin sản phẩm theo productId
    public static Products getProductById(Connection conn, int productId) {
        String sql = "SELECT * FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Products(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("image"),
                            rs.getInt("price"),
                            rs.getString("size"),
                            rs.getInt("stock"),
                            rs.getInt("category_id"),
                            rs.getBoolean("status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin sản phẩm theo productId: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy sản phẩm
    }

    // Lấy tất cả sản phẩm
    public static List<Products> getAllProducts(Connection conn) {
        List<Products> productsList = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Products product = new Products(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBytes("image"),
                        rs.getInt("price"),
                        rs.getString("size"),
                        rs.getInt("stock"),
                        rs.getInt("category_id"),
                        rs.getBoolean("status")
                );
                productsList.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
        return productsList;
    }

    // Cập nhật thông tin sản phẩm
    public static void updateProduct(Connection conn, Products product) {
        String sql = "UPDATE Products SET name = ?, description = ?, price = ?, size = ?, stock = ?, category_id = ?, status = ? WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setInt(3, product.getPrice());
            stmt.setString(4, product.getSize());
            stmt.setInt(5, product.getStock());
            stmt.setInt(6, product.getCategoryId());
            stmt.setBoolean(7, product.getStatus());
            stmt.setInt(8, product.getProductId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Xóa sản phẩm
    public static void deleteProduct(Connection conn, int productId) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Lấy tất cả sản phẩm theo danh mục (categoryId)
    public static List<Products> getProductsByCategoryId(Connection conn, int categoryId) {
        List<Products> productsList = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE category_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("image"),
                            rs.getInt("price"),
                            rs.getString("size"),
                            rs.getInt("stock"),
                            rs.getInt("category_id"),
                            rs.getBoolean("status")
                    );
                    productsList.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sản phẩm theo danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return productsList;
    }
}
