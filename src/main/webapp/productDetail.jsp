<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="Beans.Products" %>
<%@ page import="Beans.Review" %>
<%@ page import="Utils.DBProduct" %>
<%@ page import="Utils.BDReview" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="Beans.DBConnection" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Products product = null;
    List<Review> reviews = null;
    String productIdStr = request.getParameter("productId");

    if (productIdStr != null) {
        try {
            int productId = Integer.parseInt(productIdStr);
            Connection connection = DBConnection.getConnection();

            DBProduct dbProduct = new DBProduct(connection);
            product = dbProduct.getProductById(productId);

            BDReview dbReview = new BDReview(connection);
            reviews = dbReview.getReviewsByProductId(productId);

            if (product != null && product.getImage() != null) {
                String base64Image = java.util.Base64.getEncoder().encodeToString(product.getImage());
                request.setAttribute("base64Image", base64Image);
            }

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMsg", "Invalid product ID.");
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "Database error: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("errorMsg", "An error occurred: " + e.getMessage());
        }
    } else {
        request.setAttribute("errorMsg", "No product ID provided.");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${product != null ? product.name : 'Product Detail'}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@1.9.6/dist/tailwind.min.css" rel="stylesheet">
</head>
<body class="bg-gray-100">

<!-- Header -->
<header class="bg-white shadow-lg">
    <div class="container mx-auto flex justify-between items-center py-4 px-6">
        <div class="logo flex items-center">
            <img alt="Jellycat logo" class="mr-4" height="50" width="50" src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg">
            <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
        </div>
        <div class="flex items-center space-x-6">
            <a href="ProductList" class="text-gray-600 hover:text-blue-500">Home</a>
        </div>
    </div>
</header>

<div class="container mx-auto py-8">
    <h1 class="text-4xl font-semibold text-center mb-8">
        <c:out value="${product != null ? product.name : 'Product Not Found'}" />
    </h1>

    <c:if test="${not empty errorMsg}">
        <p class="text-red-600 text-center mb-4"><c:out value="${errorMsg}" /></p>
    </c:if>

    <c:if test="${product != null}">
        <div class="flex justify-center items-start space-x-8">
            <div class="w-1/3">
                <c:choose>
                    <c:when test="${not empty base64Image}">
                        <img src="data:image/jpeg;base64,${base64Image}"
                             alt="<c:out value='${product.name}'/>"
                             class="w-full h-80 object-cover rounded-lg shadow-md"/>
                    </c:when>
                    <c:otherwise>
                        <img src="default-product.jpg" alt="No image available"
                             class="w-full h-80 object-cover rounded-lg shadow-md"/>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="w-2/3">
                <p class="text-black-700 text-lg mb-4"><c:out value="${product.description}" /></p>
                <p class="text-green-600 font-bold text-xl mb-4">Price: <c:out value="${product.price}" /> VND</p>
                <p class="text-black-500 text-sm mb-4">Stock: <c:out value="${product.stock}" /></p>

                <form method="post" action="Cart" class="flex items-center">
                    <input type="hidden" name="action" value="add"/>
                    <input type="hidden" name="userId" value="<c:out value='${sessionScope.userId}' />" />
                    <input type="hidden" name="productId" value="<c:out value='${product.productId}' />" />
                    <input type="hidden" name="productName" value="<c:out value='${product.name}' />" />
                    <input type="hidden" name="price" value="<c:out value='${product.price}' />" />
                    <input type="hidden" name="productImage" value="<c:out value='${base64Image}' />" />
                    <input type="hidden" name="quantity" value="1" />
                    <button type="submit" class="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition duration-300">
                        Thêm vào giỏ hàng
                    </button>
                </form>
            </div>
        </div>

        <h2 class="text-2xl font-bold mt-12 mb-4">Đánh giá của khách hàng</h2>
        <c:if test="${not empty reviews}">
            <div class="space-y-4">
                <c:forEach var="review" items="${reviews}">
                    <div class="bg-white p-4 rounded-lg shadow">
                        <p class="text-sm text-black-400">Người đánh giá: <c:out value="${review.username}" /></p>
                        <p class="text-lg font-semibold"><c:out value="${review.comment}" /></p>
                        <p class="text-sm text-black-500">Rating: <c:out value="${review.rating}" />/5</p>
                        <p class="text-sm text-black-400">Ngày: <c:out value="${review.createdAt}" /></p>
                    </div>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${empty reviews}">
            <p class="text-black-500">Sản phẩm này chưa có đánh giá!</p>
        </c:if>
    </c:if>
</div>

<footer class="bg-blue-500 text-white py-4">
    <div class="text-center">
        <p>&copy; 2024 Jellycat.</p>
    </div>
</footer>
</body>
</html>