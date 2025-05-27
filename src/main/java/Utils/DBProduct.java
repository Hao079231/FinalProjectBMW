package Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Beans.DBConnection;
import Beans.Products;

public class DBProduct {

    private Connection connection;

    // Constructor to establish a connection to the database
    public DBProduct(Connection connection) {
        this.connection = connection;
    }

    // Method to get all products
 // Method to get all products
    public List<Products> getAllProducts() throws SQLException {
        List<Products> productList = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE status != 0";  // Sửa câu truy vấn

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
                productList.add(product);
            }
        }

        return productList;
    }

    // Method to get a product by ID
 // Method to get a product by ID
    public Products getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM Products WHERE product_id = ?";
        Products product = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = new Products(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("image"),  // Sử dụng rs.getBytes để lấy ảnh nhị phân
                            rs.getInt("price"),
                            rs.getString("size"),
                            rs.getInt("stock"),
                            rs.getInt("category_id"),
                            rs.getBoolean("status")
                    );
                }
            }
        }

        return product;
    }


    // Method to add a new product
    public boolean addProduct(Products product) throws SQLException {
        String query = "INSERT INTO Products (name, description, image, price, size, stock, category_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBytes(3, product.getImage());  // Sử dụng setBytes để lưu trữ ảnh nhị phân
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getSize());
            stmt.setInt(6, product.getStock());
            stmt.setInt(7, product.getCategoryId());
            stmt.setBoolean(8, product.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu thành công
        }
    }

    // Method to update a product
    public boolean updateProduct(Products product) throws SQLException {
        String query = "UPDATE Products SET name = ?, description = ?, image = ?, price = ?, size = ?, stock = ?, category_id = ?, status = ? " +
                "WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBytes(3, product.getImage());  // Sử dụng setBytes để cập nhật ảnh nhị phân
            stmt.setInt(4, product.getPrice());
            stmt.setString(5, product.getSize());
            stmt.setInt(6, product.getStock());
            stmt.setInt(7, product.getCategoryId());
            stmt.setBoolean(8, product.getStatus());
            stmt.setInt(9, product.getProductId()); // Đảm bảo không thay đổi ID

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    // Method to delete a product
    public boolean deleteProduct(int productId) throws SQLException {
        String checkOrderItemQuery = "SELECT COUNT(*) FROM OrderItems WHERE product_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkOrderItemQuery)) {
            checkStmt.setInt(1, productId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Nếu sản phẩm có trong OrderItems, chỉ cập nhật trạng thái
                    String updateStatusQuery = "UPDATE Products SET status = 0 WHERE product_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateStatusQuery)) {
                        updateStmt.setInt(1, productId);
                        int rowsAffected = updateStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                } else {
                    // Nếu sản phẩm không có trong OrderItems, xóa sản phẩm
                    String deleteQuery = "DELETE FROM Products WHERE product_id = ?";
                    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, productId);
                        int rowsAffected = deleteStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                }
            }
        }
    }

    // Method to search products by name
 // Method to search products by name
    public List<Products> searchProductsByName(String name) throws SQLException {
        List<Products> productList = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE name LIKE ?";  // Sử dụng % cho phép tìm kiếm từ khóa trong tên

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + name + "%");  // Đảm bảo rằng dấu % được thêm vào tên sản phẩm

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                    		rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getBytes("image"),  // Sử dụng rs.getBytes để lấy ảnh nhị phân
                            rs.getInt("price"),
                            rs.getString("size"),
                            rs.getInt("stock"),
                            rs.getInt("category_id"),
                            rs.getBoolean("status")
                    );
                    productList.add(product);
                }
            }
        }

        return productList;
    }

 // Method to find products by category ID
    public static ArrayList<Products> findByCategoryId(int categoryID) throws SQLException, ClassNotFoundException {
        ArrayList<Products> productList = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE category_id = ? AND status != 0"; 
        try (Connection connection = DBConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, categoryID);
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
                    productList.add(product);
                }
            }
        }
       
        return productList;
    }


}
