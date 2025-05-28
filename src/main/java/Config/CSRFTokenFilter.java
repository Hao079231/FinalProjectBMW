package Config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFTokenFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(CSRFTokenFilter.class);
  private static final String CSRF_TOKEN_NAME = "csrfToken";
  private static final String CSRF_TOKEN_HASH_NAME = "csrfTokenHash";
  private static final String CURRENT_PAGE_NAME = "currentPage";
  private static final String SECRET_KEY = loadSecretKey();

  // Danh sách endpoint cần bảo vệ
  private static final List<String> PROTECTED_ENDPOINTS = Arrays.asList(
      "/Cart", "/Category", "/ChangePassword", "/CustomerOrder", "/Customer",
      "/OrderDetail", "/Order", "/PayOrder", "/ProductByCategory", "/ProductList",
      "/Product", "/Review", "/SignInUp", "/Statistical", "/UserCategory", "/User"
  );

  // Tải khóa bí mật từ file cấu hình
  private static String loadSecretKey() {
    Properties props = new Properties();
    try (InputStream input = CSRFTokenFilter.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        logger.error("config.properties not found");
        return "default-secret-key";
      }
      props.load(input);
      return props.getProperty("csrf.secret.key", "default-secret-key");
    } catch (Exception e) {
      logger.error("Error loading secret key: ", e);
      return "default-secret-key";
    }
  }

  // Hàm tạo HMAC SHA256
  private String hmacSHA256(String data, String key) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
      mac.init(secretKeySpec);
      byte[] hmacData = mac.doFinal(data.getBytes());
      return Base64.getEncoder().encodeToString(hmacData);
    } catch (Exception e) {
      logger.error("Error generating HMAC: ", e);
      return null;
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Không cần thay đổi khi khởi tạo
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = httpRequest.getSession(true);
    String remoteIp = httpRequest.getRemoteAddr();
    String currentUrl = httpRequest.getRequestURI();
    String previousPage = (String) session.getAttribute(CURRENT_PAGE_NAME);
    String method = httpRequest.getMethod();

    // Kiểm tra xem endpoint hiện tại có nằm trong danh sách được bảo vệ hay không
    boolean isProtectedEndpoint = PROTECTED_ENDPOINTS.stream()
        .anyMatch(endpoint -> currentUrl.endsWith(endpoint));

    // Kiểm tra CSRF cho các request POST đến endpoint được bảo vệ
    if ("POST".equalsIgnoreCase(method) && isProtectedEndpoint) {
      String requestCsrfToken = httpRequest.getParameter(CSRF_TOKEN_HASH_NAME);
      if (requestCsrfToken == null) {
        requestCsrfToken = httpRequest.getHeader("X-CSRF-Token");
      }

      String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
      String expectedTokenHash = sessionCsrfToken != null ? hmacSHA256(sessionCsrfToken, SECRET_KEY) : null;

      logger.info("Debug CSRF Token - Session ID: {}, Request Token: {}, Expected Hash: {}, Session Token: {}",
          session.getId(), requestCsrfToken, expectedTokenHash, sessionCsrfToken);

      if (requestCsrfToken == null || expectedTokenHash == null || !requestCsrfToken.equals(expectedTokenHash)) {
        logger.warn("CSRF validation failed - IP: {}, Session: {}, Request Token: {}, Expected Hash: {}",
            remoteIp, session.getId(), requestCsrfToken, expectedTokenHash);

        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
          httpResponse.setContentType("application/json");
          httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
          httpResponse.getWriter().write("{\"error\":\"Invalid CSRF Token\"}");
        } else {
          String action = httpRequest.getParameter("action");
          if ("logout".equalsIgnoreCase(action)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpRequest.getRequestDispatcher("/signinup.jsp?status=logout_success").forward(request, response);
          } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
          }
        }
        return;
      }
      logger.info("CSRF validation passed - IP: {}, Session: {}", remoteIp, session.getId());
    }

    // Làm mới token nếu cần
    boolean shouldRegenerateToken = false;
    if ("POST".equalsIgnoreCase(method) && isProtectedEndpoint) {
      String action = httpRequest.getParameter("action");
      // Chỉ làm mới token sau POST cho các hành động không phải signin/signup
      if (!"signin".equalsIgnoreCase(action) && !"signup".equalsIgnoreCase(action)) {
        shouldRegenerateToken = true;
      }
    } else if (previousPage == null) {
      // Làm mới token nếu không có trang trước (session mới)
      shouldRegenerateToken = true;
    }

    if (shouldRegenerateToken) {
      session.removeAttribute(CSRF_TOKEN_NAME);
      session.removeAttribute(CSRF_TOKEN_HASH_NAME);
      logger.info("Old CSRF token cleared - Session: {}, IP: {}, Reason: {}",
          session.getId(), remoteIp,
          isProtectedEndpoint && "POST".equalsIgnoreCase(method) ? "Token refreshed after validation" : "New session or page change");
    }

    // Cập nhật URL trang hiện tại
    session.setAttribute(CURRENT_PAGE_NAME, currentUrl);
    request.setAttribute("currentPage", currentUrl);

    // Tạo token mới nếu chưa có
    String csrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
    String csrfTokenHash = (String) session.getAttribute(CSRF_TOKEN_HASH_NAME);
    if (csrfToken == null || csrfTokenHash == null) {
      csrfToken = UUID.randomUUID().toString();
      csrfTokenHash = hmacSHA256(csrfToken, SECRET_KEY);
      session.setAttribute(CSRF_TOKEN_NAME, csrfToken);
      session.setAttribute(CSRF_TOKEN_HASH_NAME, csrfTokenHash);
      logger.info("New CSRF token created - Session: {}, IP: {}", session.getId(), remoteIp);
    }

    request.setAttribute(CSRF_TOKEN_HASH_NAME, csrfTokenHash);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // Không cần xử lý khi filter bị hủy
  }
}