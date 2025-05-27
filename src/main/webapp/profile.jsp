<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Beans.Users" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Personal Information</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 350px;
            margin: 50px auto;
        }

        h1 {
            text-align: center;
            color: #333;
            font-size: 18px;
            margin-bottom: 20px;
        }

        p {
            font-size: 14px;
            color: #555;
            margin-bottom: 10px;
        }

        .btn-group {
            text-align: center;
        }

        .btn-group input {
            width: 100px;
            padding: 8px;
            margin: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-edit {
            background-color: #4CAF50;
            color: white;
        }

        .btn-edit:hover {
            background-color: #45a049;
        }

        .btn-cancel {
            background-color: #f44336;
            color: white;
        }

        .btn-cancel:hover {
            background-color: #e53935;
        }

        .logout-btn {
            background-color: #ff9800;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            position: fixed;
            top: 20px;
            right: 20px;
        }

        .logout-btn:hover {
            background-color: #f57c00;
        }
    </style>
</head>
<body>
    <%
        HttpSession ses = request.getSession();
        Users user = (Users) ses.getAttribute("user");
    %>

    <!-- Logout button -->
    <form action="SignInUp" method="post">
        <input type="hidden" name="action" value="logout">
        <button type="submit" class="logout-btn">Đăng xuất</button>
    </form>

    <div class="container">
        <h1>Trang cá nhân</h1>

        <% if (user != null) { %>
            <p><strong>Tên:</strong> <%= user.getUsername() %></p>
            <p><strong>Email:</strong> <%= user.getEmail() %></p>
            <p><strong>Số điện thoại:</strong> <%= user.getPhone() %></p>
            <p><strong>Địa chỉ:</strong> <%= user.getAddress() %></p>
        <% } else { %>
            <p>Bạn chưa đăng nhập</p>
        <% } %>

        <div class="btn-group">
            <!-- Button to go to edit page -->
            <form action="editProfile.jsp" method="get" style="display: inline-block; margin-right: 10px;">
                <input type="submit" class="btn-edit" value="Sửa">
            </form>
            <!-- Button to go back to user home -->
            <form action="ProductList" method="get" style="display: inline-block;">
                <input type="submit" class="btn-cancel" value="Hủy">
            </form>
        </div>
    </div>
</body>
</html>