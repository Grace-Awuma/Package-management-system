<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="../layout/main.jsp" charEncoding="UTF-8">
    <c:param name="title" value="Picked Up Packages" />
    <c:param name="content">
        <c:import url="../layout/packages/picked-up-content.jsp" />
    </c:param>
</c:import>