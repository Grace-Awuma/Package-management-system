<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1>Log New Package</h1>

<div class="card">
    <div class="card-body">
        <form action="/pms/packages" method="post" class="needs-validation" novalidate>
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="trackingNumber" class="form-label">Tracking Number</label>
                    <input type="text" class="form-control" id="trackingNumber" name="trackingNumber" required>
                    <div class="invalid-feedback">Please provide a tracking number.</div>
                </div>
                <div class="col-md-6">
                    <label for="carrier" class="form-label">Carrier</label>
                    <select class="form-select" id="carrier" name="carrier" required>
                        <option value="">Select Carrier</option>
                        <c:forEach items="${carriers}" var="carrier">
                            <option value="${carrier}">${carrier}</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Please select a carrier.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-4">
                    <label for="packageType" class="form-label">Package Type</label>
                    <select class="form-select" id="packageType" name="packageType">
                        <option value="Box">Box</option>
                        <option value="Envelope">Envelope</option>
                        <option value="Tube">Tube</option>
                        <option value="Pallet">Pallet</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="size" class="form-label">Size</label>
                    <select class="form-select" id="size" name="size">
                        <option value="Small">Small</option>
                        <option value="Medium">Medium</option>
                        <option value="Large">Large</option>
                        <option value="Extra Large">Extra Large</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="priority" class="form-label">Priority</label>
                    <select class="form-select" id="priority" name="priority">
                        <option value="Standard">Standard</option>
                        <option value="Priority">Priority</option>
                        <option value="Express">Express</option>
                        <option value="Overnight">Overnight</option>
                    </select>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-6">
                    <label for="condition" class="form-label">Condition</label>
                    <select class="form-select" id="condition" name="condition">
                        <option value="Good">Good</option>
                        <option value="Damaged">Damaged</option>
                        <option value="Open">Open</option>
                        <option value="Wet">Wet</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="storageLocation" class="form-label">Storage Location</label>
                    <input type="text" class="form-control" id="storageLocation" name="storageLocation" required>
                    <div class="invalid-feedback">Please provide a storage location.</div>
                </div>
            </div>
            
            <div class="row mb-3">
                <div class="col-md-12">
                    <label for="residentId" class="form-label">Resident</label>
                    <select class="form-select" id="residentId" name="residentId" required>
                        <option value="">Select Resident</option>
                        <c:forEach items="${residents}" var="resident">
                            <option value="${resident.id}">${resident.firstName} ${resident.lastName} - Unit ${resident.unitNumber}, Room ${resident.roomNumber}</option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Please select a resident.</div>
                </div>
            </div>
            
            <div class="mb-3">
                <a href="/pms/packages" class="btn btn-secondary">Cancel</a>
                <button type="submit" class="btn btn-primary">Log Package</button>
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
    
    // Add search functionality to resident dropdown
    $(document).ready(function() {
        // Initialize select2 for better searchable dropdown
        $('#residentId').select2({
            placeholder: 'Select a resident',
            allowClear: true,
            width: '100%'
        });
    });
</script>