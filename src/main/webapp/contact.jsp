<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <title>Jellycat</title>
</head>
<body class="font-roboto bg-gray-100">
    <!-- Header -->
    <header class="border-b bg-white shadow-sm">
        <div class="container mx-auto flex justify-between items-center py-4 px-6">
            <!-- Logo -->
            <div class="logo flex items-center">
                <img alt="Jellycat logo" class="mr-4" height="50" src="https://storage.googleapis.com/a1aa/image/FQHw3eMxpk2eakMv3O1MJ3DrSguzwJk8FAGfLnYFr6mlCFlnA.jpg" width="50">
                <span class="text-2xl font-bold text-blue-500">JELLYCAT</span>
            </div>
            <!-- Icons -->
            <div class="flex items-center space-x-6">
                <%
                    // Kiểm tra xem người dùng đã đăng nhập hay chưa
                    String profileLink;
                    if (session.getAttribute("user") != null) {
                        // Nếu đã đăng nhập, dẫn đến profile.jsp
                        profileLink = "profile.jsp";
                    } else {
                        // Nếu chưa đăng nhập, dẫn đến signinup.jsp
                        profileLink = "signinup.jsp";
                    }
                %>
                <a href="<%= profileLink %>">
                    <i class="fas fa-user text-gray-600 text-lg cursor-pointer"></i>
                </a>
                <i class="fas fa-heart text-gray-600 text-lg cursor-pointer"></i>
                <a href="cart.jsp"> 
                    <i class="fas fa-shopping-cart text-gray-600 text-lg cursor-pointer"></i>
                </a>
            </div>
        </div>
    </header>

    <!-- Navigation -->
    <nav class="bg-gray-100">
        <div class="container mx-auto flex items-center justify-between py-3">
            <!-- Các mục còn lại ở chính giữa -->
             <div class="flex justify-center space-x-8">
                <a class="text-gray-600 text-sm font-bold hover:text-black" href="about.jsp">GIỚI THIỆU</a>
                <a class="text-gray-600 text-sm font-bold hover:text-black" href="ProductList">SẢN PHẨM</a>
                <a class="text-gray-600 text-sm font-bold hover:text-black" href="CustomerOrder">ĐƠN HÀNG</a>
                <a class="text-gray-600 text-sm font-bold hover:text-black" href="contact.jsp">LIÊN HỆ</a>
            </div>
        </div>
    </nav>

    <!-- Contact Section -->
    <div class="container mx-auto p-6">
		<div class="bg-white shadow-md rounded-lg p-6">
    		<h1 class="text-3xl font-bold mb-6 text-center text-blue-500">
     			Liên Hệ
    		</h1>
    	<div class="flex flex-col md:flex-row items-center mb-6">
     		<div class="md:w-1/2 mb-6 md:mb-0">
      			<img alt="A cute cartoon teddy bear holding a heart" class="rounded-lg shadow-md" height="400" src="https://storage.googleapis.com/a1aa/image/2RHzJLcVQt6LGZIYkfWubWbLFRPbeaQRe1kZOH3zaR5OYIwnA.jpg" width="400"/>
     		</div>
		<div class="md:w-1/2 md:pl-6">
      		<div class="mb-6 flex items-center">
		    <h2 class="text-xl font-semibold text-gray-800 mr-4">
		        Tên Shop
		    </h2>
		    <p class="text-2xl font-bold text-blue-500">
		    	JELLYCAT
		    </p>
			</div>
		<div class="mb-6">
       		<h2 class="text-xl font-semibold text-gray-800">
        		Số Điện Thoại
       		</h2>
       	<p class="text-gray-700">
        	<i class="fas fa-phone-alt text-blue-500">
        	</i>
        		0386215152
       	</p>
      	</div>
      	<div class="mb-6">
       		<h2 class="text-xl font-semibold text-gray-800">
        		Email
       		</h2>
       		<p class="text-gray-700">
        	<i class="fas fa-envelope text-blue-500">
        	</i>
        		HongSang@gmail.com
       		</p>
      	</div>
      	<div class="mb-6">
       		<h2 class="text-xl font-semibold text-gray-800">
        		Địa Chỉ
       		</h2>
       		<p class="text-gray-700">
        	<i class="fas fa-map-marker-alt text-blue-500">
        	</i>
        		123 Đường Thành Đạt, Quận 9, TP. Hồ Chí Minh, Việt Nam
       		</p>
      	</div>
     </div>
    </div>
   </div>
  </div>
  <script>
   function toggleDropdown() {
            var dropdown = document.getElementById('dropdown');
            dropdown.classList.toggle('hidden');
        }
   </script>
</body>
</html>