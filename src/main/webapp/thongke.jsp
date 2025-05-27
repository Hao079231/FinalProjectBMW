<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Statistical" %>
<%@ page import="Beans.Users" %>
<%
    HttpSession sessionCheck = request.getSession(false);
    if (sessionCheck == null || sessionCheck.getAttribute("user") == null) {
        response.sendRedirect("signinup.jsp?status=login_required");
        return;
    }
    Users user = (Users) sessionCheck.getAttribute("user");
    if (!"admin".equals(user.getRole())) {
        response.sendRedirect("error.jsp?message=AccessDenied");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <title>Thống kê đơn hàng</title>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }
    </style>
</head>
<body class="bg-gray-100">
    <!-- Navbar -->
    <nav class="bg-white shadow-lg">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between">
                <div class="flex space-x-4">
                    <div>
                        <a class="flex items-center py-5 px-2 text-gray-700" href="#">
                            <i class="fas fa-store text-2xl"></i>
                            <span class="font-bold text-2xl ml-2">Jellycat</span>
                        </a>
                    </div>
                    <div class="hidden md:flex items-center space-x-1">
                        <a class="py-5 px-3 text-gray-700" href="Product">Products</a>
                        <a class="py-5 px-3 text-gray-700" href="Order">Orders</a>
                        <a class="py-5 px-3 text-gray-700" href="Customer">Customers</a>
                        <a class="py-5 px-3 text-gray-700" href="Statistical">Statistics</a>
                        <a class="py-5 px-3 text-gray-700" href="Category">Category</a>
                    </div>
                </div>
              <form action="SignInUp" method="post" class="hidden md:flex items-center space-x-1">
                  <input type="hidden" name="action" value="logout">
                  <button type="submit" class="py-2 px-3 bg-orange-500 text-white rounded-full hover:bg-orange-600 transition-colors">
                      Logout
                  </button>
              </form>
            </div>
        </div>
    </nav>
    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 py-6" id="main-content">
        <h1 class="text-3xl font-bold mb-6">Thống kê đơn hàng</h1>
        <form method="post" action="Statistical" class="mb-6">
            <div class="flex space-x-4">
                <div>
                    <label for="tuNgay" class="block text-gray-700">Từ ngày:</label>
                    <input type="date" name="tuNgay" id="tuNgay" required class="p-2 border border-gray-300 rounded-lg">
                </div>
                <div>
                    <label for="denNgay" class="block text-gray-700">Đến ngày:</label>
                    <input type="date" name="denNgay" id="denNgay" required class="p-2 border border-gray-300 rounded-lg">
                </div>
                <button type="submit" class="px-6 py-2 bg-blue-500 text-white rounded-lg">Thống kê</button>
            </div>
        </form>
        <!-- Table for displaying orders -->
        <div class="overflow-x-auto bg-white shadow-lg rounded-lg">
            <table class="min-w-full table-auto">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">ID</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Họ tên</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Địa chỉ</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Điện thoại</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Email</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Ngày đặt</th>
                        <th class="px-6 py-3 text-left text-sm font-medium text-gray-700">Tổng tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Statistical> danhSachThongKe = (List<Statistical>) request.getAttribute("danhSachThongKe");
                        long tongTienTatCa = 0;
                        if (danhSachThongKe != null && !danhSachThongKe.isEmpty()) {
                            for (Statistical thongKe : danhSachThongKe) {
                                tongTienTatCa += thongKe.getTongTien();
                    %>
                    <tr>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getId() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getHoTen() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getDiaChi() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getDienThoai() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getEmail() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getNgayDat() %></td>
                        <td class="px-6 py-3 text-sm text-gray-700"><%= thongKe.getTongTien() %></td>
                    </tr>
                    <%
                            }
                    %>
                    <tr class="bg-gray-100">
                        <td colspan="6" class="px-6 py-3 text-sm font-bold text-gray-700">Tổng cộng</td>
                        <td class="px-6 py-3 text-sm font-bold text-gray-700"><%= tongTienTatCa %></td>
                    </tr>
                    <%
                        } else {
                    %>
                    <tr>
                        <td colspan="7" class="px-6 py-3 text-sm text-gray-700 text-center">Không có dữ liệu thống kê.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>