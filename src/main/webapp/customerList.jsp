<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Users" %>
<%@ page import="Utils.DBUsers" %>
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
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <title>Customer Dashboard</title>
    <style>
        body { font-family: 'Roboto', sans-serif; }
        .hidden { display: none; }
    </style>
</head>
<body class="bg-gray-100">
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
                        <a class="py-5 px-3 text-gray-700" href="Product" id="nav-products">Products</a>
                        <a class="py-5 px-3 text-gray-700" href="Order" id="nav-orders">Orders</a>
                        <a class="py-5 px-3 text-gray-700" href="Customer" id="nav-customers">Customers</a>
                        <a class="py-5 px-3 text-gray-700" href="Statistical" id="nav-statistics">Statistics</a>
                        <a class="py-5 px-3 text-gray-700" href="Category" id="nav-category">Category</a>
                    </div>
                </div>
               <form action="SignInUp" method="post" class="hidden md:flex items-center space-x-1">
                   <input type="hidden" name="action" value="logout">
                   <button type="submit" class="py-2 px-3 bg-orange-500 text-white rounded-full hover:bg-orange-600 transition-colors">
                       Logout
                   </button>
               </form>
                <div class="md:hidden flex items-center">
                    <button class="mobile-menu-button">
                        <i class="fas fa-bars"></i>
                    </button>
                </div>
            </div>
        </div>
    </nav>
    <div class="mobile-menu hidden md:hidden">
        <a class="block py-2 px-4 text-sm" href="Product" id="mobile-nav-products">Products</a>
        <a class="block py-2 px-4 text-sm" href="Order" id="mobile-nav-orders">Orders</a>
        <a class="block py-2 px-4 text-sm" href="Customer" id="mobile-nav-customers">Customers</a>
        <a class="block py-2 px-4 text-sm" href="Statistical" id="mobile-nav-statistics">Statistics</a>
        <a class="block py-2 px-4 text-sm" href="Category" id="mobile-nav-category">Category</a>
    </div>
    <div class="max-w-7xl mx-auto px-4 py-6" id="main-content">
        <h1 class="text-3xl font-bold mb-6">Danh sách khách hàng</h1>
        <div class="flex justify-between mb-4">
            <div class="relative">
                <form action="Customer" method="get">
                    <input type="text" name="search" placeholder="Tìm kiếm" class="p-2 border border-gray-300 rounded-lg pl-10">
                    <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
                </form>
            </div>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="customer-list">
            <%
                List<Users> usersList = (List<Users>) request.getAttribute("usersList");
                if (usersList != null) {
                    for (Users customer : usersList) {
            %>
            <div class="bg-white p-6 rounded-lg shadow-lg">
                <h2 class="text-xl font-bold mb-2"><%= customer.getUsername() %></h2>
                <p class="text-gray-700 mb-4">Email: <%= customer.getEmail() %></p>
                <p class="text-gray-700 mb-4">Phone: <%= customer.getPhone() %></p>
                <p class="text-gray-700 mb-4">Address: <%= customer.getAddress() %></p>
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