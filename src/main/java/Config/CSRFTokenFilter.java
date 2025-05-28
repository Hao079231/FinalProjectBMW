package Config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
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

  private static String loadSecretKey() {
    Properties props = new Properties();
    try (InputStream input = CSRFTokenFilter.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        logger.error("Unable to find config.properties");
        return "default-secret-key";
      }
      props.load(input);
      return props.getProperty("csrf.secret.key", "default-secret-key");
    } catch (Exception e) {
      logger.error("Error loading secret key: ", e);
      return "default-secret-key";
    }
  }

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
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = httpRequest.getSession(true);
    String remoteIp = httpRequest.getRemoteAddr();

    // Lấy URL hiện tại
    String currentUrl = httpRequest.getRequestURI();
    String previousPage = (String) session.getAttribute(CURRENT_PAGE_NAME);

    // Kiểm tra nếu đây là trang mới
    if (previousPage == null || !previousPage.equals(currentUrl)) {
      // Xóa CSRF token cũ
      session.removeAttribute(CSRF_TOKEN_NAME);
      session.removeAttribute(CSRF_TOKEN_HASH_NAME);
      logger.info("Cleared old CSRF token for session: {}, IP: {}", session.getId(), remoteIp);
      
      // Lưu trang hiện tại
      session.setAttribute(CURRENT_PAGE_NAME, currentUrl);
    }

    // Thêm currentPage vào request attribute
    request.setAttribute("currentPage", currentUrl);

    // Sinh CSRF token mới nếu chưa có
    String csrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
    String csrfTokenHash = (String) session.getAttribute(CSRF_TOKEN_HASH_NAME);
    if (csrfToken == null || csrfTokenHash == null) {
      csrfToken = UUID.randomUUID().toString();
      csrfTokenHash = hmacSHA256(csrfToken, SECRET_KEY);
      session.setAttribute(CSRF_TOKEN_HASH_NAME, csrfTokenHash);
      session.setAttribute(CSRF_TOKEN_NAME, csrfToken);
      logger.info("Generated new CSRF token for session: {}, IP: {}", session.getId(), remoteIp);
    }

    // Chỉ set token hash vào request attribute, không set token gốc
    request.setAttribute(CSRF_TOKEN_HASH_NAME, csrfTokenHash);

    // Kiểm tra CSRF token cho các yêu cầu POST và GET có tham số
    String method = httpRequest.getMethod();
    boolean hasParameters = !httpRequest.getParameterMap().isEmpty();
    
    if ("POST".equalsIgnoreCase(method)) {
      // Lấy token đã mã hóa từ request
      String requestCsrfToken = httpRequest.getParameter(CSRF_TOKEN_HASH_NAME);
      if (requestCsrfToken == null) {
        // Hỗ trợ backward compatibility nếu form gửi lên là csrfToken
        requestCsrfToken = httpRequest.getParameter(CSRF_TOKEN_NAME);
      }
      if (requestCsrfToken == null) {
        requestCsrfToken = httpRequest.getHeader("X-CSRF-Token");
      }

      // Lấy token gốc từ session và hash lại
      String sessionCsrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
      String expectedTokenHash = hmacSHA256(sessionCsrfToken, SECRET_KEY);

      // So sánh token đã mã hóa
      if (requestCsrfToken == null || !requestCsrfToken.equals(expectedTokenHash)) {
        logger.warn("CSRF token validation failed for IP: {}, session: {}, token: {}", 
            remoteIp, session.getId(), requestCsrfToken);
        
        // Nếu là AJAX request, trả về lỗi 403 dạng JSON
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
          httpResponse.setContentType("application/json");
          httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
          httpResponse.getWriter().write("{\"error\":\"Invalid CSRF Token\"}");
        } else {
          // Nếu là logout hoặc huy, forward về signinup.jsp?status=logout_success nhưng trả về 403
          String action = httpRequest.getParameter("action");
          if ("logout".equalsIgnoreCase(action)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpRequest.getRequestDispatcher("/signinup.jsp?status=logout_success").forward(request, response);
          }  else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
          }
        }
        return;
      }
      logger.info("CSRF token validated successfully for IP: {}, session: {}", remoteIp, session.getId());
    }

    // Tiếp tục chuỗi filter
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
