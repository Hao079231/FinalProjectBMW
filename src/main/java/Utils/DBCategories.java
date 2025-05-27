package Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Beans.Categories;
import Beans.DBConnection;
public class DBCategories {
	// Hiển thị tất cả các loại sản phẩm
    public static List<Categories> getAllCategories() {
    List<Categories> categoriesList = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM Categories")) {

        while (rs.next()) {
            Categories category = new Categories(rs.getInt("category_id"), rs.getString("cname"));
            categoriesList.add(category);
        }

        // Log để kiểm tra kết quả
//        System.out.println("Categories List: " + categoriesList);

    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return categoriesList;
}


    // Thêm mới một loại sản phẩm
    public static boolean addCategory(Categories category) {
        String sql = "INSERT INTO Categories (cname) VALUES (?)"; // Không cần thêm category_id
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCname()); // Chỉ truyền cname
            return pstmt.executeUpdate() > 0; // Trả về true nếu thêm thành công

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Lấy loại sản phẩm theo mã ID
    public static Categories getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Categories(rs.getInt("category_id"), rs.getString("cname"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }

    // Sửa loại sản phẩm theo mã ID
    public static boolean updateCategory(Categories category) {
        String sql = "UPDATE Categories SET cname = ? WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getCname());
            pstmt.setInt(2, category.getCategoryId());
            return pstmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa loại sản phẩm theo mã ID
    public static boolean deleteCategoryById(int categoryId) {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate() > 0; // Trả về true nếu xóa thành công

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}