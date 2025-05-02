<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../layout/main.jsp">
    <jsp:param name="title" value="Record Package Pickup" />
    <jsp:param name="activeMenu" value="packages" />
    <jsp:param name="content" value="packages/pickup-content.jsp" />
</jsp:include>
