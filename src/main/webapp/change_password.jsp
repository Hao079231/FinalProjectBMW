<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ include file="components/common_css_js.jsp" %>
    <%@ include file="components/navbar.jsp" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8">
        <title>Đổi mật khẩu</title>
        <!-- CSRF Token để JavaScript có thể truy cập -->
        <meta name="csrf-token" content="${sessionScope.csrfToken}">
        <!-- Nhúng script bảo vệ CSRF -->
        <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
        <style>
          .form-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
          }

          .alert {
            margin-bottom: 15px;
          }

          .password-requirements {
            font-size: 0.85em;
            color: #6c757d;
            margin-top: 10px;
          }

          .requirement-met {
            color: #198754;
          }

          .requirement-not-met {
            color: #dc3545;
          }
        </style>
      </head>

      <body>
        <div class="container">
          <div class="form-container">
            <div class="card">
              <div class="card-header text-center">
                <h3>Đổi mật khẩu mới</h3>
              </div>
              <div class="card-body">
                <%@ include file="components/message.jsp" %>
                  <form action="ChangePasswordServlet" method="post" class="needs-validation" novalidate
                    data-no-csrf="true">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                    <div class="form-group mb-3">
                      <label for="password" class="form-label">Mật khẩu mới:</label>
                      <input type="password" class="form-control" id="password" name="password" required minlength="8"
                        pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$">
                      <div class="invalid-feedback">
                        Mật khẩu không đáp ứng yêu cầu.
                      </div>
                      <div class="password-requirements">
                        Mật khẩu phải có:
                        <ul>
                          <li id="length">Ít nhất 8 ký tự</li>
                          <li id="uppercase">Ít nhất 1 chữ in hoa</li>
                          <li id="lowercase">Ít nhất 1 chữ thường</li>
                          <li id="number">Ít nhất 1 số</li>
                          <li id="special">Ít nhất 1 ký tự đặc biệt (@$!%*?&)</li>
                        </ul>
                      </div>
                    </div>
                    <div class="form-group mb-3">
                      <label for="confirm_password" class="form-label">Xác nhận mật khẩu:</label>
                      <input type="password" class="form-control" id="confirm_password" name="confirm_password"
                        required>
                      <div class="invalid-feedback">
                        Mật khẩu xác nhận không khớp.
                      </div>
                    </div>
                    <div class="d-grid gap-2">
                      <button type="submit" class="btn btn-primary" id="submitBtn" disabled>Đổi mật khẩu</button>
                      <a href="signinup.jsp" class="btn btn-outline-primary">Quay lại đăng nhập</a>
                    </div>
                  </form>
              </div>
            </div>
          </div>
        </div>

        <script>
          // Password validation
          const password = document.getElementById('password');
          const confirmPassword = document.getElementById('confirm_password');
          const submitBtn = document.getElementById('submitBtn');
          const requirements = {
            length: /.{8,}/,
            uppercase: /[A-Z]/,
            lowercase: /[a-z]/,
            number: /[0-9]/,
            special: /[@$!%*?&]/
          };

          function validatePassword() {
            const pwd = password.value;
            let valid = true;

            // Check each requirement
            for (let req in requirements) {
              const element = document.getElementById(req);
              if (requirements[req].test(pwd)) {
                element.classList.add('requirement-met');
                element.classList.remove('requirement-not-met');
              } else {
                element.classList.add('requirement-not-met');
                element.classList.remove('requirement-met');
                valid = false;
              }
            }

            // Check if passwords match
            if (pwd === confirmPassword.value && pwd !== '') {
              confirmPassword.setCustomValidity('');
            } else {
              confirmPassword.setCustomValidity('Mật khẩu không khớp');
              valid = false;
            }

            submitBtn.disabled = !valid;
            return valid;
          }

          password.addEventListener('input', validatePassword);
          confirmPassword.addEventListener('input', validatePassword);

          // Form validation
          (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms)
              .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                  if (!form.checkValidity() || !validatePassword()) {
                    event.preventDefault()
                    event.stopPropagation()
                  }
                  form.classList.add('was-validated')
                }, false)
              })
          })()
        </script>
      </body>

      </html>