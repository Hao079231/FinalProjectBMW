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

// Danh sách các form cần xử lý trực tiếp (không qua AJAX)
const directSubmitForms = [
  'SignInUp',     // Form đăng nhập/đăng ký
  'addProduct',   // Form thêm sản phẩm
  'editProduct',  // Form sửa sản phẩm
  'CartServlet',  // Form giỏ hàng
  'PayOrder',     // Form thanh toán
  'CustomerOrder' // Form đơn hàng
];

// Function to check if a form should be submitted directly
function shouldSubmitDirectly(form) {
  // Luôn sử dụng submit trực tiếp - sửa lỗi "Đã xảy ra lỗi khi xử lý form"
  return true;

  /* Disable AJAX submission temporarily
  // Kiểm tra dựa trên action URL
  for (const keyword of directSubmitForms) {
    if (form.action.includes(keyword)) {
      return true;
    }
  }
  
  // Kiểm tra form có input file
  if (form.querySelector('input[type="file"]')) {
    return true;
  }
  
  // Form có enctype="multipart/form-data"
  if (form.enctype === 'multipart/form-data') {
    return true;
  }
  
  // Các form đăng nhập/đăng ký đặc biệt
  if (form.querySelector('input[name="action"][value="signin"]') ||
      form.querySelector('input[name="action"][value="signup"]')) {
    return true;
  }
  
  return false;
  */
}

// Hàm lấy CSRF token đã mã hóa từ hidden input hoặc component chung
function getCsrfToken() {
  // Ưu tiên lấy theo id (nếu dùng page-info.jsp)
  let tokenInput = document.getElementById('csrfTokenHash');
  if (tokenInput) return tokenInput.value;

  // Nếu không có, thử lấy theo name trong form đầu tiên
  tokenInput = document.querySelector('input[name="csrfTokenHash"]');
  if (tokenInput) return tokenInput.value;

  // Nếu không tìm thấy, cảnh báo
  console.warn('CSRF token not found in DOM!');
  return '';
}

// Function to handle form submission with CSRF token
function addCsrfTokenToForm(form) {
  form.addEventListener('submit', function (event) {
    // Prevent default submission temporarily
    event.preventDefault();

    // Thêm CSRF token vào form nếu chưa có
    let csrfToken = getCsrfToken();
    let tokenInput = form.querySelector('input[name="csrfTokenHash"]');
    if (!tokenInput && csrfToken) {
      tokenInput = document.createElement('input');
      tokenInput.type = 'hidden';
      tokenInput.name = 'csrfTokenHash';
      tokenInput.value = csrfToken;
      form.appendChild(tokenInput);
    } else if (tokenInput) {
      tokenInput.value = csrfToken;
    }

    // Submit form
    form.submit();
  });
}

// Tự động áp dụng CSRF protection cho tất cả các form trên trang
document.addEventListener('DOMContentLoaded', function () {
  // Lấy tất cả các form trên trang
  const forms = document.querySelectorAll('form');

  // Thêm sự kiện closeToast cho tất cả các nút đóng toast
  const closeButtons = document.querySelectorAll('[onclick*="closeToast"]');
  closeButtons.forEach(button => {
    if (button.getAttribute('onclick') && button.getAttribute('onclick').match(/closeToast\(['"](.+)['"]\)/)) {
      const toastId = button.getAttribute('onclick').match(/closeToast\(['"](.+)['"]\)/)[1];
      button.onclick = function () { closeToast(toastId); };
    }
  });

  // Áp dụng xử lý CSRF cho mỗi form
  forms.forEach(function (form) {
    // Chỉ áp dụng cho các form có method POST hoặc GET
    if (form.method.toLowerCase() === 'post' || form.method.toLowerCase() === 'get') {
      // Kiểm tra xem form có data-no-csrf attribute không 
      if (!form.hasAttribute('data-no-csrf')) {
        addCsrfTokenToForm(form);
      }
    }
  });
}); 