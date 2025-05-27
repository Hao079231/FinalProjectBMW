package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Beans.SQLServerConnection;
import Beans.Statistical;

public class DBStatistical {
	public ArrayList<Statistical> getThongKe(String tuNgay, String denNgay) {
	    ArrayList<Statistical> lst = new ArrayList<>();
	    try (Connection conn = SQLServerConnection.initializeDatabase()) {  // Tạo kết nối từ SQLServerConnection
	        String sql = "SELECT o.order_id AS id, u.username AS hoTen, u.address AS diaChi, u.phone AS dienThoai, "
	                   + "u.email, FORMAT(o.created_at, 'yyyy-MM-dd') AS ngayDat, "
	                   + "SUM(oi.quantity * oi.price) AS tongTien "
	                   + "FROM Orders o "
	                   + "JOIN Users u ON o.customer_id = u.user_id "
	                   + "JOIN OrderItems oi ON o.order_id = oi.order_id "
	                   + "WHERE o.created_at BETWEEN ? AND ? "
	                   + "AND o.payment_status = 1 "  // Thêm điều kiện lấy đơn đã thanh toán
	                   + "GROUP BY o.order_id, u.username, u.address, u.phone, u.email, o.created_at "
	                   + "ORDER BY o.created_at";
	        PreparedStatement cmd = conn.prepareStatement(sql);
	        cmd.setString(1, tuNgay);
	        cmd.setString(2, denNgay);
	        ResultSet rs = cmd.executeQuery();
	        
	        while (rs.next()) {
	            Statistical thongKe = new Statistical();
	            thongKe.setId(rs.getLong("id"));
	            thongKe.setHoTen(rs.getString("hoTen"));
	            thongKe.setDiaChi(rs.getString("diaChi"));
	            thongKe.setDienThoai(rs.getString("dienThoai"));
	            thongKe.setEmail(rs.getString("email"));
	            thongKe.setNgayDat(rs.getString("ngayDat"));
	            thongKe.setTongTien(rs.getLong("tongTien"));
	            lst.add(thongKe);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lst;
	}
}

