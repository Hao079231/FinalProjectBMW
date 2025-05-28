// Hàm đóng thông báo
function closeToast(id) {
    var toast = document.getElementById(id);
    if (toast) {
        toast.style.display = 'none';
    }
}

// Tự động ẩn thông báo sau 5 giây
setTimeout(function () {
    var successToast = document.getElementById('success-toast');
    var errorToast = document.getElementById('error-toast');
    if (successToast) successToast.style.display = 'none';
    if (errorToast) errorToast.style.display = 'none';
}, 5000);

// Danh sách form gửi trực tiếp
const directSubmitForms = [
    'SignInUp', 'addProduct', 'editProduct', 'CartServlet', 'PayOrder', 'CustomerOrder'
];

function shouldSubmitDirectly(form) {
    return true; // Giữ gửi trực tiếp
}

// Hàm lấy token CSRF
function getCsrfToken() {
    let tokenInput = document.getElementById('csrfTokenHash');
    if (tokenInput) {
        console.log('Tìm thấy CSRF Token trong DOM:', tokenInput.value);
        return tokenInput.value;
    }

    tokenInput = document.querySelector('input[name="csrfTokenHash"]');
    if (tokenInput) {
        console.log('Tìm thấy CSRF Token trong form:', tokenInput.value);
        return tokenInput.value;
    }

    console.error('Không tìm thấy CSRF token trong DOM!');
    return '';
}

// Xử lý gửi form với token CSRF
function addCsrfTokenToForm(form) {
    form.addEventListener('submit', function (event) {
        event.preventDefault();

        // Kiểm tra page-info.jsp đã tải
        if (!document.getElementById('csrfTokenHash')) {
            console.error('Không tìm thấy input CSRF token trong DOM khi gửi form');
        }

        let csrfToken = getCsrfToken();
        let tokenInput = form.querySelector('input[name="csrfTokenHash"]');
        if (!tokenInput && csrfToken) {
            tokenInput = document.createElement('input');
            tokenInput.type = 'hidden';
            tokenInput.name = 'csrfTokenHash';
            tokenInput.value = csrfToken;
            form.appendChild(tokenInput);
            console.log('Đã thêm CSRF token vào form:', csrfToken);
        } else if (tokenInput) {
            tokenInput.value = csrfToken;
            console.log('Đã cập nhật CSRF token trong form:', csrfToken);
        } else {
            console.error('Không thể thêm CSRF token vào form');
        }

        form.submit();
    });
}

// Đảm bảo DOM tải xong trước khi xử lý form
document.addEventListener('DOMContentLoaded', function () {
    const forms = document.querySelectorAll('form');

    // Xử lý nút đóng thông báo
    const closeButtons = document.querySelectorAll('[onclick*="closeToast"]');
    closeButtons.forEach(button => {
        if (button.getAttribute('onclick') && button.getAttribute('onclick').match(/closeToast\(['"](.+)['"]\)/)) {
            const toastId = button.getAttribute('onclick').match(/closeToast\(['"](.+)['"]\)/)[1];
            button.onclick = function () { closeToast(toastId); };
        }
    });

    // Áp dụng bảo vệ CSRF cho form
    forms.forEach(function (form) {
        if (form.method.toLowerCase() === 'post' || form.method.toLowerCase() === 'get') {
            if (!form.hasAttribute('data-no-csrf')) {
                addCsrfTokenToForm(form);
            }
        }
    });
});