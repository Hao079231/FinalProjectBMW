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

// Function to handle form submission with AJAX and CSRF token
function addCsrfTokenToForm(form) {
  // Try to get CSRF token from meta tag
  let csrfToken = '';
  const metaTag = document.querySelector('meta[name="csrf-token"]');

  if (metaTag) {
    csrfToken = metaTag.getAttribute('content');
  } else {
    console.warn('CSRF meta tag not found, trying to get from hidden inputs');
    // Thử lấy từ hidden input nếu có
    const tokenInput = document.querySelector('input[name="csrfToken"]');
    if (tokenInput) {
      csrfToken = tokenInput.value;
    }
  }

  form.addEventListener('submit', function (event) {
    // Only process if not already submitting
    if (form.dataset.processing === 'true') {
      return;
    }

    // Prevent default submission temporarily
    event.preventDefault();
    form.dataset.processing = 'true';

    try {
      // Kiểm tra xem form có cần submit trực tiếp không
      if (shouldSubmitDirectly(form)) {
        // Thêm CSRF token vào form nếu chưa có
        let tokenInput = form.querySelector('input[name="csrfToken"]');
        if (!tokenInput && csrfToken) {
          tokenInput = document.createElement('input');
          tokenInput.type = 'hidden';
          tokenInput.name = 'csrfToken';
          tokenInput.value = csrfToken;
          form.appendChild(tokenInput);
        }

        // Submit form trực tiếp không qua AJAX
        console.log('Submitting form directly:', form.action);
        form.dataset.processing = 'false';
        setTimeout(function () {
          form.submit();
        }, 10);
        return;
      }

      // AJAX submission cho các form thông thường - Phần này hiện tại không được sử dụng
      const formData = new FormData(form);

      // Thêm CSRF token vào formData
      if (csrfToken && !formData.has('csrfToken')) {
        formData.append('csrfToken', csrfToken);
      }

      // Tạo và cấu hình AJAX request
      const xhr = new XMLHttpRequest();
      xhr.open('POST', form.action, true);

      // Thêm header CSRF để server có thể xác thực
      if (csrfToken) {
        xhr.setRequestHeader('X-CSRF-Token', csrfToken);
      }

      // Đánh dấu là AJAX request
      xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');

      // Xử lý response
      xhr.onload = function () {
        form.dataset.processing = 'false';

        if (xhr.status >= 200 && xhr.status < 300) {
          // Success handling
          const successToast = document.getElementById('success-toast');
          if (successToast) {
            successToast.style.display = 'block';
            const successMessage = successToast.querySelector('span');
            if (successMessage) {
              successMessage.textContent = 'Thao tác thành công';
            }
          }

          // Xử lý response HTML (redirect)
          if (xhr.responseText.includes('<!DOCTYPE html>') ||
            xhr.responseText.includes('<html>')) {
            document.open();
            document.write(xhr.responseText);
            document.close();
            return;
          }

          // Xử lý JSON response
          try {
            const response = JSON.parse(xhr.responseText);
            if (response.redirect) {
              window.location.href = response.redirect;
              return;
            }

            if (response.message) {
              if (successToast) {
                const successMessage = successToast.querySelector('span');
                if (successMessage) {
                  successMessage.textContent = response.message;
                }
              }
            }
          } catch (e) {
            // Không phải JSON hoặc không có redirect
            console.log('Response is not JSON or has no redirect info');
          }

          // Mặc định: reload trang sau khi thành công
          setTimeout(function () {
            window.location.reload();
          }, 1000);
        } else {
          // Error handling
          console.error('Form submission failed:', xhr.status, xhr.statusText);
          const errorToast = document.getElementById('error-toast');

          if (errorToast) {
            errorToast.style.display = 'block';
            const errorMessage = errorToast.querySelector('span');
            if (errorMessage) {
              let errorText = 'Error ' + xhr.status + ': ' + (xhr.statusText || 'Form submission failed');

              try {
                // Kiểm tra xem response có phải JSON không
                const response = JSON.parse(xhr.responseText);
                if (response.error) {
                  errorText = response.error;
                }
              } catch (e) {
                // Không phải JSON, giữ nguyên message mặc định
              }

              errorMessage.textContent = errorText;
            }
          } else {
            // Fallback error display
            alert('Error ' + xhr.status + ': ' + (xhr.statusText || 'Form submission failed'));
          }
        }
      };

      // Xử lý lỗi mạng
      xhr.onerror = function () {
        form.dataset.processing = 'false';
        console.error('Network error during form submission');

        const errorToast = document.getElementById('error-toast');
        if (errorToast) {
          errorToast.style.display = 'block';
          const errorMessage = errorToast.querySelector('span');
          if (errorMessage) {
            errorMessage.textContent = 'Lỗi kết nối mạng khi gửi form';
          }
        } else {
          // Fallback error display
          alert('Lỗi kết nối mạng khi gửi form');
        }

        // Sau lỗi mạng, thử submit trực tiếp
        setTimeout(function () {
          if (confirm('Thử lại bằng cách gửi form trực tiếp?')) {
            // Thêm CSRF token nếu cần
            let tokenInput = form.querySelector('input[name="csrfToken"]');
            if (!tokenInput && csrfToken) {
              tokenInput = document.createElement('input');
              tokenInput.type = 'hidden';
              tokenInput.name = 'csrfToken';
              tokenInput.value = csrfToken;
              form.appendChild(tokenInput);
            }
            form.submit();
          }
        }, 1000);
      };

      // Gửi form data
      xhr.send(formData);
    } catch (error) {
      console.error('Error in form submission:', error);
      form.dataset.processing = 'false';

      // Fallback to normal form submission in case of errors
      let tokenInput = form.querySelector('input[name="csrfToken"]');
      if (!tokenInput && csrfToken) {
        tokenInput = document.createElement('input');
        tokenInput.type = 'hidden';
        tokenInput.name = 'csrfToken';
        tokenInput.value = csrfToken;
        form.appendChild(tokenInput);
      }

      // Submit trực tiếp không cần xác nhận khi có lỗi
      form.submit();
    }
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
    // Chỉ áp dụng cho các form có method POST
    if (form.method.toLowerCase() === 'post') {
      // Kiểm tra xem form có data-no-csrf attribute không 
      if (!form.hasAttribute('data-no-csrf')) {
        addCsrfTokenToForm(form);
      }
    }
  });
}); 