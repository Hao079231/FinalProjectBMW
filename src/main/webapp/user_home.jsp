<%@page import="java.util.Locale.Category" %>
<%@page import="Beans.Categories" %>
<%@page import="java.util.ArrayList" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Beans.Products" %>
<%@ page import="Beans.Products" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
        rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap"
        rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
    <title>Jellycat</title>
</head>

<body class="font-roboto">
    <!-- Slideshow Section -->
    <div class="slideshow-container">
        <%@ include file="slideHeader.jsp" %>
    </div>

    <!-- Header -->
    <header class="border-b">
        <div class="container mx-auto flex justify-between items-center py-4 px-6">
            <div class="logo flex items-center">
                <img alt="Jellycat logo" class="mr-4" height="50"
                    src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg"
                    width="50">
                <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
            </div>
            <div class="search relative w-1/3">
                <form action="ProductList" method="get">
                    <input type="text" name="search" placeholder="Tìm kiếm"
                        class="p-2 border border-gray-300 rounded-lg pl-10">
                    <i class="fas fa-search absolute left-3 top-3 text-gray-400"></i>
                </form>
            </div>
            <div class="flex items-center space-x-6">
                <% String profileLink=session.getAttribute("user") !=null ? "profile.jsp"
                    : "SignInUp" ; %>
                    <a href="<%= profileLink %>">
                        <i class="fas fa-user text-gray-600 text-lg cursor-pointer"></i>
                    </a>
                    <i class="fas fa-heart text-gray-600 text-lg cursor-pointer"></i>
                    <a href="cart.jsp">
                        <i
                            class="fas fa-shopping-cart text-gray-600 text-lg cursor-pointer"></i>
                    </a>
            </div>
        </div>
    </header>

    <!-- Navigation -->
    <% List<Categories> categoriesList = (List<Categories>)
            request.getAttribute("categoriesList");
            %>
            <nav class="bg-gray-100 relative">
                <div class="container mx-auto flex items-center justify-between py-3">
                    <div class="relative">
                        <a class="text-gray-600 text-sm font-bold hover:text-black flex items-center space-x-2 cursor-pointer"
                            onclick="toggleDropdown()" aria-expanded="false" role="button">
                            <i class="fas fa-bars text-lg"></i>
                            <span>DANH MỤC</span>
                        </a>
                        <div id="dropdown"
                            class="hidden absolute left-0 mt-2 bg-white border border-gray-300 rounded shadow-lg w-40 transition-opacity duration-300 z-50">
                            <% if (categoriesList !=null && !categoriesList.isEmpty()) { for
                                (Categories category : categoriesList) { %>
                                <a href="ProductByCategory?categoryId=<%= category.getCategoryId() %>"
                                    class="block px-4 py-2 text-gray-600 hover:bg-gray-100">
                                    <%= category.getCname() %>
                                </a>
                                <% } } else { %>
                                    <span class="block px-4 py-2 text-gray-600">No
                                        categories available</span>
                                    <% } %>
                        </div>
                    </div>

                    <div class="flex justify-center space-x-8">
                        <a class="text-gray-600 text-sm font-bold hover:text-black"
                            href="about.jsp">GIỚI THIỆU</a>
                        <a class="text-gray-600 text-sm font-bold hover:text-black"
                            href="ProductList">SẢN PHẨM</a>
                        <a class="text-gray-600 text-sm font-bold hover:text-black"
                            href="CustomerOrder">ĐƠN HÀNG</a>
                        <a class="text-gray-600 text-sm font-bold hover:text-black"
                            href="contact.jsp">LIÊN HỆ</a>
                    </div>
                </div>
            </nav>

            <!-- Script -->
            <script>
                function toggleDropdown() {
                    const dropdown = document.getElementById("dropdown");
                    dropdown.classList.toggle("hidden");
                    document.querySelector('[aria-expanded]').setAttribute("aria-expanded", !dropdown.classList.contains("hidden"));
                }

                document.addEventListener("click", function (event) {
                    const dropdown = document.getElementById("dropdown");
                    if (!event.target.closest(".cursor-pointer")) {
                        dropdown.classList.add("hidden");
                        document.querySelector('[aria-expanded]').setAttribute("aria-expanded", false);
                    }
                });
            </script>

            <main class="container mx-auto py-8">
                <h1 class="text-3xl font-bold mb-2">New in</h1>
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    <% List<Products> productList = (List<Products>)
                            request.getAttribute("productList");
                            if (productList != null && !productList.isEmpty()) {
                            for (Products product : productList) {
                            %>
                            <div
                                class="product-card p-4 bg-white rounded shadow-md relative group">
                                <!-- Bọc hình ảnh và tên sản phẩm trong một thẻ <a> -->
                                <a href="productDetail.jsp?productId=<%= product.getProductId() %>"
                                    class="block">
                                    <img class="w-full h-80 object-cover rounded"
                                        src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>"
                                        alt="<%= product.getName() %>" loading="lazy" />
                                    <h3 class="text-lg font-bold text-center my-2">
                                        <%= product.getName() %>
                                    </h3>
                                </a>
                                <div class="flex justify-between items-center mb-4">
                                    <p class="text-green-600 font-semibold">Giá: <%=
                                            product.getPrice() %> VND</p>
                                    <p class="text-gray-500">Stock: <%= product.getStock()
                                            %>
                                    </p>
                                </div>
                                <form method="post" action="CartServlet">
                                    <input type="hidden" name="action" value="add" />
                                    <input type="hidden" name="userId"
                                        value="<%= session.getAttribute(" userId") %>" />
                                    <input type="hidden" name="productId"
                                        value="<%= product.getProductId() %>" />
                                    <input type="hidden" name="productName"
                                        value="<%= product.getName() %>">
                                    <input type="hidden" name="price"
                                        value="<%= product.getPrice() %>">
                                    <input type="hidden" name="productImage"
                                        value="<%= java.util.Base64.getEncoder().encodeToString(product.getImage()) %>">
                                    <input type="hidden" name="quantity" value="1" />
                                    <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 focus:ring-2 focus:ring-blue-500
absolute bottom-4 left-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity">
                                        Thêm vào giỏ hàng
                                    </button>
                                </form>
                            </div>
                            <% } // End of for loop } else { %>
                                <p class="col-span-full text-center text-gray-500">No
                                    products available.</p>
                                <% } // End of if condition %>
                </div>

                <!-- Pagination Controls -->

            </main>

            <footer class="bg-blue-500 text-white py-4">
                <div class="container mx-auto text-center">
                    <p>© 2024 Jellycat.</p>
                </div>
            </footer>
</body>

</html>