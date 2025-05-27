<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Users" %>
<%@ page import="Utils.DBOrder" %>
<%@ page import="Beans.OrderItem" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    HttpSession sessionCheck = request.getSession(false);
    if (sessionCheck == null || sessionCheck.getAttribute("user") == null) {
        response.sendRedirect("signup.jsp?status=login_required");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet" />
    <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
    <title>JellyCat</title>
</head>

<body class="font-roboto bg-gray-100">
<%-- Kiểm tra và hiển thị thông báo lỗi nếu có --%>
<% if (request.getAttribute("error") != null) { %>
<div id="error-toast" class="bg-red-600 text-white p-2 rounded-lg shadow-md mb-4 flex items-center space-x-2">
    <i class="fas fa-exclamation-circle text-base"></i>
    <span class="text-sm"><c:out value="${requestScope.error}" /></span>
    <button onclick="closeToast('error-toast')" class="ml-2 bg-transparent text-white font-bold py-1 px-2 border border-white rounded text-sm">OK</button>
</div>
<% } %>

<%-- Kiểm tra thông báo thành công --%>
<% if (request.getAttribute("message") != null) { %>
<div id="success-toast" class="bg-green-600 text-white p-2 rounded-lg shadow-md mb-4 flex items-center space-x-2">
    <i class="fas fa-check-circle text-base"></i>
    <span class="text-sm"><c:out value="${requestScope.message}" /></span>
    <button onclick="closeToast('success-toast')" class="ml-2 bg-transparent text-white font-bold py-1 px-2 border border-white rounded text-sm">OK</button>
</div>
<% } %>

<script>
    // Hàm đóng thông báo
    function closeToast(id) {
        var toast = document.getElementById(id);
        if (toast) {
            toast.style.display = 'none'; // Ẩn thông báo khi nhấn OK
        }
    }

    // Tự động ẩn thông báo sau một khoảng thời gian (ví dụ 5 giây)
    setTimeout(function() {
        var successToast = document.getElementById('success-toast');
        var errorToast = document.getElementById('error-toast');
        if (successToast) successToast.style.display = 'none';
        if (errorToast) errorToast.style.display = 'none';
    }, 5000);
</script>

<div class="max-w-7xl mx-auto px-4 py-6" id="main-content">
    <h1 class="text-3xl font-bold mb-6">Chi tiết đơn hàng</h1>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="order-item-list">
        <%
            List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("orderItems");
            if (orderItems != null) {
                for (OrderItem item : orderItems) {
        %>
        <div class="bg-white p-6 rounded-lg shadow-lg">
            <img alt="Product Image" class="w-full h-80 object-cover mb-4 rounded" src="data:image/jpeg;base64,<%= new String(java.util.Base64.getEncoder().encode(item.getImage())) %>" />
            <h3 class="text-xl font-bold mb-2">Sản phẩm: <c:out value="<%= item.getProductName() %>" /></h3>
            <p class="text-gray-700 mb-4">Số lượng: <c:out value="<%= String.valueOf(item.getQuantity()) %>" /></p>
            <p class="text-gray-700 mb-4">Giá: <c:out value="<%= String.valueOf(item.getPrice()) %>" /></p>
            <p class="text-gray-700 mb-4">Tổng: <c:out value="<%= String.valueOf(item.getQuantity() * item.getPrice()) %>" /></p>

            <%-- Thêm nút Đánh giá, cần chỉnh qua góc phải --%>
            <form action="Review" method="post" class="flex justify-end">
                <input type="hidden" name="action" value="check" />
                <input type="hidden" name="productId" value="<c:out value='<%= String.valueOf(item.getProductId()) %>' />" />
                <input type="hidden" name="orderId" value="<c:out value='<%= String.valueOf(item.getOrderId()) %>' />" />
                <button type="submit" class="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700">Đánh giá</button>
            </form>
        </div>
        <%
                }
            }
        %>
    </div>

    <%-- Nút quay lại --%>
    <div class="flex justify-start mt-4">
        <a href="CustomerOrder" class="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-700">Quay lại</a>
    </div>
</div>
</body>
</html>