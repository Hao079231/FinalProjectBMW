let currentSlide = 0;
const dots = document.querySelectorAll(".dot"); // Lấy tất cả các dot

function updateDots() {
    dots.forEach((dot, index) => {
        if (index === currentSlide) {
            dot.classList.remove("bg-gray-300");
            dot.classList.add("bg-gray-800");
        } else {
            dot.classList.remove("bg-gray-800");
            dot.classList.add("bg-gray-300");
        }
    });
}

function navigateSlide(index) {
    const slideshow = document.getElementById("slideshow");
    if (!slideshow) {
        console.error("Slideshow element not found!");
        return;
    }

    const slides = slideshow.children;
    const totalSlides = slides.length;

    if (index < 0) {
        currentSlide = totalSlides - 1;
    } else if (index >= totalSlides) {
        currentSlide = 0;
    } else {
        currentSlide = index;
    }

    const slideWidth = slides[0].clientWidth;
    slideshow.style.transform = `translateX(-${currentSlide * slideWidth}px)`;
    updateDots(); // Cập nhật trạng thái dots
}

// Khởi tạo trạng thái dots khi trang tải
updateDots();

// Previous Button
const prevButton = document.getElementById("prev");
if (prevButton) {
    prevButton.addEventListener("click", () => {
        navigateSlide(currentSlide - 1);
    });
}

// Next Button
const nextButton = document.getElementById("next");
if (nextButton) {
    nextButton.addEventListener("click", () => {
        navigateSlide(currentSlide + 1);
    });
}

// Tự động chuyển slide mỗi 5 giây
setInterval(() => {
    navigateSlide(currentSlide + 1);
}, 5000);