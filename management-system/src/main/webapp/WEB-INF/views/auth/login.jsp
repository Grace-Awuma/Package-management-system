<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Package Management System</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        :root {
            --primary-cyan: #06b6d4;
            --primary-purple: #8b5cf6;
            --surface-elevated: #f8fafc;
            --text-primary: #0f172a;
            --text-secondary: #64748b;
        }
        
        body {
            background: linear-gradient(135deg, var(--primary-cyan) 0%, var(--primary-purple) 100%);
            min-height: 100vh;
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
        }
        
        .auth-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem 1rem;
        }
        
        .auth-card {
            background: var(--surface-elevated);
            border-radius: 1rem;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
            padding: 3rem;
            width: 100%;
            max-width: 400px;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        
        .auth-header {
            text-align: center;
            margin-bottom: 2rem;
        }
        
        .auth-logo {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, var(--primary-cyan), var(--primary-purple));
            border-radius: 1rem;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            color: white;
            font-size: 1.5rem;
        }
        
        .auth-title {
            color: var(--text-primary);
            font-weight: 700;
            font-size: 1.5rem;
            margin-bottom: 0.5rem;
        }
        
        .auth-subtitle {
            color: var(--text-secondary);
            font-size: 0.875rem;
        }
        
        .form-floating {
            margin-bottom: 1rem;
        }
        
        .form-control {
            border: 2px solid #e2e8f0;
            border-radius: 0.75rem;
            padding: 1rem;
            font-size: 0.875rem;
            transition: all 0.2s ease;
        }
        
        .form-control:focus {
            border-color: var(--primary-cyan);
            box-shadow: 0 0 0 3px rgba(6, 182, 212, 0.1);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, var(--primary-cyan), var(--primary-purple));
            border: none;
            border-radius: 0.75rem;
            padding: 0.875rem 1.5rem;
            font-weight: 600;
            font-size: 0.875rem;
            transition: all 0.2s ease;
            width: 100%;
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 25px -5px rgba(6, 182, 212, 0.4);
            color: white;
        }
        
        .btn-primary:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }
        
        .alert {
            border-radius: 0.75rem;
            border: none;
            font-size: 0.875rem;
        }
        
        .auth-link {
            color: var(--primary-cyan);
            text-decoration: none;
            font-weight: 500;
        }
        
        .auth-link:hover {
            color: var(--primary-purple);
        }
        
        .loading-spinner {
            display: none;
            width: 20px;
            height: 20px;
            border: 2px solid transparent;
            border-top: 2px solid white;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            margin-left: 0.5rem;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .btn-content {
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <div class="auth-logo">
                    <i class="fas fa-box"></i>
                </div>
                <h1 class="auth-title">Welcome Back</h1>
                <p class="auth-subtitle">Sign in to your Package Management System account</p>
            </div>
            
            <!-- Alert Messages -->
            <div id="alertContainer">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i><c:out value="${errorMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i><c:out value="${successMessage}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
            </div>
            
            <!-- <CHANGE> Using dynamic context path with proper JSP syntax -->
            <form id="loginForm" method="post" action="<c:url value='/auth/login' />">
                <div class="form-floating">
                    <input type="text" class="form-control" id="username" name="username" placeholder="Username" required autocomplete="off">
                    <label for="username"><i class="fas fa-user me-2"></i>Username</label>
                </div>
                
                <div class="form-floating">
                    <input type="password" class="form-control" id="password" name="password" placeholder="Password" required autocomplete="off">
                    <label for="password"><i class="fas fa-lock me-2"></i>Password</label>
                </div>
                
                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="remember" name="remember">
                    <label class="form-check-label" for="remember">
                        Remember me
                    </label>
                </div>
                
                <button type="submit" class="btn btn-primary" id="loginBtn">
                    <div class="btn-content">
                        <span id="loginText">Sign In</span>
                        <div class="loading-spinner" id="loginSpinner"></div>
                    </div>
                </button>
            </form>
            
            <!-- <CHANGE> Using c:url tag for proper URL resolution -->
            <div class="text-center mt-3">
                <p class="mb-0">Don't have an account? <a href="<c:url value='/auth/register' />" class="auth-link">Sign up</a></p>
            </div>
        </div>
    </div>
    
    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <script>
        $(document).ready(function() {
            $('#loginForm').on('submit', function(e) {
                e.preventDefault();
                
                var form = $(this);
                var btn = $('#loginBtn');
                var text = $('#loginText');
                var spinner = $('#loginSpinner');
                var alertContainer = $('#alertContainer');
                
                // Show loading state
                btn.prop('disabled', true);
                text.hide();
                spinner.show();
                
                // Clear previous alerts
                alertContainer.find('.alert').remove();
                
                $.ajax({
                    url: form.attr('action'),
                    method: 'POST',
                    data: form.serialize(),
                    success: function(response, textStatus, xhr) {
                        // Check if redirect occurred (successful login)
                        if (xhr.status === 200) {
                        window.location.href = '${pageContext.request.contextPath}/dashboard';
                        }
                    },
                    error: function(xhr) {
                        var errorMessage = 'Login failed. Please try again.';
                        
                        if (xhr.status === 401) {
                            errorMessage = 'Invalid username or password.';
                        } else if (xhr.status === 404) {
                            errorMessage = 'Login endpoint not found. Please contact support.';
                        } else if (xhr.status === 500) {
                            errorMessage = 'Server error. Please try again later.';
                        }
                        
                        showAlert('danger', errorMessage);
                    },
                    complete: function() {
                        // Reset button state
                        btn.prop('disabled', false);
                        text.show();
                        spinner.hide();
                    }
                });
            });
            
            function showAlert(type, message) {
                var iconClass = type === 'success' ? 'check-circle' : 'exclamation-circle';
                var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                    '<i class="fas fa-' + iconClass + ' me-2"></i>' + message +
                    '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>' +
                    '</div>';
                alertContainer.html(alertHtml);
            }
        });
    </script>
</body>
</html>