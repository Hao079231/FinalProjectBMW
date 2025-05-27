package Beans;

import jakarta.mail.MessagingException;

public class MailMessenger {
  public static boolean sendOtp(String userEmail, int code) throws MessagingException {
    String subject = "Mã xác nhận thay đổi mật khẩu";
    String body = String.format("""
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
            <h2 style="color: #333;">Xác nhận thay đổi mật khẩu</h2>
            <p>Xin chào,</p>
            <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>
            <p>Vui lòng sử dụng mã xác minh bên dưới để đặt lại mật khẩu:</p>
            <div style="background-color: #f5f5f5; padding: 15px; text-align: center; margin: 20px 0;">
                <h2 style="color: #007bff; margin: 0;">%d</h2>
            </div>
            <p>Mã này sẽ hết hạn sau 10 phút.</p>
            <p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>
            <p>Trân trọng,<br>Ban quản trị</p>
        </div>
        """, code);

    return Mail.sendMail(userEmail, subject, body);
  }
}
