<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="layout/main.jsp">
    <jsp:param name="title" value="Home" />
    <jsp:param name="activeMenu" value="home" />
    <jsp:param name="content" value="home-content.jsp" />
</jsp:include>
