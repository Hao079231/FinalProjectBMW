package Servlet;

import Beans.DBConnection;
import Beans.MailMessenger;
import Beans.Message;
import Utils.DBUsers;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class ChangePasswordServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final int OTP_EXPIRY_MINUTES = 10;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String referrer = request.getHeader("referer");
    HttpSession session = request.getSession();

    try (Connection conn = DBConnection.getConnection()) {
      if (referrer.contains("forgot_password")) {
        handleForgotPassword(request, response, session, conn);
      } else if (referrer.contains("otp_code")) {
        handleOtpVerification(request, response, session);
      } else if (referrer.contains("change_password")) {
        handlePasswordChange(request, response, session, conn);
      }
    } catch (Exception e) {
      e.printStackTrace();
      session.setAttribute("message", new Message("Đã xảy ra lỗi: " + e.getMessage(), "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
    }
  }

  private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response,
      HttpSession session, Connection conn)
      throws IOException, MessagingException {
    String email = request.getParameter("email").trim();
    List<String> emails = DBUsers.getAllEmail(conn);

    if (!emails.contains(email)) {
      session.setAttribute("message", new Message("Email không tồn tại! Thử lại với Email khác!", "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
      return;
    }

    Random rand = new Random();
    int otp = rand.nextInt(90000) + 10000; // 5-digit OTP

    if (MailMessenger.sendOtp(email, otp)) {
      session.setAttribute("otp", otp);
      session.setAttribute("email", email);
      session.setAttribute("otpTimestamp", LocalDateTime.now());
      session.setAttribute("message", new Message("Chúng tôi đã gửi mã đặt lại mật khẩu tới " + email, "success", "alert-success"));
      response.sendRedirect("otp_code.jsp");
    } else {
      session.setAttribute("message", new Message("Không thể gửi email. Vui lòng thử lại sau!", "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
    }
  }

  private void handleOtpVerification(HttpServletRequest request, HttpServletResponse response,
      HttpSession session) throws IOException {
    Integer storedOtp = (Integer) session.getAttribute("otp");
    LocalDateTime otpTimestamp = (LocalDateTime) session.getAttribute("otpTimestamp");

    if (storedOtp == null || otpTimestamp == null) {
      session.setAttribute("message", new Message("Phiên làm việc đã hết hạn. Vui lòng thử lại!", "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
      return;
    }

    if (otpTimestamp.plusMinutes(OTP_EXPIRY_MINUTES).isBefore(LocalDateTime.now())) {
      session.removeAttribute("otp");
      session.removeAttribute("otpTimestamp");
      session.setAttribute("message", new Message("Mã OTP đã hết hạn. Vui lòng thử lại!", "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
      return;
    }

    int submittedOtp = Integer.parseInt(request.getParameter("code"));
    if (submittedOtp == storedOtp) {
      session.removeAttribute("otp");
      session.removeAttribute("otpTimestamp");
      response.sendRedirect("change_password.jsp");
    } else {
      session.setAttribute("message", new Message("Mã xác minh không hợp lệ!", "error", "alert-danger"));
      response.sendRedirect("otp_code.jsp");
    }
  }

  private void handlePasswordChange(HttpServletRequest request, HttpServletResponse response,
      HttpSession session, Connection conn) throws IOException {
    String email = (String) session.getAttribute("email");
    if (email == null) {
      session.setAttribute("message", new Message("Phiên làm việc đã hết hạn!", "error", "alert-danger"));
      response.sendRedirect("forgot_password.jsp");
      return;
    }

    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirm_password");

    if (!password.equals(confirmPassword)) {
      session.setAttribute("message", new Message("Mật khẩu không khớp!", "error", "alert-danger"));
      response.sendRedirect("change_password.jsp");
      return;
    }

    DBUsers.updateUserPasswordByEmail(conn, password, email);
    session.removeAttribute("email");
    session.setAttribute("message", new Message("Mật khẩu đã được cập nhật thành công!", "success", "alert-success"));
    response.sendRedirect("signinup.jsp");
  }
}
