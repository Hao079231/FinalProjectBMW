<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Beans.Cart" %>
<%@ page import="Beans.CartItem" %>
<%@ page import="Beans.Products" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Users" %>
<%@ page import="Utils.DBOrder" %>
<%@ page import="Beans.SQLServerConnection" %>
<%@ page import="java.sql.Connection" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
    <!-- CSRF Token để JavaScript có thể truy cập -->
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/tailwindcss@1.9.6/dist/tailwind.min.css">
    <!-- Nhúng script bảo vệ CSRF -->
    <script
        src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
</head>

<body class="bg-gray-100">

    <header class="bg-white shadow-lg">
        <div
            class="container mx-auto flex justify-between items-center py-4 px-6">
            <div class="logo flex items-center">
                <img alt="Logo" class="mr-4" height="50"
                    src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg"
                    width="50">
                <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
            </div>
            <div class="flex items-center space-x-6">
                <a href="ProductList"
                    class="text-gray-600 hover:text-blue-500">Home</a>
                <a href="Cart"
                    class="text-gray-600 hover:text-blue-500">Cart</a>
            </div>
        </div>
    </header>

    <div class="container mx-auto py-8">
        <h1 class="text-3xl font-bold mb-4">Đặt hàng</h1>
        <% String errorMessage=(String) session.getAttribute("error"); if
            (errorMessage !=null) { %>
            <div class="alert alert-danger text-red-500">
                <%= errorMessage %>
            </div>
            <% session.removeAttribute("error"); } %>
                <% Beans.Cart cart=(Beans.Cart)
                    session.getAttribute("cart"); if (cart !=null &&
                    !cart.isEmpty()) { %>

                    <!-- Giỏ hàng -->
                    <h2 class="text-2xl font-semibold mb-4">Sản phẩm</h2>
                    <table
                        class="min-w-full bg-white border border-gray-300 mb-6">
                        <thead>
                            <tr>
                                <th class="py-2 px-4 border-b">Tên sản phẩm</th>
                                <th class="py-2 px-4 border-b">Giá</th>
                                <th class="py-2 px-4 border-b">số lượng</th>
                                <th class="py-2 px-4 border-b">Tổng</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%for(Map.Entry<Integer, CartItem> entry :
                                cart.getItems().entrySet()) {
                                Integer productId = entry.getKey();
                                CartItem item = entry.getValue();
                                Products product = item.getProduct();
                                %>
                                <tr>
                                    <td class="py-2 px-4 border-b">
                                        <%= product.getName() %>
                                    </td>
                                    <td class="py-2 px-4 border-b">
                                        <%= product.getPrice() %> VND
                                    </td>
                                    <td class="py-2 px-4 border-b">
                                        <%= item.getQuantity() %>
                                    </td>
                                    <td class="py-2 px-4 border-b">
                                        <%= item.getTotal() %> VND
                                    </td>
                                </tr>
                                <% } %>
                        </tbody>
                    </table>

                    <div class="mt-4">
                        <h2 class="text-xl font-bold">Tổng: <%=
                                cart.getTotalPrice() %> VND</h2>
                    </div>

                    <!-- Form thông tin giao hàng và thanh toán -->
                    <h2 class="text-2xl font-semibold mt-8 mb-4">Địa chỉ giao
                        hàng</h2>
                    <form id="checkoutForm"
                        action="<%= request.getContextPath() %>/PayOrder"
                        method="post">
                        <input type="hidden" name="action" value="submit" />
                        <div class="mb-4">
                            <label for="shippingAddress"
                                class="block text-sm font-semibold">Địa
                                chỉ</label>
                            <input type="text" id="shippingAddress"
                                name="shippingAddress" required
                                class="w-full p-2 border border-gray-300 rounded mt-2" />
                        </div>

                        <div class="mt-6">
                            <button type="submit"
                                class="bg-blue-500 text-white px-6 py-3 rounded">Đặt
                                hàng</button>
                        </div>
                    </form>
                    <% } else { %>
                        <div class="text-center text-red-500">
                            <p>Giỏ hàng của bạn trống!</p>
                        </div>
                        <% } %>
    </div>
</body>

</html>