<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <%@ page import="Beans.Message" %>

    <% Message msg=(Message)session.getAttribute("message"); if(msg !=null) { %>
      <div class="alert <%= msg.getCssClass() %> alert-dismissible fade show" role="alert">
        <%= msg.getContent() %>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
      </div>
      <% session.removeAttribute("message"); } %>