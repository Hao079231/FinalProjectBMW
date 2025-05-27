<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Orders" %>
<%@ page import="Beans.OrderItem"%>
<%@ page import="Utils.DBOrder" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <title>Order Details</title>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }
        .hidden {
            display: none;
        }
    </style>
</head>
<body class="bg-gray-100">
    <!-- Navbar -->
    <nav class="bg-white shadow-lg">
        <div class="max-w-7xl mx-auto px-4">
            <div class="flex justify-between">
                <div class="flex space-x-4">
                    <!-- Logo -->
                    <div>
                        <a class="flex items-center py-5 px-2 text-gray-700" href="#">
                            <i class="fas fa-store text-2xl"></i>
                            <span class="font-bold text-2xl ml-2">Jellycat</span>
                        </a>
                    </div>
                    <!-- Primary Nav -->
                    <div class="hidden md:flex items-center space-x-1">
                        <a class="py-5 px-3 text-gray-700" href="Product" id="nav-products">Products</a>
                        <a class="py-5 px-3 text-gray-700" href="OrderServlet" id="nav-orders">Orders</a>
                        <a class="py-5 px-3 text-gray-700" href="CustomerServlet" id="nav-customers">Customers</a>
                        <a class="py-5 px-3 text-gray-700" href="Statistical" id="nav-statistics">Statistics</a>
                        <a class="py-5 px-3 text-gray-700" href="Category" id="nav-category">Category</a>
                    </div>
                </div>
                <!-- Secondary Nav -->
              <form action="SignInUp" method="post" class="hidden md:flex items-center space-x-1">
                  <input type="hidden" name="action" value="logout">
                  <button type="submit" class="py-2 px-3 bg-orange-500 text-white rounded-full hover:bg-orange-600 transition-colors">
                      Logout
                  </button>
              </form>
                <!-- Mobile Button -->
                <div class="md:hidden flex items-center">
                    <button class="mobile-menu-button">
                        <i class="fas fa-bars"></i>
                    </button>
                </div>
            </div>
        </div>
    </nav>
    <!-- Mobile Menu -->
    <div class="mobile-menu hidden md:hidden">
        <a class="block py-2 px-4 text-sm" href="ProductServlet" id="mobile-nav-products">Products</a>
        <a class="block py-2 px-4 text-sm" href="OrderServlet" id="mobile-nav-orders">Orders</a>
        <a class="block py-2 px-4 text-sm" href="CustomerServlet" id="mobile-nav-customers">Customers</a>
        <a class="block py-2 px-4 text-sm" href="Statistical" id="mobile-nav-statistics">Statistics</a>
        <a class="block py-2 px-4 text-sm" href="Category" id="mobile-nav-category">Category</a>
    </div>
    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 py-6" id="main-content">
        <!-- Order Details Section -->
        <h1 class="text-3xl font-bold mb-6">Chi tiết đơn hàng</h1>
        <div class="bg-white p-6 rounded-lg shadow-lg">
            <h2 class="text-xl font-bold mb-2">Mã đơn hàng: <%= request.getAttribute("orderId") %></h2>
            <p class="text-gray-700 mb-4">Tên khách hàng: <%= request.getAttribute("customerName") %></p>
            <p class="text-gray-700 mb-4">Tổng tiền: <%= request.getAttribute("totalAmount") %></p>
            <p class="text-gray-700 mb-4">Trạng thái thanh toán: <%= (request.getAttribute("paymentStatus") != null && (Boolean) request.getAttribute("paymentStatus")) ? "Đã thanh toán" : "Chưa thanh toán" %></p>
            <p class="text-gray-700 mb-4">Trạng thái giao hàng: <%= request.getAttribute("deliveryStatus") %></p>
            <p class="text-gray-700 mb-4">Ngày tạo: <%= request.getAttribute("createdAt") %></p>
            <p class="text-gray-700 mb-4">Ngày cập nhật: <%= request.getAttribute("updatedAt") %></p>
        </div>

        <h2 class="text-2xl font-bold mt-6 mb-4">Danh sách sản phẩm</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="order-item-list">
            <%
                // Lấy danh sách sản phẩm trong đơn hàng
                List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
                if (orderItems != null) {
                    for (OrderItem item : orderItems) {
            %>
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                        	<img alt="Product Image" class="w-full h-40 object-cover mb-4 rounded"
                                 src="data:image/jpeg;base64,<%= new String(java.util.Base64.getEncoder().encode(item.getImage())) %>" />
                            <h3 class="text-xl font-bold mb-2">Sản phẩm: <%= item.getProductName() %></h3>
                            <p class="text-gray-700 mb-4">Số lượng: <%= item.getQuantity() %></p>
                            <p class="text-gray-700 mb-4">Giá: <%= item.getPrice() %></p>
                            <p class="text-gray-700 mb-4">Tổng: <%= item.getQuantity() * item.getPrice() %></p>
                        </div>
            <%
                    }
                }
            %>
        </div>
    </div>

    <script>
        const btn = document.querySelector("button.mobile-menu-button");
        const menu = document.querySelector(".mobile-menu");

        btn.addEventListener("click", () => {
            menu.classList.toggle("hidden");
        });
    </script>
</body>
</html>