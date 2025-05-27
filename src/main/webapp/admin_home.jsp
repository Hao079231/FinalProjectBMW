<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Products" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <title>Shop Owner Dashboard</title>
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
                        <a class="py-5 px-3 text-gray-700" href="Order" id="nav-orders">Orders</a>
                        <a class="py-5 px-3 text-gray-700" href="Customer" id="nav-customers">Customers</a>
                        <a class="py-5 px-3 text-gray-700" href="Statistical" id="nav-statistics">Statistics</a>
                        <a class="py-5 px-3 text-gray-700" href="Category" id="nav-category">Category</a>
                    </div>
                </div>
                <!-- Secondary Nav -->
                <div class="hidden md:flex items-center space-x-1">
                    <form action="SignInUp" method="post">
                        <input type="hidden" name="action" value="logout">
                        <button type="submit" class="py-2 px-3 bg-orange-500 text-white rounded-full">Logout</button>
                    </form>
                </div>
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
        <a class="block py-2 px-4 text-sm" href="Product" id="mobile-nav-products">Products</a>
        <a class="block py-2 px-4 text-sm" href="Order" id="mobile-nav-orders">Orders</a>
        <a class="block py-2 px-4 text-sm" href="Customer" id="mobile-nav-customers">Customers</a>
        <a class="block py-2 px-4 text-sm" href="Statistical" id="mobile-nav-statistics">Statistics</a>
        <a class="block py-2 px-4 text-sm" href="Category" id="mobile-nav-category">Category</a>
        <form action="SignInUp" method="post">
            <input type="hidden" name="action" value="logout">
            <button type="submit" class="block py-2 px-4 text-sm text-white bg-orange-500">Logout</button>
        </form>
    </div>
    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 py-6" id="main-content">
        <!-- Products Section -->
        <h1 class="text-3xl font-bold mb-6">Products</h1>

        <%
            String status = request.getParameter("status");
            if (status != null) {
                String message = "";
                String bgColor = "bg-green-600";
                if (status.equals("add_success")) {
                    message = "Thêm sản phẩm thành công!";
                } else if (status.equals("edit_success")) {
                    message = "Cập nhật sản phẩm thành công!";
                } else if (status.equals("error")) {
                    message = "Có lỗi xảy ra. Vui lòng thử lại.";
                    bgColor = "bg-red-600";
                }
                if (!message.isEmpty()) {
        %>
        <div class="<%= bgColor %> text-white p-4 rounded-lg shadow-lg mb-6 flex items-center space-x-4">
            <i class="fas fa-<%= status.equals("error") ? "exclamation-circle" : "check-circle" %> text-xl"></i>
            <span><%= message %></span>
        </div>
        <%
                }
            }
        %>

        <div class="flex justify-between mb-4">
            <button class="bg-orange-500 text-white px-4 py-2 rounded" onclick="addProduct()">Thêm Sản Phẩm</button>
            <div class="relative">
                <form action="Product" method="get">
                    <input type="text" name="search" placeholder="Tìm kiếm" class="p-2 border border-gray-300 rounded-lg pl-10">
                    <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
                </form>
            </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="product-list">
            <%
                List<Products> productList = (List<Products>) request.getAttribute("productList");
                if (productList != null) {
                    for (Products product : productList) {
            %>
            <div class="bg-white p-6 rounded-lg shadow-lg">
                <img alt="Product Image" class="w-full h-80 object-cover mb-4 rounded"
                     src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>" />
                <h2 class="text-xl font-bold mb-2"><%= product.getName() %></h2>
                <p class="text-gray-700 mb-4"><%= product.getDescription() %></p>
                <div class="flex justify-between items-center">
                    <span class="text-gray-900 font-bold"><%= (int) product.getPrice() %> VND</span>
                    <div class="flex space-x-2">
                        <button class="bg-orange-500 text-white px-3 py-1 rounded"
                                onclick="editProduct(<%= product.getProductId() %>)">Sửa</button>
                        <button class="bg-red-500 text-white px-3 py-1 rounded"
                                onclick="deleteProduct(<%= product.getProductId() %>)">Xóa</button>
                    </div>
                </div>
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

        function addProduct() {
            window.location.href = 'Product?action=add';
        }

        function editProduct(productId) {
            window.location.href = 'Product?action=edit&productId=' + productId;
        }

        function deleteProduct(productId) {
            if (confirm('Bạn có chắc muốn xóa sản phẩm này không')) {
                window.location.href = 'Product?action=delete&productId=' + productId;
            }
        }
    </script>
</body>
</html>