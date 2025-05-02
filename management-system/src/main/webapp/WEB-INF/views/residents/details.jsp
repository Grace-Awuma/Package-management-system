<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Resident Details - PMS</title>
    <link rel="stylesheet" href="/pms/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h1>Resident Details</h1>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title">${resident.fullName}</h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>ID:</strong> ${resident.id}</p>
                        <p><strong>First Name:</strong> ${resident.firstName}</p>
                        <p><strong>Last Name:</strong> ${resident.lastName}</p>
                        <p><strong>Email:</strong> ${resident.email}</p>
                        <p><strong>Phone:</strong> ${resident.phone}</p>
                    </div>
					<div class="col-md-6">
					    <p><strong>Unit Number:</strong> ${resident.unitNumber}</p>
					    <p><strong>Room Number:</strong> ${resident.roomNumber}</p>
					</div>
                </div>
            </div>
        </div>
        
        <h2>Packages</h2>
        <c:choose>
            <c:when test="${not empty resident.packages}">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tracking #</th>
                            <th>Carrier</th>
                            <th>Arrival Date</th>
                            <th>Status</th>
                            <th>Storage Location</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${resident.packages}" var="pkg">
                            <tr>
                                <td>${pkg.id}</td>
                                <td>${pkg.trackingNumber}</td>
                                <td>${pkg.carrier}</td>
                                <td><fmt:formatDate value="${pkg.arrivalDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td>
                                    <span class="badge ${pkg.status == 'RECEIVED' ? 'bg-primary' : 
                                                      pkg.status == 'STORED' ? 'bg-info' : 
                                                      pkg.status == 'NOTIFIED' ? 'bg-warning' : 
                                                      'bg-success'}">
                                        ${pkg.status}
                                    </span>
                                </td>
                                <td>${pkg.storageLocation}</td>
                                <td>
                                    <a href="/pms/packages/${pkg.id}" class="btn btn-sm btn-info">Details</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">No packages found for this resident.</div>
            </c:otherwise>
        </c:choose>
        
        <div class="mb-3">
            <a href="/pms/residents" class="btn btn-secondary">Back to List</a>
            <a href="/pms/residents/${resident.id}/edit" class="btn btn-warning">Edit</a>
            <a href="/pms/residents/${resident.id}/delete" class="btn btn-danger" 
               onclick="return confirm('Are you sure you want to delete this resident?')">Delete</a>
            <a href="/pms/packages/new?residentId=${resident.id}" class="btn btn-primary">Log New Package</a>
        </div>
    </div>
    
    <script src="/pms/webjars/jquery/3.6.4/jquery.min.js"></script>
    <script src="/pms/webjars/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
