<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1>Package List</h1>
    <div>
        <a href="/pms/packages/new" class="btn btn-primary me-2">
            <i class="bi bi-plus-circle"></i> Log New Package
        </a>
        <a href="/pms/packages/picked-up" class="btn btn-success">
            <i class="bi bi-check-circle"></i> View Picked Up Packages
        </a>
    </div>
</div>

<div class="card mb-4">
    <div class="card-header bg-light">
        <h5 class="card-title mb-0">Filter Packages</h5>
    </div>
    <div class="card-body">
        <form action="/pms/packages/filter" method="get" class="row g-3">
            <div class="col-md-4">
                <label for="status" class="form-label">Status</label>
                <select name="status" id="status" class="form-select">
                    <option value="">All Statuses</option>
                    <option value="RECEIVED" ${selectedStatus == 'RECEIVED' ? 'selected' : ''}>Received</option>
                    <option value="STORED" ${selectedStatus == 'STORED' ? 'selected' : ''}>Stored</option>
                    <option value="NOTIFIED" ${selectedStatus == 'NOTIFIED' ? 'selected' : ''}>Notified</option>
                    <option value="PICKED_UP" ${selectedStatus == 'PICKED_UP' ? 'selected' : ''}>Picked Up</option>
                </select>
            </div>
            <div class="col-md-4">
                <label for="carrier" class="form-label">Carrier</label>
                <select name="carrier" id="carrier" class="form-select">
                    <option value="">All Carriers</option>
                    <option value="USPS" ${selectedCarrier == 'USPS' ? 'selected' : ''}>USPS</option>
                    <option value="UPS" ${selectedCarrier == 'UPS' ? 'selected' : ''}>UPS</option>
                    <option value="FedEx" ${selectedCarrier == 'FedEx' ? 'selected' : ''}>FedEx</option>
                    <option value="DHL" ${selectedCarrier == 'DHL' ? 'selected' : ''}>DHL</option>
                    <option value="Amazon" ${selectedCarrier == 'Amazon' ? 'selected' : ''}>Amazon</option>
                    <option value="Other" ${selectedCarrier == 'Other' ? 'selected' : ''}>Other</option>
                </select>
            </div>
            <div class="col-md-4 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
            </div>
        </form>
    </div>
</div>

<!-- Debug Output -->
<p class="text-muted">Total Packages Found: ${fn:length(parcelPackages)}</p>

<c:choose>
    <c:when test="${not empty parcelPackages}">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Tracking #</th>
                        <th>Carrier</th>
                        <th>Resident</th>
                        <th>Arrival Date</th>
                        <th>Status</th>
                        <th>Storage Location</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${parcelPackages}" var="pkg">
                        <tr>
                            <td>${pkg.id}</td>
                            <td>${pkg.trackingNumber}</td>
                            <td>${pkg.carrier}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty pkg.resident}">
                                        ${pkg.resident.firstName} ${pkg.resident.lastName}
                                    </c:when>
                                    <c:otherwise>
                                        <em>No Resident</em>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${pkg.arrivalDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>
                                <span class="badge 
                                    ${pkg.status == 'RECEIVED' ? 'bg-primary' : 
                                    pkg.status == 'STORED' ? 'bg-info' : 
                                    pkg.status == 'NOTIFIED' ? 'bg-warning' : 
                                    'bg-success'}">
                                    ${pkg.status}
                                </span>
                            </td>
                            <td>${pkg.storageLocation}</td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="/pms/packages/${pkg.id}" class="btn btn-info">Details</a>
                                    <c:if test="${pkg.status != 'PICKED_UP'}">
                                        <a href="/pms/packages/${pkg.id}/pickup" class="btn btn-success"
                                           onclick="return confirm('Mark this package as picked up by ${pkg.resident.fullName}?')">Pickup</a>
                                    </c:if>
                                    <c:if test="${pkg.status == 'PICKED_UP'}">
                                        <a href="/pms/packages/${pkg.id}/delete" class="btn btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this package?')">Delete</a>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info">
            <h4 class="alert-heading">No packages found!</h4>
            <p>There are no packages matching your criteria. Try adjusting your filters or add a new package.</p>
            <hr>
            <a href="/pms/packages/new" class="btn btn-primary">Log New Package</a>
        </div>
    </c:otherwise>
</c:choose>
