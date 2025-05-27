<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
 <meta charset="utf-8"/>
 <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
 <title>
  Logout
 </title>
 <script src="https://cdn.tailwindcss.com">
 </script>
 <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
 <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&amp;display=swap" rel="stylesheet"/>
 <style>
  body {
           font-family: 'Roboto', sans-serif;
       }
 </style>
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
 <div class="bg-white p-8 rounded-lg shadow-lg max-w-md w-full">
  <div class="text-center mb-6">
   <img alt="Jellycat logo featuring a cute, smiling jellycat character with a whimsical design" class="mx-auto mb-4" height="100" src="https://storage.googleapis.com/a1aa/image/fSeQHmsTyGqcO0fjr8e2Bg4Y0kbzWebJgeBg6FnFJzo1BJm9E.jpg" width="100"/>
   <h1 class="text-2xl font-bold text-gray-800">
    You have been logged out
   </h1>
  </div>
  <p class="text-gray-600 mb-6 text-center">
   Thank you for using our service. We hope to see you again soon.
  </p>
  <div class="flex justify-center">
   <a class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition duration-200" href="signinup.jsp">
    <i class="fas fa-sign-in-alt mr-2">
    </i>
    Login Again
   </a>
  </div>
 </div>
</body>
</html>
