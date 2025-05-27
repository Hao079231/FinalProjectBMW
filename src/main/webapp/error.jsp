<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lỗi Truy Cập</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="bg-white p-8 rounded-lg shadow-lg max-w-md text-center">
        <h1 class="text-2xl font-bold text-red-600 mb-4">Lỗi Truy Cập</h1>
        <p class="text-gray-700 mb-4">
            <%= request.getParameter("message") != null ? request.getParameter("message") : "Bạn không có quyền truy cập trang này." %>
        </p>
        <a href="ProductList" class="bg-blue-500 text-white px-4 py-2 rounded">Quay lại Trang Chủ</a>
    </div>
</body>
</html>