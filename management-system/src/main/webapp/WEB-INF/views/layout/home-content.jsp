<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="jumbotron bg-light p-5 rounded">
    <h1 class="display-4">Welcome to Package Management System</h1>
    <p class="lead">Efficiently manage package deliveries, notifications, and pickups for residential buildings.</p>
    <hr class="my-4">
    <p>Get started by logging in or exploring the features below.</p>
    <div class="d-flex gap-2">
        <a class="btn btn-primary btn-lg" href="/pms/packages" role="button">Manage Packages</a>
        <a class="btn btn-outline-primary btn-lg" href="/pms/residents" role="button">Manage Residents</a>
    </div>
</div>

<div class="row mt-5">
    <h2 class="text-center mb-4">Key Features</h2>
    
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body text-center">
                <div class="mb-3 fs-1">📦</div>
                <h5 class="card-title">Package Tracking</h5>
                <p class="card-text">Log and track packages from arrival to pickup with detailed status updates.</p>
            </div>
        </div>
    </div>
    
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body text-center">
                <div class="mb-3 fs-1">📱</div>
                <h5 class="card-title">Instant Notifications</h5>
                <p class="card-text">Automatically notify residents via email or SMS when their packages arrive.</p>
            </div>
        </div>
    </div>
    
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <div class="card-body text-center">
                <div class="mb-3 fs-1">✍️</div>
                <h5 class="card-title">Pickup Management</h5>
                <p class="card-text">Record package pickups with signature confirmation for accountability.</p>
            </div>
        </div>
    </div>
</div>
