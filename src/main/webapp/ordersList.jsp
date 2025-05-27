<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Orders" %>
<%@ page import="Utils.DBOrder" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <title>Order Dashboard</title>
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
        <!-- Orders Section -->
        <h1 class="text-3xl font-bold mb-6">Danh sách đơn hàng</h1>
        <div class="flex justify-between mb-4">
            <div class="relative">
                <form action="Order" method="get">
                    <input type="text" name="search" placeholder="Tìm kiếm" class="p-2 border border-gray-300 rounded-lg pl-10">
                    <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
                </form>
            </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="order-list">
            <!-- Order Cards will be inserted here dynamically -->
            <%
                List<Orders> ordersList = (List<Orders>) request.getAttribute("ordersList"); // Lấy danh sách đơn hàng
                if (ordersList != null) {
                    for (Orders order : ordersList) { // Duyệt qua danh sách đơn hàng
            %>
                        <div class="bg-white p-6 rounded-lg shadow-lg">
                            <h2 class="text-xl font-bold mb-2">Mã đơn hàng: <%= order.getOrderId() %></h2>
                            <p class="text-gray-700 mb-4">Tên khách hàng: <%= order.getCustomerName() %></p>
                            <p class="text-gray-700 mb-4">Tổng tiền: <%= order.getTotalAmount() %></p>
                            <p class="text-gray-700 mb-4">Trạng thái thanh toán: <%= order.isPaymentStatus() ? "Đã thanh toán" : "Chưa thanh toán" %></p>
                            <p class="text-gray-700 mb-4">Trạng thái giao hàng: <%= order.getDeliveryStatus() %></p>
                            <p class="text-gray-700 mb-4">Ngày tạo: <%= order.getCreatedAt() %></p>
                            <p class="text-gray-700 mb-4">Ngày cập nhật: <%= order.getUpdatedAt() %></p>
                            <!-- Cập nhật liên kết chi tiết dz hàng -->
                            <a href="OrderDetail?orderId=<%= order.getOrderId() %>" class="bg-blue-500 text-white px-3 py-1 rounded">Chi tiết</a>
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