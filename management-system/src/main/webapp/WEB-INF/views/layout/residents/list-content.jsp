<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1>Resident List</h1>
    <a href="/pms/residents/new" class="btn btn-primary">
        <i class="bi bi-plus-circle"></i> Add New Resident
    </a>
</div>

<div class="card mb-4">
    <div class="card-body">
        <form action="/pms/residents/search" method="get" class="row g-3">
            <div class="col-md-10">
                <input type="text" class="form-control" id="keyword" name="keyword" 
                       placeholder="Search by name, email, phone, unit, or room" value="${keyword}">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
        </form>
    </div>
</div>

<c:choose>
    <c:when test="${not empty residents}">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Unit</th>
                        <th>Room</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${residents}" var="resident">
                        <tr>
                            <td>${resident.id}</td>
                            <td>${resident.fullName}</td>
                            <td>${resident.email}</td>
                            <td>${resident.phone}</td>
                            <td>${resident.unitNumber}</td>
                            <td>${resident.roomNumber}</td>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="/pms/residents/${resident.id}" class="btn btn-info">Details</a>
                                    <a href="/pms/residents/${resident.id}/edit" class="btn btn-warning">Edit</a>
                                    <a href="/pms/residents/${resident.id}/delete" class="btn btn-danger" 
                                       onclick="return confirm('Are you sure you want to delete this resident?')">Delete</a>
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
            <h4 class="alert-heading">No residents found!</h4>
            <p>There are no residents in the system yet. Add a new resident to get started.</p>
            <hr>
            <a href="/pms/residents/new" class="btn btn-primary">Add New Resident</a>
        </div>
    </c:otherwise>
</c:choose>
