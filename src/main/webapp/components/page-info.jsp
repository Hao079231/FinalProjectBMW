<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!-- Page Info Component -->
    <div class="page-info" style="display: none;">
      <input type="hidden" id="currentPage" value="${currentPage}">
      <input type="hidden" id="csrfTokenHash" name="csrfTokenHash" value="${csrfTokenHash}">
    </div>

    <script>
      // Function to get current page info
      function getCurrentPage() {
        return document.getElementById('currentPage') ? document.getElementById('currentPage').value : '';
      }

      // Function to get CSRF token
      function getCsrfToken() {
        return document.getElementById('csrfTokenHash') ? document.getElementById('csrfTokenHash').value : '';
      }

      // Log page change for debugging
      // console.log('Current Page:', getCurrentPage());
    </script>