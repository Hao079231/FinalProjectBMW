<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Beans.Users" %>
<%@ page import="Utils.DBOrderDetail" %>
<%@ page import="Beans.SQLServerConnection" %>
<%@ page import="Beans.OrderItem" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <title>Đánh giá sản phẩm</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet" />
    <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
    <style>body { font-family: 'Roboto', sans-serif; }</style>
</head>

<body class="bg-gray-100 flex items-center justify-center min-h-screen">
<div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-2xl">
    <h1 class="text-3xl font-bold mb-6 text-center">Đánh giá sản phẩm</h1>
    <form action="Review" method="post">
        <input type="hidden" name="action" value="submit" />
        <input type="hidden" id="userId" name="userId" value="<c:out value='${sessionScope.userId}' />" />
        <input type="hidden" id="productId" name="productId" value="<c:out value='${param.productId}' />" />
        <input type="hidden" id="orderId" name="orderId" value="<c:out value='${requestScope.orderId}' />" />

        <div class="mb-4">
            <label for="rating" class="block text-gray-700 mb-2">Đánh giá:</label>
            <select id="rating" name="rating" class="w-full p-2 border border-gray-300 rounded" required>
                <option value="">Chọn đánh giá</option>
                <option value="1">1 - Rất tệ</option>
                <option value="2">2 - Tệ</option>
                <option value="3">3 - Bình thường</option>
                <option value="4">4 - Tốt</option>
                <option value="5">5 - Rất tốt</option>
            </select>
        </div>

        <div class="mb-4">
            <label for="comment" class="block text-gray-700 mb-2">Nhận xét:</label>
            <textarea id="comment" name="comment" rows="4" class="w-full p-2 border border-gray-300 rounded"
                      placeholder="Nhập nhận xét của bạn..."></textarea>
        </div>

        <div class="flex justify-between">
            <button type="submit" class="bg-orange-500 text-white px-4 py-2 rounded">Gửi đánh giá</button>
            <a href="CustomerOrder?order_id=<c:out value='${requestScope.orderId}' />"
               class="bg-gray-500 text-white px-4 py-2 rounded">Quay lại</a>
        </div>
    </form>
</div>
</body>
</html>