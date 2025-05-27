<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <title>Slideshow Example</title>
    <style>
        #slideshow {
            display: flex;
            transition: transform 0.5s ease-in-out;
        }
    </style>
</head>
<body class="font-roboto">
<!-- Slideshow Section -->
<div class="relative w-full max-w-6xl mx-auto mt-12 overflow-hidden">
    <!-- Slides -->
    <div id="slideshow" class="relative flex transition-transform duration-700 ease-in-out w-full">
        <!-- Slide 1 -->
        <div class="flex-shrink-0 w-full">
            <img class="w-full h-80 object-cover"
                 src="https://bloomingdales.ae/on/demandware.static/-/Library-Sites-BloomingDalesSharedLibrary/default/dwc73befc3/FINAL-IMAGES-24/BRAND-HEADERS/mobile-brand/WK50_24-BLM-JellyCat-PLP-Banner-MB.jpg"
                 alt="A colorful display of various Jellycat plush toys arranged in a playful manner">
        </div>
        <!-- Slide 2 -->
        <div class="flex-shrink-0 w-full">
            <img class="w-full h-80 object-cover"
                 src="https://mystyle.vn/wp-content/uploads/2023/10/easter-jellycat-1370x900_jpg.jpg"
                 alt="A cozy room setting with Jellycat plush toys placed on a shelf and a bed">
        </div>
        <!-- Slide 3 -->
        <div class="flex-shrink-0 w-full">
            <img class="w-full h-80 object-cover"
                 src="https://m.media-amazon.com/images/I/51WlI+cS1mL._AC_SY355_.jpg"
                 alt="A group of children playing with Jellycat plush toys in a park">
        </div>
    </div>

    <!-- Navigation Dots -->
    <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
        <button onclick="navigateSlide(0)" class="dot w-3 h-3 rounded-full bg-gray-300"></button>
        <button onclick="navigateSlide(1)" class="dot w-3 h-3 rounded-full bg-gray-300"></button>
        <button onclick="navigateSlide(2)" class="dot w-3 h-3 rounded-full bg-gray-300"></button>
    </div>

    <!-- Navigation Buttons -->
    <button id="prev" class="absolute left-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white px-4 py-2 rounded-l-md hover:bg-gray-600">
        &#8249; <!-- Left Arrow -->
    </button>
    <button id="next" class="absolute right-0 top-1/2 transform -translate-y-1/2 bg-gray-800 text-white px-4 py-2 rounded-r-md hover:bg-gray-600">
        &#8250; <!-- Right Arrow -->
    </button>
</div>
<!-- Link external JavaScript -->
<script src="js/script.js"></script>
</body>
</html>