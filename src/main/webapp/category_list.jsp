<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Categories" %>
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
    <title>Shop Owner Dashboard - Category List</title>
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
                    <a class="flex items-center py-5 px-2 text-gray-700" href="#">
                        <i class="fas fa-store text-2xl"></i>
                        <span class="font-bold text-2xl ml-2">Jellycat</span>
                    </a>
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
    <!-- Mobile Menu -->
    <div class="mobile-menu hidden md:hidden">
        <a class="block py-2 px-4 text-sm" href="Product" id="mobile-nav-products">Products</a>
        <a class="block py-2 px-4 text-sm" href="Order" id="mobile-nav-orders">Orders</a>
        <a class="block py-2 px-4 text-sm" href="Customer" id="mobile-nav-customers">Customers</a>
        <a class="block py-2 px-4 text-sm" href="Statistical" id="mobile-nav-statistics">Statistics</a>
        <a class="block py-2 px-4 text-sm" href="Category" id="mobile-nav-category">Category</a>
    </div>
    <!-- Content -->
    <div class="container mx-auto mt-8">
        <h2 class="text-2xl font-bold mb-4">Danh mục sản phẩm</h2>
        <a href="category_add.jsp" class="bg-blue-500 text-white px-4 py-2 rounded mb-4 inline-block">Add New Category</a>
        <%
            List<Categories> categoriesList = (List<Categories>) request.getAttribute("categoriesList");
        %>
        <div class="bg-white shadow-md rounded my-6">
            <table class="min-w-full bg-white">
                <thead class="bg-gray-800 text-white">
                    <tr>
                        <th class="w-1/3 py-3 px-4 uppercase font-semibold text-sm text-left">Category ID</th>
                        <th class="w-1/3 py-3 px-4 uppercase font-semibold text-sm text-left">Category Name</th>
                        <th class="w-1/3 py-3 px-4 uppercase font-semibold text-sm text-left">Actions</th>
                    </tr>
                </thead>
                <tbody class="text-gray-700">
                    <%
                        if (categoriesList != null && !categoriesList.isEmpty()) {
                            for (Categories category : categoriesList) {
                    %>
                    <tr class="hover:bg-gray-100">
                        <td class="py-3 px-4"><%= category.getCategoryId() %></td>
                        <td class="py-3 px-4"><%= category.getCname() %></td>
                        <td class="py-3 px-4">
                            <a href="Category?action=edit&categoryId=<%= category.getCategoryId() %>" class="text-yellow-500 hover:underline">Edit</a> |
                            <a href="Category?action=delete&categoryId=<%= category.getCategoryId() %>" class="text-red-500 hover:underline" onclick="return confirm('Are you sure you want to delete this category?');">Delete</a>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="3" class="text-center py-3 text-red-500">No categories available.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <script>
        const mobileMenuButton = document.querySelector(".mobile-menu-button");
        const mobileMenu = document.querySelector(".mobile-menu");

        mobileMenuButton.addEventListener("click", () => {
            mobileMenu.classList.toggle("hidden");
        });
    </script>
</body>
</html>