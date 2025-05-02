<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Update Storage Location - PMS</title>
    <link rel="stylesheet" href="/pms/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Update Storage Location</h1>
        
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title">Package #${package.id}</h5>
            </div>
            <div class="card-body">
                <p><strong>Tracking Number:</strong> ${package.trackingNumber}</p>
                <p><strong>Carrier:</strong> ${package.carrier}</p>
                <p><strong>Resident:</strong> ${package.resident.fullName}</p>
                <p><strong>Current Storage Location:</strong> ${package.storageLocation}</p>
            </div>
        </div>
        
        <form action="/pms/packages/${package.id}/update-storage" method="post" class="needs-validation" novalidate>
            <div class="mb-3">
                <label for="storageLocation" class="form-label">New Storage Location</label>
                <input type="text" class="form-control" id="storageLocation" name="storageLocation" 
                       value="${package.storageLocation}" required>
                <div class="invalid-feedback">Please provide a storage location.</div>
            </div>
            
            <div class="mb-3">
                <a href="/pms/packages/${package.id}" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Update Storage</button>
            </div>
        </form>
    </div>
    
    <script src="/pms/webjars/jquery/3.6.4/jquery.min.js"></script>
    <script src="/pms/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation
        (function() {
            'use strict';
            var forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function(form) {
                form.addEventListener('submit', function(event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
