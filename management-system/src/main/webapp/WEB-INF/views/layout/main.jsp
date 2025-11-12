<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - Package Management System</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <!-- Updated CSS path to work with Spring MVC resource handler -->
    <link rel="stylesheet" href="/css/styles.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/"><i class="fas fa-box"></i> PMS</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                <a class="nav-link ${param.activeMenu == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/">Home</a>
                    </li>
                    <li class="nav-item">
            <a class="nav-link ${param.activeMenu == 'packages' ? 'active' : ''}" href="${pageContext.request.contextPath}/packages">Packages</a>
                    </li>
                    <li class="nav-item">
            <a class="nav-link ${param.activeMenu == 'residents' ? 'active' : ''}" href="${pageContext.request.contextPath}/residents">Residents</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/auth/login">Login</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Alert Messages -->
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Main Content -->
    <main class="container mt-4">
        <jsp:include page="${param.content}" />
    </main>

    <!-- Footer -->
    <footer class="bg-light py-4 mt-5">
        <div class="container text-center">
            <p class="mb-0">Package Management System &copy; 2025 | Developed by Grace Aku Nutifafa Awuma</p>
        </div>
    </footer>

    <!-- Load jQuery FIRST -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Include Chat Widget (AFTER jQuery) -->
    <jsp:include page="../component/chat-widget.jsp" />

</body>
</html>
