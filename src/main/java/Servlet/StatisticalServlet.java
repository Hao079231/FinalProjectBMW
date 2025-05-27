package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import Beans.Statistical;
import Utils.DBStatistical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class StatisticalServlet
 */
@WebServlet("/Statistical")
public class StatisticalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(StatisticalServlet.class);

    public StatisticalServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Received GET request for statistical page");
        logger.debug("Forwarding to thongke.jsp");
        request.getRequestDispatcher("thongke.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tuNgay = request.getParameter("tuNgay");
        String denNgay = request.getParameter("denNgay");

        logger.info("Received POST request with date range: from {} to {}", tuNgay, denNgay);

        try {
            DBStatistical dbStatistical = new DBStatistical();
            logger.debug("Fetching statistical data for date range: {} to {}", tuNgay, denNgay);
            ArrayList<Statistical> danhSachThongKe = dbStatistical.getThongKe(tuNgay, denNgay);

            logger.info("Retrieved {} statistical records for date range: {} to {}",
                    danhSachThongKe.size(), tuNgay, denNgay);
            request.setAttribute("danhSachThongKe", danhSachThongKe);
            logger.debug("Forwarding to thongke.jsp with statistical data");
            request.getRequestDispatcher("thongke.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Error retrieving statistical data for date range: {} to {}", tuNgay, denNgay, e);
            request.setAttribute("errorMessage", "Không thể lấy dữ liệu thống kê. Vui lòng thử lại.");
            request.getRequestDispatcher("thongke.jsp").forward(request, response);
        }
    }
}