<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
   <meta charset="UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <script src="https://cdn.tailwindcss.com"></script>
   <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
   <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
   <title>Giới Thiệu - Cửa Hàng Jellycat</title>
</head>
<body class="font-roboto bg-gray-100">
   <!-- Header -->
   <header class="bg-white shadow">
       <div class="container mx-auto px-4 py-6 flex justify-between items-center">
           <div class="text-2xl font-bold text-gray-800">
               Jellycat Store
           </div>
           <nav class="space-x-4">
               <a class="text-gray-600 hover:text-gray-800" href="ProductList">Trang Chủ</a>
           </nav>
       </div>
   </header>
   <!-- About Section -->
   <section class="bg-white py-12">
       <div class="container mx-auto px-6">
           <h1 class="text-3xl font-bold text-center text-gray-800 mb-6">Về Chúng Tôi</h1>
           <img src="https://cdn11.bigcommerce.com/s-8zeylxlay7/images/stencil/original/image-manager/jellycat-jack-25-years-our-story-5000-1600.jpg?t=1707760524"
               alt="Giới thiệu cửa hàng gấu bông với hình ảnh các loại gấu bông dễ thương được trưng bày"
               class="w-full mb-8 rounded-lg shadow-lg" />
           <div class="text-center">
               <h2 class="text-2xl font-semibold mb-4">Khởi Đầu Từ Niềm Đam Mê</h2>
               <p class="text-gray-600 mb-4">
                   Jellycat Store ra đời với mong muốn mang đến cho khách hàng những sản phẩm chất lượng cao cùng trải nghiệm mua sắm thân thiện, hiện đại. Là một cửa hàng mới mở, chúng tôi luôn nỗ lực để xây dựng uy tín và niềm tin với từng khách hàng.
               </p>
               <p class="text-gray-600 mb-4">
                   Sản phẩm của chúng tôi được chọn lọc cẩn thận để đảm bảo phù hợp với nhu cầu và sở thích đa dạng của bạn. Jellycat Store hy vọng trở thành người bạn đồng hành đáng tin cậy trong cuộc sống hàng ngày của bạn.
               </p>
               <p class="text-gray-600">
                   Hãy khám phá và trải nghiệm mua sắm cùng chúng tôi ngay hôm nay!
                   <a  href="ProductList" class="text-blue-500 font-bold hover:underline">Xem thêm sản phẩm</a>
               </p>
           </div>
       </div>
       <hr class="border-t-2 border-gray-300 w-1/2 mx-auto mt-6">
   </section>
  
   <section class="bg-white py-12">
       <div class="container mx-auto px-6 text-center">
           <div class="relative">
               <div class="absolute top-0 left-6 text-orange-500 text-3xl font-bold">“</div>
           </div>
           <h2 class="text-xl font-bold text-gray-800 mb-4">Tại Sao Chọn Chúng Tôi?</h2>
           <p class="text-gray-600 mb-4">
               Từ sự tận tâm trong khâu lựa chọn sản phẩm đến dịch vụ khách hàng chuyên nghiệp, chúng tôi luôn đặt bạn là trọng tâm của mọi hoạt động.
               <br />
               <span class="text-gray-600 mb-4">Hạnh phúc của bạn chính là động lực của chúng tôi.</span>
           </p>
           <div class="relative">
               <div class="absolute bottom-0 right-6 text-orange-500 text-3xl font-bold">”</div>
           </div>
           <hr class="border-t-2 border-gray-300 w-1/2 mx-auto mt-6">
       </div>
   </section>
  
   <section class="bg-gray-100 py-16 mb-8">
       <div class="container mx-auto px-6 flex flex-col md:flex-row items-center md:items-start">
           <div class="md:w-1/2 text-center md:text-left md:pr-8">
               <h2 class="text-2xl font-bold text-gray-700">Câu Chuyện Của Chúng Tôi</h2>
               <p class="text-gray-600 mb-4 leading-relaxed">
                   Jellycat Store được sáng lập bởi những con người trẻ tràn đầy nhiệt huyết, mong muốn xây dựng một thương hiệu đáng tin cậy. Mỗi sản phẩm tại cửa hàng đều được kiểm tra kỹ lưỡng, mang đến sự hài lòng và giá trị cho khách hàng.
                   <br><br>
                   Với phương châm "Khách hàng là trọng tâm", chúng tôi không ngừng hoàn thiện và phát triển để phục vụ bạn tốt nhất. Hãy đồng hành cùng Jellycat Store trên hành trình này!
               </p>
               
           </div>
           <div class="md:w-1/2 mt-8 md:mt-0">
               <img src="https://storage.googleapis.com/a1aa/image/2RHzJLcVQt6LGZIYkfWubWbLFRPbeaQRe1kZOH3zaR5OYIwnA.jpg" alt="Hình ảnh cửa hàng gấu bông với các loại gấu bông được trưng bày đẹp mắt" class="rounded-lg shadow-lg">
           </div>
       </div>
   </section>
             
   <!-- Footer -->
   <footer class="bg-blue-500 text-white py-4">
       <div class="container mx-auto text-center">
           <p>&copy; 2024 Jellycat Store</p>
       </div>
   </footer>
</body>
</html>