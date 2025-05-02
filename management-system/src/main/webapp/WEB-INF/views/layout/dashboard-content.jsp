<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1 class="mb-4">Dashboard</h1>

<div class="row mb-4">
    <div class="col-md-3">
        <div class="card bg-primary text-white mb-3">
            <div class="card-body text-center">
                <h5 class="card-title">Total Packages</h5>
                <p class="card-text display-4">${totalPackages != null ? totalPackages : 0}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card bg-success text-white mb-3">
            <div class="card-body text-center">
                <h5 class="card-title">Total Residents</h5>
                <p class="card-text display-4">${totalResidents != null ? totalResidents : 0}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card bg-warning text-dark mb-3">
            <div class="card-body text-center">
                <h5 class="card-title">Pending Packages</h5>
                <p class="card-text display-4">${pendingPackages != null ? pendingPackages : 0}</p>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card bg-info text-white mb-3">
            <div class="card-body text-center">
                <h5 class="card-title">Notified Packages</h5>
                <p class="card-text display-4">${notifiedPackages != null ? notifiedPackages : 0}</p>
            </div>
        </div>
    </div>
</div>

<div class="row mb-4">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header bg-light">
                <h5 class="card-title mb-0">Recent Packages</h5>
            </div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty recentPackages}">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Resident</th>
                                        <th>Carrier</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${recentPackages}" var="pkg">
                                        <tr>
                                            <td>${pkg.id}</td>
                                            <td>${pkg.resident.fullName}</td>
                                            <td>${pkg.carrier}</td>
                                            <td>
                                                <span class="badge ${pkg.status == 'RECEIVED' ? 'bg-primary' : 
                                                                  pkg.status == 'STORED' ? 'bg-info' : 
                                                                  pkg.status == 'NOTIFIED' ? 'bg-warning' : 
                                                                  'bg-success'}">
                                                    ${pkg.status}
                                                </span>
                                            </td>
                                            <td>
                                                <a href="/pms/packages/${pkg.id}" class="btn btn-sm btn-info">View</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted">No recent packages found.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="card-footer bg-light">
                <a href="/pms/packages" class="btn btn-primary btn-sm">View All Packages</a>
            </div>
        </div>
    </div>
    
    <div class="col-md-6">
        <div class="card">
            <div class="card-header bg-light">
                <h5 class="card-title mb-0">Quick Actions</h5>
            </div>
            <div class="card-body">
                <div class="d-grid gap-2">
                    <a href="/pms/packages/new" class="btn btn-primary">Log New Package</a>
                    <a href="/pms/packages/filter?status=NOTIFIED" class="btn btn-warning">Process Pickups</a>
                    <a href="/pms/residents/new" class="btn btn-success">Add New Resident</a>
                </div>
            </div>
        </div>
    </div>
</div>
