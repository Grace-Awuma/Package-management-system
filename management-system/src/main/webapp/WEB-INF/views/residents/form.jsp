<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${resident.id == null ? 'Add New' : 'Edit'} Resident - PMS</title>
    <link rel="stylesheet" href="/pms/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <jsp:include page="form-content.jsp" />
    </div>
    
    <script src="/pms/webjars/jquery/3.6.4/jquery.min.js"></script>
    <script src="/pms/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
