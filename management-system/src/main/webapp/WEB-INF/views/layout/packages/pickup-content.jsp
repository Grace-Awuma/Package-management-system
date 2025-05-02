<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1>Record Package Pickup</h1>

<div class="card mb-4">
    <div class="card-header">
        <h5 class="card-title">Package #${parcelPackage.id}</h5>
    </div>
    <div class="card-body">
        <p><strong>Tracking Number:</strong> ${parcelPackage.trackingNumber}</p>
        <p><strong>Carrier:</strong> ${parcelPackage.carrier}</p>
        <p><strong>Resident:</strong> ${parcelPackage.resident.fullName}</p>
        <p><strong>Storage Location:</strong> ${parcelPackage.storageLocation}</p>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <form action="/pms/packages/${parcelPackage.id}/pickup" method="post" class="needs-validation" novalidate>
            <div class="mb-3">
                <label for="recipientName" class="form-label">Recipient Name</label>
                <input type="text" class="form-control" id="recipientName" name="recipientName" 
                       value="${parcelPackage.resident.fullName}" required>
                <div class="invalid-feedback">Please provide the recipient's name.</div>
            </div>
            
            <div class="mb-3">
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="signatureConfirmation" name="signatureConfirmation" checked>
                    <label class="form-check-label" for="signatureConfirmation">
                        Signature Confirmation
                    </label>
                </div>
            </div>
            
            <div class="mb-3" id="signatureSection">
                <label class="form-label">Signature</label>
                <div>
                    <canvas id="signatureCanvas" width="400" height="200" style="border: 1px solid #ccc; background-color: #fff;"></canvas>
                    <input type="hidden" id="signatureData" name="signatureData">
                </div>
                <div class="mt-2">
                    <button type="button" class="btn btn-sm btn-secondary" id="clearSignature">Clear Signature</button>
                </div>
            </div>
            
            <div class="mb-3">
                <label for="notes" class="form-label">Notes</label>
                <textarea class="form-control" id="notes" name="notes" rows="3"></textarea>
            </div>
            
            <div class="mb-3">
                <a href="/pms/packages/${parcelPackage.id}" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Record Pickup</button>
            </div>
        </form>
    </div>
</div>

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
    
    // Signature pad
    document.addEventListener('DOMContentLoaded', function() {
        var canvas = document.getElementById('signatureCanvas');
        var ctx = canvas.getContext('2d');
        var drawing = false;
        var lastX, lastY;
        
        // Show/hide signature section based on checkbox
        document.getElementById('signatureConfirmation').addEventListener('change', function() {
            if(this.checked) {
                document.getElementById('signatureSection').style.display = 'block';
            } else {
                document.getElementById('signatureSection').style.display = 'none';
            }
        });
        
        // Handle mouse events for drawing
        canvas.addEventListener('mousedown', function(e) {
            drawing = true;
            var rect = canvas.getBoundingClientRect();
            lastX = e.clientX - rect.left;
            lastY = e.clientY - rect.top;
        });
        
        canvas.addEventListener('mousemove', function(e) {
            if (!drawing) return;
            var rect = canvas.getBoundingClientRect();
            var currentX = e.clientX - rect.left;
            var currentY = e.clientY - rect.top;
            
            ctx.beginPath();
            ctx.moveTo(lastX, lastY);
            ctx.lineTo(currentX, currentY);
            ctx.stroke();
            
            lastX = currentX;
            lastY = currentY;
        });
        
        canvas.addEventListener('mouseup', function() {
            drawing = false;
            // Save signature data to hidden field
            document.getElementById('signatureData').value = canvas.toDataURL();
        });
        
        canvas.addEventListener('mouseout', function() {
            drawing = false;
        });
        
        // Clear signature
        document.getElementById('clearSignature').addEventListener('click', function() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            document.getElementById('signatureData').value = '';
        });
        
        // Set up canvas
        ctx.lineWidth = 2;
        ctx.strokeStyle = '#000';
        ctx.lineJoin = 'round';
        ctx.lineCap = 'round';
        
        // Touch support for mobile devices
        canvas.addEventListener('touchstart', function(e) {
            e.preventDefault();
            var touch = e.touches[0];
            var rect = canvas.getBoundingClientRect();
            lastX = touch.clientX - rect.left;
            lastY = touch.clientY - rect.top;
            drawing = true;
        });
        
        canvas.addEventListener('touchmove', function(e) {
            e.preventDefault();
            if (!drawing) return;
            var touch = e.touches[0];
            var rect = canvas.getBoundingClientRect();
            var currentX = touch.clientX - rect.left;
            var currentY = touch.clientY - rect.top;
            
            ctx.beginPath();
            ctx.moveTo(lastX, lastY);
            ctx.lineTo(currentX, currentY);
            ctx.stroke();
            
            lastX = currentX;
            lastY = currentY;
        });
        
        canvas.addEventListener('touchend', function(e) {
            e.preventDefault();
            drawing = false;
            document.getElementById('signatureData').value = canvas.toDataURL();
        });
    });
</script>
