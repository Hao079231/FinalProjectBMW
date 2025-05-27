<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Beans.Users" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang cá nhân</title>
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

        input[type="text"], input[type="email"], input[type="phone"], input[type="address"] {
            width: 100%;
            padding: 8px;
            margin: 10px 0;
            border-radius: 4px;
            border: 1px solid #ccc;
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

        .btn-save {
            background-color: #4CAF50;
            color: white;
        }

        .btn-save:hover {
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
        <button type="submit" class="logout-btn">Đăng xuất</button>
    </form>

    <div class="container">
        <h1>Trang cá nhân</h1>

        <% if (user != null) { %>
            <form action="User?action=update" method="post">
                <input type="hidden" name="userId" value="<%= user.getUserId() %>" />
                <p><strong>Tên:</strong></p>
                <input type="text" name="username" value="<%= user.getUsername() %>" required />

                <p><strong>Email:</strong></p>
                <input type="email" name="email" value="<%= user.getEmail() %>" required />

                <p><strong>Số điện thoại:</strong></p>
                <input type="text" name="phone" value="<%= user.getPhone() %>" required />

                <p><strong>Địa chỉ:</strong></p>
                <input type="text" name="address" value="<%= user.getAddress() %>" required />
                
                <div class="btn-group">
                    <input type="submit" class="btn-save" value="Save Changes">
                </div>
            </form>
        <% } else { %>
            <p>Bạn chưa đăng nhập.</p>
        <% } %>

        <div class="btn-group">
            <!-- Button to cancel and go back -->
            <form action="profile.jsp" method="get">
                <input type="submit" class="btn-cancel" value="Cancel">
            </form>
        </div>
    </div>
</body>
</html>