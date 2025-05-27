<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Beans.Cart"%>
<%@page import="Beans.CartItem"%>
<%@page import="java.util.Map"%>
<%@page import="Beans.Products"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng của bạn</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@1.9.6/dist/tailwind.min.css">
</head>
<body class="bg-gray-100">
    <%
        String message = (String) session.getAttribute("message");
        String error = (String) session.getAttribute("error");
        session.removeAttribute("message");
        session.removeAttribute("error");
    %>

    <!-- Success Toast -->
    <% if (message != null) { %>
        <div id="success-toast" class="bg-green-600 text-white p-4 rounded-lg shadow-lg mb-6 flex items-center space-x-4">
            <i class="fas fa-check-circle text-xl"></i>
            <span><%= message %></span>
            <button onclick="closeToast('success-toast')" class="ml-4 bg-transparent text-white font-bold py-2 px-4 border border-white rounded">OK</button>
        </div>
    <% } %>

    <!-- Error Toast -->
    <% if (error != null) { %>
        <div id="error-toast" class="bg-red-600 text-white p-4 rounded-lg shadow-lg mb-6 flex items-center space-x-4">
            <i class="fas fa-exclamation-circle text-xl"></i>
            <span><%= error %></span>
            <button onclick="closeToast('error-toast')" class="ml-4 bg-transparent text-white font-bold py-2 px-4 border border-white rounded">OK</button>
        </div>
    <% } %>

    <script>
        function closeToast(id) {
            var toast = document.getElementById(id);
            if (toast) {
                toast.style.display = 'none';
            }
        }
        setTimeout(function() {
            var successToast = document.getElementById('success-toast');
            var errorToast = document.getElementById('error-toast');
            if (successToast) successToast.style.display = 'none';
            if (errorToast) errorToast.style.display = 'none';
        }, 5000);
    </script>

    <header class="bg-white shadow-lg">
        <div class="container mx-auto flex justify-between items-center py-4 px-6">
            <div class="logo flex items-center">
                <img alt="Jellycat logo" class="mr-4" height="50" src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg" width="50">
                <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
            </div>
            <div class="flex items-center space-x-6">
                <a href="ProductList" class="text-gray-600 hover:text-blue-500">Trang chủ</a>
            </div>
        </div>
    </header>

    <div class="container mx-auto py-8">
        <h1 class="text-3xl font-bold mb-4">GIỎ HÀNG</h1>

        <%
            Beans.Cart cart = (Beans.Cart) session.getAttribute("cart");
            boolean hasInvalidItems = false;
            if (cart != null && !cart.isEmpty()) {
                for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
                    CartItem item = entry.getValue();
                    if (item.getQuantity() < 1 || item.getQuantity() > item.getProduct().getStock() || !item.getProduct().getStatus()) {
                        hasInvalidItems = true;
                        break;
                    }
                }
                if (hasInvalidItems) {
        %>
                <div class="bg-yellow-600 text-white p-4 rounded-lg shadow-lg mb-6 flex items-center space-x-4">
                    <i class="fas fa-exclamation-circle text-xl"></i>
                    <span>Một số sản phẩm trong giỏ hàng không hợp lệ hoặc hết hàng và đã bị xóa.</span>
                </div>
        <%
                }
        %>
        <table class="min-w-full bg-white border border-gray-300">
            <thead>
                <tr>
                    <th class="py-2 px-4 border-b">Hình ảnh</th>
                    <th class="py-2 px-4 border-b">Tên sản phẩm</th>
                    <th class="py-2 px-4 border-b">Giá</th>
                    <th class="py-2 px-4 border-b">Số lượng</th>
                    <th class="py-2 px-4 border-b">Tổng</th>
                    <th class="py-2 px-4 border-b">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Map.Entry<Integer, CartItem> entry : cart.getItems().entrySet()) {
                        Integer productId = entry.getKey();
                        CartItem item = entry.getValue();
                        Products product = item.getProduct();
                        if (item.getQuantity() >= 1 && item.getQuantity() <= product.getStock() && product.getStatus()) {
                %>
                            <tr>
                                <td class="py-2 px-4 border-b">
                                    <img src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>"
                                         alt="<%= product.getName() %>" class="w-20 h-20 object-cover"/>
                                </td>
                                <td class="py-2 px-4 border-b"><%= product.getName() %></td>
                                <td class="py-2 px-4 border-b"><%= product.getPrice() %> VND</td>
                                <td class="py-2 px-4 border-b">
                                    <form action="Cart" method="post" class="inline">
                                        <input type="hidden" name="action" value="edit"/>
                                        <input type="hidden" name="productId" value="<%= productId %>"/>
                                        <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1" max="<%= product.getStock() %>" class="border border-gray-300 rounded px-2"/>
                                        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>" />
                                        <button type="submit" class="ml-2 bg-blue-600 text-white rounded px-2 py-1">Cập nhật</button>
                                    </form>
                                </td>
                                <td class="py-2 px-4 border-b"><%= item.getTotal() %> VND</td>
                                <td class="py-2 px-4 border-b">
                                    <form action="Cart" method="post" class="inline">
                                        <input type="hidden" name="action" value="remove"/>
                                        <input type="hidden" name="productId" value="<%= productId %>"/>
                                        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>" />
                                        <button type="submit" class="bg-red-600 text-white rounded px-2 py-1">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <div class="mt-4">
            <h2 class="text-xl font-bold">Tổng tiền: <%= cart.getTotalPrice() %> VND</h2>
        </div>

        <div class="mt-4">
            <a href="ProductList" class="bg-green-600 text-white rounded px-4 py-2">TIẾP TỤC MUA SẮM</a>
            <a href="checkout.jsp" class="bg-blue-600 text-white rounded px-4 py-2 ml-2">ĐẶT HÀNG</a>
        </div>
        <%
            } else {
        %>
        <p class="text-gray-500">GIỎ HÀNG TRỐNG.</p>
        <a href="ProductList" class="bg-green-600 text-white rounded px-4 py-2">TIẾP TỤC MUA SẮM</a>
        <%
            }
        %>
    </div>
</body>
</html>