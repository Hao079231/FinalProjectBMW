<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <title>Add New Category</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
        }
    </style>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="bg-white p-8 rounded-lg shadow-lg w-full max-w-md">
        <h1 class="text-3xl font-bold mb-6">Thêm Danh Mục Mới</h1>
        <form action="Category" method="post" class="space-y-4">
            <input type="hidden" name="action" value="add">
            <div>
                <label for="cname" class="block text-gray-700 mb-2">Tên Danh Mục</label>
                <input type="text" id="cname" name="cname" required
                       class="w-full p-2 border border-gray-300 rounded">
            </div>
            <button type="submit" class="w-full bg-orange-500 text-white py-2 rounded hover:bg-orange-600">Thêm</button>
            <button type="button" onclick="window.location.href='Category?action=list'"
                    class="w-full bg-gray-500 text-white py-2 rounded hover:bg-gray-600 mt-2">Hủy</button>
        </form>
    </div>
</body>
</html>