<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-4">
    <h2>Picked Up Packages</h2>
    
    <c:choose>
        <c:when test="${not empty parcelPackages}">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Tracking #</th>
                            <th>Carrier</th>
                            <th>Resident</th>
                            <th>Arrival Date</th>
                            <th>Pickup Date</th>
                            <th>Recipient</th>
                            <th>Signature</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="parcelPackage" items="${parcelPackages}">
                            <tr>
                                <td>${parcelPackage.id}</td>
                                <td>${parcelPackage.trackingNumber}</td>
                                <td>${parcelPackage.carrier}</td>
                                <td>${parcelPackage.resident.fullName}</td>
                                <td>${requestScope['formattedArrivalDate_'.concat(parcelPackage.id)]}</td>
                                <c:if test="${parcelPackage.pickup != null}">
                                    <td>${requestScope['formattedPickupDate_'.concat(parcelPackage.id)]}</td>
                                    <td>${parcelPackage.pickup.recipientName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${parcelPackage.pickup.signatureConfirmation}">
                                                <span class="badge bg-success">Yes</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning">No</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${parcelPackage.pickup.notes}</td>
                                </c:if>
                                <c:if test="${parcelPackage.pickup == null}">
                                    <td colspan="4"><span class="text-danger">No pickup information available</span></td>
                                </c:if>
                                <td>
                                    <a href="/pms/packages/${parcelPackage.id}" class="btn btn-info btn-sm">Details</a>
                                    <a href="/pms/packages/${parcelPackage.id}/delete" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure you want to delete this package?')">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                No picked up packages found.
            </div>
        </c:otherwise>
    </c:choose>
    
    <div class="mt-3">
        <a href="/pms/packages" class="btn btn-primary">Back to All Packages</a>
    </div>
</div>