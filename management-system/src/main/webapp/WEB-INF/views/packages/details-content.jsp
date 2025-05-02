<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>Package Details</h1>

<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title">Package #${parcelPackage.id}</h5>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-6">
                <p><strong>Tracking Number:</strong> ${parcelPackage.trackingNumber}</p>
                <p><strong>Carrier:</strong> ${parcelPackage.carrier}</p>
                <p><strong>Package Type:</strong> ${parcelPackage.packageType}</p>
                <p><strong>Size:</strong> ${parcelPackage.size}</p>
                <p><strong>Priority:</strong> ${parcelPackage.priority}</p>
                <p><strong>Condition:</strong> ${parcelPackage.condition}</p>
            </div>
            <div class="col-md-6">
                <p><strong>Arrival Date:</strong> <fmt:formatDate value="${parcelPackage.arrivalDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                <p>
                    <strong>Status:</strong> 
                    <span class="badge ${parcelPackage.status == 'RECEIVED' ? 'bg-primary' : 
                                      parcelPackage.status == 'STORED' ? 'bg-info' : 
                                      parcelPackage.status == 'NOTIFIED' ? 'bg-warning' : 
                                      'bg-success'}">
                        ${parcelPackage.status}
                    </span>
                </p>
                <p><strong>Storage Location:</strong> ${parcelPackage.storageLocation}</p>
                <p><strong>Notification Sent:</strong> ${parcelPackage.notificationSent ? 'Yes' : 'No'}</p>
                <c:if test="${parcelPackage.notificationSent}">
                    <p><strong>Notification Date:</strong> <fmt:formatDate value="${parcelPackage.notificationDate}" pattern="yyyy-MM-dd HH:mm" /></p>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title">Resident Information</h5>
    </div>
    <div class="card-body">
        <p><strong>Name:</strong> ${parcelPackage.resident.fullName}</p>
        <p><strong>Email:</strong> ${parcelPackage.resident.email}</p>
        <p><strong>Phone:</strong> ${parcelPackage.resident.phone}</p>
        <p><strong>Unit Number:</strong> ${parcelPackage.resident.unitNumber}</p>
        <p><strong>Room Number:</strong> ${parcelPackage.resident.roomNumber}</p>
    </div>
</div>

<c:if test="${parcelPackage.status == 'PICKED_UP'}">
    <div class="card mb-4">
        <div class="card-header bg-success text-white">
            <h5 class="card-title mb-0">Pickup Information</h5>
        </div>
        <div class="card-body">
            <p><strong>Pickup Date:</strong> <fmt:formatDate value="${parcelPackage.pickup.pickupDate}" pattern="yyyy-MM-dd HH:mm" /></p>
            <p><strong>Recipient:</strong> ${parcelPackage.pickup.recipientName}</p>
            <p><strong>Signature Confirmation:</strong> ${parcelPackage.pickup.signatureConfirmation ? 'Yes' : 'No'}</p>
            <c:if test="${not empty parcelPackage.pickup.notes}">
                <p><strong>Notes:</strong> ${parcelPackage.pickup.notes}</p>
            </c:if>
            <c:if test="${not empty parcelPackage.pickup.signatureData}">
                <p><strong>Signature:</strong></p>
                <img src="${parcelPackage.pickup.signatureData}" alt="Signature" class="img-fluid border" />
            </c:if>
        </div>
    </div>
</c:if>

<div class="mb-3">
    <a href="/pms/packages" class="btn btn-secondary">Back to List</a>
    <c:if test="${parcelPackage.status != 'PICKED_UP'}">
        <a href="/pms/packages/${parcelPackage.id}/pickup" class="btn btn-success">Record Pickup</a>
    </c:if>
    <c:if test="${parcelPackage.status == 'PICKED_UP'}">
        <a href="/pms/packages/${parcelPackage.id}/delete" class="btn btn-danger" 
           onclick="return confirm('Are you sure you want to delete this package?')">Delete Package</a>
    </c:if>
</div>
