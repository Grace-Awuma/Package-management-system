<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${param.title} - Package Management System</title>
    <link rel="stylesheet" href="/pms/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="/pms/css/styles.css">
	<!-- Add Select2 CSS here -->
	    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <!-- Additional CSS -->
    <c:if test="${not empty param.styles}">
        <jsp:include page="${param.styles}" />
    </c:if>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="/pms">📦 PMS</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link ${param.activeMenu == 'home' ? 'active' : ''}" href="/pms">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.activeMenu == 'packages' ? 'active' : ''}" href="/pms/packages">Packages</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${param.activeMenu == 'residents' ? 'active' : ''}" href="/pms/residents">Residents</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li class="nav-item">
                                <a class="nav-link" href="/pms/auth/login">Login</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                                    Welcome, ${sessionScope.user.fullName}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="/pms/auth/logout">Logout</a></li>
                                </ul>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container mt-4">
        <!-- Alert Messages -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty warningMessage}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                ${warningMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        
        <!-- Page Content -->
        <jsp:include page="${param.content}" />
    </div>

    <!-- Footer -->
    <footer class="bg-light py-4 mt-5">
        <div class="container text-center">
            <p class="mb-0">Package Management System &copy; 2025 | Developed by Grace Aku Nutifafa Awuma</p>
        </div>
    </footer>

    <!-- JavaScript -->
    <script src="/pms/webjars/jquery/3.6.4/jquery.min.js"></script>
    <script src="/pms/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
	<!-- Add Select2 JS here -->
	<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <!-- Additional JavaScript -->
    <c:if test="${not empty param.scripts}">
        <jsp:include page="${param.scripts}" />
    </c:if>
</body>
</html>
