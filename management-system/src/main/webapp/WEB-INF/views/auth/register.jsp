<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Package Management System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .error-message {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
            display: block;
        }
        .is-invalid {
            border-color: #dc3545 !important;
        }
        .is-valid {
            border-color: #198754 !important;
        }
        .validation-spinner {
            display: none;
            width: 1rem;
            height: 1rem;
            margin-left: 0.5rem;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-6">
                <div class="card shadow">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">Create an Account</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger" role="alert">
                                ${errorMessage}
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/auth/register" method="post" id="registerForm">
                            <div class="mb-3">
                                <label for="fullName" class="form-label">Full Name</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="fullName" name="fullName" value="${user.fullName}" required>
                                    <span class="spinner-border text-primary validation-spinner" id="fullNameSpinner"></span>
                                </div>
                                <span class="error-message" id="fullNameError"></span>
                            </div>
                            
                            <div class="mb-3">
                                <label for="username" class="form-label">Username</label>
                                <div class="input-group">
                                    <input type="text" class="form-control" id="username" name="username" value="${user.username}" required>
                                    <span class="spinner-border text-primary validation-spinner" id="usernameSpinner"></span>
                                </div>
                                <span class="error-message" id="usernameError"></span>
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="password" name="password" required>
                                    <span class="spinner-border text-primary validation-spinner" id="passwordSpinner"></span>
                                </div>
                                <span class="error-message" id="passwordError"></span>
                                <small class="text-muted">Password must be at least 8 characters long</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirm Password</label>
                                <div class="input-group">
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                    <span class="spinner-border text-primary validation-spinner" id="confirmPasswordSpinner"></span>
                                </div>
                                <span class="error-message" id="confirmPasswordError"></span>
                            </div>
                            
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary" id="registerButton">Register</button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center">
                        <p class="mb-0">Already have an account? <a href="${pageContext.request.contextPath}/auth/login">Login</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS and dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- AJAX Validation Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Get form elements
            const fullNameInput = document.getElementById('fullName');
            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');
            const confirmPasswordInput = document.getElementById('confirmPassword');
            const registerForm = document.getElementById('registerForm');
            const registerButton = document.getElementById('registerButton');
            
            // Get error message elements
            const fullNameError = document.getElementById('fullNameError');
            const usernameError = document.getElementById('usernameError');
            const passwordError = document.getElementById('passwordError');
            const confirmPasswordError = document.getElementById('confirmPasswordError');
            
            // Get spinner elements
            const fullNameSpinner = document.getElementById('fullNameSpinner');
            const usernameSpinner = document.getElementById('usernameSpinner');
            const passwordSpinner = document.getElementById('passwordSpinner');
            const confirmPasswordSpinner = document.getElementById('confirmPasswordSpinner');
            
            // Track validation state
            const validationState = {
                fullName: false,
                username: false,
                password: false,
                confirmPassword: false
            };
            
            // Function to validate a field via AJAX
            function validateField(field, value, errorElement, spinnerElement) {
                // Show spinner
                spinnerElement.style.display = 'inline-block';
                
                // For confirm password, we need to send both passwords
                let requestValue = value;
                if (field === 'confirmPassword') {
                    requestValue = passwordInput.value + '::' + value;
                }
                
                // Create URL with context path
                const url = '${pageContext.request.contextPath}/auth/validate-field?field=' + 
                            field + '&value=' + encodeURIComponent(requestValue);
                
                fetch(url)
                    .then(response => response.json())
                    .then(result => {
                        // Hide spinner
                        spinnerElement.style.display = 'none';
                        
                        const input = document.getElementById(field);
                        
                        if (!result.valid) {
                            // Show error message
                            errorElement.textContent = result.message;
                            input.classList.add('is-invalid');
                            input.classList.remove('is-valid');
                            validationState[field] = false;
                        } else {
                            // Clear error message
                            errorElement.textContent = '';
                            input.classList.add('is-valid');
                            input.classList.remove('is-invalid');
                            validationState[field] = true;
                        }
                        
                        // Update button state
                        updateButtonState();
                    })
                    .catch(error => {
                        // Hide spinner
                        spinnerElement.style.display = 'none';
                        console.error('Validation error:', error);
                        errorElement.textContent = 'An error occurred during validation';
                        validationState[field] = false;
                        updateButtonState();
                    });
            }
            
            // Function to update button state based on validation
            function updateButtonState() {
                const allValid = Object.values(validationState).every(state => state === true);
                registerButton.disabled = !allValid;
            }
            
            // Add debounce function to limit API calls
            function debounce(func, wait) {
                let timeout;
                return function(...args) {
                    clearTimeout(timeout);
                    timeout = setTimeout(() => func.apply(this, args), wait);
                };
            }
            
            // Debounced validation functions
            const debouncedValidateFullName = debounce(function() {
                validateField('fullName', fullNameInput.value, fullNameError, fullNameSpinner);
            }, 300);
            
            const debouncedValidateUsername = debounce(function() {
                validateField('username', usernameInput.value, usernameError, usernameSpinner);
            }, 300);
            
            const debouncedValidatePassword = debounce(function() {
                validateField('password', passwordInput.value, passwordError, passwordSpinner);
                
                // If confirm password has a value, validate it again since it depends on password
                if (confirmPasswordInput.value) {
                    validateField('confirmPassword', confirmPasswordInput.value, confirmPasswordError, confirmPasswordSpinner);
                }
            }, 300);
            
            const debouncedValidateConfirmPassword = debounce(function() {
                validateField('confirmPassword', confirmPasswordInput.value, confirmPasswordError, confirmPasswordSpinner);
            }, 300);
            
            // Add input and blur event listeners
            fullNameInput.addEventListener('input', debouncedValidateFullName);
            fullNameInput.addEventListener('blur', function() {
                validateField('fullName', this.value, fullNameError, fullNameSpinner);
            });
            
            usernameInput.addEventListener('input', debouncedValidateUsername);
            usernameInput.addEventListener('blur', function() {
                validateField('username', this.value, usernameError, usernameSpinner);
            });
            
            passwordInput.addEventListener('input', debouncedValidatePassword);
            passwordInput.addEventListener('blur', function() {
                validateField('password', this.value, passwordError, passwordSpinner);
            });
            
            confirmPasswordInput.addEventListener('input', debouncedValidateConfirmPassword);
            confirmPasswordInput.addEventListener('blur', function() {
                validateField('confirmPassword', this.value, confirmPasswordError, confirmPasswordSpinner);
            });
            
            // Prevent form submission if there are validation errors
            registerForm.addEventListener('submit', function(event) {
                // Validate all fields before submission
                validateField('fullName', fullNameInput.value, fullNameError, fullNameSpinner);
                validateField('username', usernameInput.value, usernameError, usernameSpinner);
                validateField('password', passwordInput.value, passwordError, passwordSpinner);
                validateField('confirmPassword', confirmPasswordInput.value, confirmPasswordError, confirmPasswordSpinner);
                
                // Check if all fields are valid
                const allValid = Object.values(validationState).every(state => state === true);
                
                if (!allValid) {
                    event.preventDefault();
                    alert('Please fix the validation errors before submitting.');
                }
            });
            
            // Initial validation of pre-filled fields
            if (fullNameInput.value) {
                validateField('fullName', fullNameInput.value, fullNameError, fullNameSpinner);
            }
            
            if (usernameInput.value) {
                validateField('username', usernameInput.value, usernameError, usernameSpinner);
            }
        });
    </script>
</body>
</html>