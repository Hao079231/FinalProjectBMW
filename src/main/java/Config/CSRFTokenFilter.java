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
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFTokenFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(CSRFTokenFilter.class);
  private static final String CSRF_TOKEN_NAME = "csrfToken";

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

    // Sinh CSRF token nếu chưa có
    String csrfToken = (String) session.getAttribute(CSRF_TOKEN_NAME);
    if (csrfToken == null) {
      csrfToken = UUID.randomUUID().toString();
      session.setAttribute(CSRF_TOKEN_NAME, csrfToken);
      logger.info("Generated new CSRF token for session: {}, IP: {}", session.getId(), remoteIp);
    }

    // Set token as request attribute so it can be accessed in JSP
    request.setAttribute(CSRF_TOKEN_NAME, csrfToken);

    // Kiểm tra CSRF token cho các yêu cầu POST
    if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
      // Bỏ qua kiểm tra CSRF cho action logout
      String action = httpRequest.getParameter("action");
      if ("logout".equalsIgnoreCase(action)) {
        chain.doFilter(request, response);
        return;
      }

      // Kiểm tra CSRF token từ form parameter
      String requestCsrfToken = httpRequest.getParameter(CSRF_TOKEN_NAME);
      
      // Nếu không tìm thấy trong form, kiểm tra header (cho AJAX requests)
      if (requestCsrfToken == null) {
        requestCsrfToken = httpRequest.getHeader("X-CSRF-Token");
      }

      // Kiểm tra token có hợp lệ hay không
      if (requestCsrfToken == null || !requestCsrfToken.equals(csrfToken)) {
        logger.warn("CSRF token validation failed for IP: {}, session: {}, token: {}", 
            remoteIp, session.getId(), requestCsrfToken);
        
        // Nếu là AJAX request, trả về lỗi 403 dạng JSON
        String xRequestedWith = httpRequest.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(xRequestedWith)) {
          httpResponse.setContentType("application/json");
          httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
          httpResponse.getWriter().write("{\"error\":\"Invalid CSRF Token\"}");
        } else {
          // Nếu là request thông thường, chuyển hướng đến trang lỗi
          httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
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
