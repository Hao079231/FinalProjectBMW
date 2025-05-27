<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ include file="components/common_css_js.jsp" %>
    <%@ include file="components/navbar.jsp" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8">
        <title>Xác nhận OTP</title>
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

          .otp-input {
            letter-spacing: 2px;
            text-align: center;
            font-size: 1.2em;
          }
        </style>
      </head>

      <body>
        <div class="container">
          <div class="form-container">
            <div class="card">
              <div class="card-header text-center">
                <h3>Nhập mã OTP</h3>
              </div>
              <div class="card-body">
                <%@ include file="components/message.jsp" %>
                  <form action="ChangePasswordServlet" method="post" class="needs-validation" novalidate
                    data-no-csrf="true">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                    <div class="form-group mb-3">
                      <label for="code" class="form-label">Mã OTP:</label>
                      <input type="text" class="form-control otp-input" id="code" name="code" required minlength="5"
                        maxlength="5" pattern="[0-9]{5}" placeholder="Nhập mã 5 số">
                      <div class="invalid-feedback">
                        Vui lòng nhập mã OTP 5 số.
                      </div>
                      <small class="form-text text-muted">Nhập mã OTP đã được gửi đến email của bạn.</small>
                    </div>
                    <div class="d-grid gap-2">
                      <button type="submit" class="btn btn-primary">Xác nhận</button>
                      <a href="forgot_password.jsp" class="btn btn-outline-primary">Quay lại</a>
                    </div>
                  </form>
              </div>
            </div>
          </div>
        </div>

        <script>
          // Form validation
          (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms)
              .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                  if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                  }
                  form.classList.add('was-validated')
                }, false)
              })
          })()

          // Auto format OTP input
          document.getElementById('code').addEventListener('input', function (e) {
            this.value = this.value.replace(/[^0-9]/g, '');
          });
        </script>
      </body>

      </html>