<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Products" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
  <title>Products by Category</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
</head>
<body class="font-roboto bg-gray-100">
  <!-- Header Section -->
  <header class="bg-white shadow-lg">
    <div class="container mx-auto flex justify-between items-center py-4 px-6">
      <div class="logo flex items-center">
        <img alt="Jellycat logo" class="mr-4" height="50" src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg" width="50">
        <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
      </div>
      <div class="flex items-center space-x-6">
        <!-- Home link -->
        <a href="ProductList" class="text-gray-600 hover:text-blue-500">Trang chủ</a>
      </div>
    </div>
  </header>

  <!-- Main Content: Product List -->
  <main class="container mx-auto px-4 py-6">
    <h2 class="text-2xl font-bold text-gray-800 mb-4">Sản phẩm theo danh mục bạn chọn</h2>

    <!-- Product Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <%
        List<Products> productList = (List<Products>) request.getAttribute("productList");
        if (productList != null && !productList.isEmpty()) {
          for (Products product : productList) {
      %>
        <div class="product-card p-4 bg-white rounded shadow-md relative group">
          <!-- Product Image and Name (Clickable) -->
          <button onclick="window.location.href='productDetail.jsp?productId=<%= product.getProductId() %>'" class="w-full text-left">
            <img class="w-full h-64 object-cover rounded"
                 src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>"
                 alt="<%= product.getName() %>" />
            <h3 class="text-lg font-bold text-center my-2"><%= product.getName() %></h3>
          </button>
          <p class="text-gray-600 text-sm mb-2"><%= product.getDescription() %></p>
          <div class="flex justify-between items-center mb-4">
            <p class="text-green-600 font-semibold">Price: <%= product.getPrice() %> VND</p>
            <p class="text-gray-500">Stock: <%= product.getStock() %></p>
          </div>
          <!-- Add to Cart Form -->
          <form method="post" action="CartServlet">
            <input type="hidden" name="action" value="add"/>
            <input type="hidden" name="userId" value="<%= session.getAttribute("userId") %>" />
            <input type="hidden" name="productId" value="<%= product.getProductId() %>" />
            <input type="hidden" name="productName" value="<%= product.getName() %>">
            <input type="hidden" name="price" value="<%= product.getPrice() %>">
            <input type="hidden" name="productImage" value="<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>">
            <input type="hidden" name="quantity" value="1" />
            <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>"/>
            <button type="submit"
                    class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 focus:ring-2 focus:ring-blue-500
                           absolute bottom-4 left-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity">
              Thêm vào giỏ hàng
            </button>
          </form>
        </div>
      <%
          }
        } else {
      %>
        <p>Danh mục hiện tại chưa có sản phẩm.</p>
      <%
        }
      %>
    </div>
  </main>

  <!-- Footer Section -->
  <footer class="bg-blue-500 text-white py-4">
    <div class="container mx-auto text-center">
      <p>© 2024 Jellycat. All rights reserved.</p>
    </div>
  </footer>
</body>
</html>