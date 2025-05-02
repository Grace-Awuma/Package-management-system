<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="container mt-4">
    <div class="row">
        <div class="col-md-12">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert">
                    ${successMessage}
                </div>
            </c:if>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">
                    <h4>${resident.id == null ? 'Add New Resident' : 'Edit Resident'}</h4>
                </div>
                <div class="card-body">
                    <form:form action="${pageContext.request.contextPath}/residents/save" method="post" modelAttribute="resident" class="needs-validation" novalidate="true">
                        <form:hidden path="id" />
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="firstName" class="form-label">First Name</label>
                                <form:input path="firstName" id="firstName" class="form-control" required="true" />
                                <div class="invalid-feedback">Please enter first name.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="lastName" class="form-label">Last Name</label>
                                <form:input path="lastName" id="lastName" class="form-control" required="true" />
                                <div class="invalid-feedback">Please enter last name.</div>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="email" class="form-label">Email</label>
                                <form:input path="email" id="email" class="form-control" type="email" required="true" />
                                <div class="invalid-feedback">Please enter a valid email.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="phone" class="form-label">Phone</label>
                                <form:input path="phone" id="phone" class="form-control" required="true" />
                                <div class="invalid-feedback">Please enter phone number.</div>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="unitNumber" class="form-label">Unit Number</label>
                                <select class="form-select" id="unitNumber" name="unitNumber" required>
                                    <option value="">Select Unit</option>
                                    <c:forEach items="${unitNumbers}" var="unit">
                                        <option value="${unit}" ${resident.unitNumber == unit ? 'selected' : ''}>${unit}</option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Please select a unit number.</div>
                            </div>
							<div class="col-md-6">
							    <label for="room.id" class="form-label">Room Number</label>
							    <select class="form-select" id="room.id" name="room.id" required>
							        <option value="">Select Room</option>
							        <c:forEach items="${rooms}" var="room">
							            <option value="${room.id}" ${resident.room.id == room.id ? 'selected' : ''}>
							                Room ${room.roomNumber}
							            </option>
							        </c:forEach>
							    </select>
							    <div class="invalid-feedback">Please select a room number.</div>
							</div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="moveInDate" class="form-label">Move-in Date</label>
                                <form:input path="moveInDate" id="moveInDate" class="form-control" type="date" required="true" />
                                <div class="invalid-feedback">Please enter move-in date.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="leaseEndDate" class="form-label">Lease End Date</label>
                                <form:input path="leaseEndDate" id="leaseEndDate" class="form-control" type="date" required="true" />
                                <div class="invalid-feedback">Please enter lease end date.</div>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <label for="notes" class="form-label">Notes</label>
                                <form:textarea path="notes" id="notes" class="form-control" rows="3" />
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-primary">Save</button>
                                <a href="${pageContext.request.contextPath}/residents" class="btn btn-secondary">Cancel</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// JavaScript for form validation
(function() {
    'use strict';
    
    // Fetch all forms we want to apply validation to
    var forms = document.querySelectorAll('.needs-validation');
    
    // Loop over them and prevent submission
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
</script>