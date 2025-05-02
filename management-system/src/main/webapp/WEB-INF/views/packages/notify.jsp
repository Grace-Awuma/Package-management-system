<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Notify Resident - PMS</title>
    <link rel="stylesheet" href="/pms/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Notify Resident</h1>
        
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title">Package #${package.id}</h5>
            </div>
            <div class="card-body">
                <p><strong>Tracking Number:</strong> ${package.trackingNumber}</p>
                <p><strong>Carrier:</strong> ${package.carrier}</p>
                <p><strong>Resident:</strong> ${package.resident.fullName}</p>
                <p><strong>Email:</strong> ${package.resident.email}</p>
                <p><strong>Phone:</strong> ${package.resident.phone}</p>
                <p><strong>Notification Status:</strong> ${package.notificationSent ? 'Sent' : 'Not Sent'}</p>
            </div>
        </div>
        
        <form action="/pms/packages/${package.id}/notify" method="post">
            <div class="mb-3">
                <label class="form-label">Notification Type</label>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="notificationType" id="emailNotification" value="email" 
                           ${empty package.resident.email ? 'disabled' : 'checked'}>
                    <label class="form-check-label" for="emailNotification">
                        Email Notification
                    </label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="notificationType" id="smsNotification" value="sms"
                           ${empty package.resident.phone ? 'disabled' : ''}>
                    <label class="form-check-label" for="smsNotification">
                        SMS Notification
                    </label>
                </div>
            </div>
            
            <div class="mb-3">
                <a href="/pms/packages/${package.id}" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary" ${empty package.resident.email && empty package.resident.phone ? 'disabled' : ''}>
                    Send Notification
                </button>
            </div>
        </form>
    </div>
    
    <script src="/pms/webjars/jquery/3.6.4/jquery.min.js"></script>
    <script src="/pms/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
