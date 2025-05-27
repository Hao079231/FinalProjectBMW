<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Products" %>
<!DOCTYPE html>
<html lang="en">
<head>
 <meta charset="utf-8"/>
 <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
 <title>Product Page</title>
 <script src="https://cdn.tailwindcss.com"></script>
 <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
 <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
</head>
<body class="font-roboto bg-gray-100">
 <header class="bg-white shadow">
   <div class="container mx-auto px-4 py-6 flex justify-between items-center">
     <div class="text-2xl font-bold text-gray-800">MyStore</div>
     <nav class="space-x-4">
       <a class="text-gray-600 hover:text-gray-800" href="user_home">Home</a>
       <a class="text-gray-600 hover:text-gray-800" href="productMenu.jsp">Products</a>
       <a class="text-gray-600 hover:text-gray-800" href="about.jsp">About</a>
     </nav>
   </div>
 </header>
 <main class="container mx-auto px-4 py-6">
   <h2 class="text-2xl font-bold mb-4">Product List</h2>
   <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
     <%
       List<Products> productList = (List<Products>) request.getAttribute("productList");
       if (productList != null) {
         for (Products product : productList) {
     %>
     <div class="border rounded-lg p-4 bg-white shadow">
       <h3 class="text-lg font-semibold"><%= product.getName() %></h3>
       <p class="text-gray-600"><%= product.getDescription() %></p>
       <p class="text-gray-800 font-bold">Price: <%= product.getPrice() %> VND</p>
       <p class="text-gray-600">Size: <%= product.getSize() %></p>
       <p class="text-gray-600">Stock: <%= product.getStock() %></p>
       <%
         // Hiển thị hình ảnh nếu có
         if (product.getImage() != null) {
             String base64Image = java.util.Base64.getEncoder().encodeToString(product.getImage());
       %>
       <img src="data:image/jpeg;base64,<%= base64Image %>" alt="<%= product.getName() %>" class="w-full h-auto"/>
       <%
         }
       %>
     </div>
     <%
         }
       } else {
     %>
     <p class="text-red-600">No products available.</p>
     <%
       }
     %>
   </div>
 </main>
</body>
</html>
