<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ include file="components/common_css_js.jsp" %>
    <%@ include file="components/navbar.jsp" %>
      <!DOCTYPE html>
      <html>

      <head>
        <meta charset="UTF-8">
        <title>Quên mật khẩu</title>
        <meta name="csrf-token" content="${sessionScope.csrfToken}">
        <script src="${pageContext.request.contextPath}/js/csrf-protection.js"></script>
      </head>

      <body>
        <div class="container">
          <div class="form-container">
            <div class="card">
              <div class="card-header text-center">
                <h3>Quên mật khẩu</h3>
              </div>
              <div class="card-body">
                <%@ include file="components/message.jsp" %>
                  <form action="ChangePasswordServlet" method="post" class="needs-validation" novalidate
                    data-no-csrf="true">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                    <div class="form-group mb-3">
                      <label for="email" class="form-label">Email:</label>
                      <input type="email" class="form-control" id="email" name="email" required
                        pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
                      <div class="invalid-feedback">
                        Vui lòng nhập địa chỉ email hợp lệ.
                      </div>
                      <small class="form-text text-muted">Nhập email đã đăng ký để nhận mã OTP.</small>
                    </div>
                    <div class="d-grid gap-2">
                      <button type="submit" class="btn btn-primary">Gửi mã OTP</button>
                      <a href="signinup.jsp" class="btn btn-outline-primary">Quay lại đăng nhập</a>
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
        </script>
      </body>

      </html>