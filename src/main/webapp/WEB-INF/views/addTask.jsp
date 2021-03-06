<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a4e465149a.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="/resources/css/bootstrap.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" integrity="sha512-mSYUmp1HYZDFaVKK//63EcZq4iFWFjxSL+Z3T/aCt4IO9Cejm03q3NKKYN6pFQzY0SBOr8h+eCIAZHPXcpZaNw==" crossorigin="anonymous" />
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
    <title>PlainPlanner - Add Task</title>
</head>
<body>
	<div id="wrapper">
		<nav class="navbar navbar-expand-md navbar-dark bg-primary p-0 m-0 position-relative">
	        <a href="/index" class="navbar-brand ml-5 p-0 text-dark" style="font-size: 2rem;">PlainPlanner</a>
	        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	
	        <div class="collapse navbar-collapse" id="navbarCollapse">
	            <ul class="nav navbar-nav ml-auto ">
	            	<li class="nav-item">
	                	<a href="/dashboard" class="nav-link py-3 px-2">Dashboard</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="/projects" class="nav-link py-3 px-2">Projects</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="/buckets" class="nav-link py-3 px-2">Buckets</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="/notes" class="nav-link py-3 px-2">Notes</a>
	            	</li>
	                <li class="nav-item dropdown" style="min-width:185px;">
	                	<a class="nav-link dropdown-toggle py-3 px-2" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" style="text-transform: capitalize;">
	                		<i class="fas fa-user-circle mx-2 d-none d-md-inline"></i> 
	                		<security:authorize access="isAuthenticated()">
							    <security:authentication property="principal.username" /> 
							</security:authorize>
	                	</a>
	                	<div class="dropdown-menu m-0 rounded-0 border-top-0" style="right:0%; left:auto; min-width:185px;">
	                		<a class="dropdown-item" href="/statistics">Statistics</a>
	                		<a class="dropdown-item" href="/settings">Settings</a>
	                		<div class="dropdown-divider"></div>
	                		<a class="dropdown-item" href="/about">About PlainPlanner</a>
	                		<form action="/signout" method="post">
				    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				    			<button type="submit" class="btn btn-link dropdown-item py-0">Sign Out</button>
				    		</form>
	                	</div>
	                </li>
	            </ul>
	        </div>
	    </nav>
	
	    <section id="statistics-section" class="mb-5 mt-3">
	    	<div class="container">
	    		<div class="card">
	    			<div class="card-header text-white text-center" style="background-color: #3A3535">
	    				<h2>Create New Task</h2>
	    			</div>
	    			<div class="card-body">
						<form:form action="/addNewItem/${referrer}/${referrerId}" method="post" modelAttribute="item">
							<form:hidden path="itemType" value="task" />
							<c:choose>
								<c:when test="${referrer == 'bucket'}">
									<form:hidden path="bucketID" value="${referrerId}" />
								</c:when>
								<c:otherwise>
									<form:hidden path="projectID" value="${referrerId}" />
								</c:otherwise>
							</c:choose>
							<div class="row my-2">
		    					<div class="input-group justify-content-center">
		    						<form:label class="col-4 col-md-2 col-form-label" path="title">Title</form:label>
		    						<div class="col-8 col-md-4">
		    							<form:input type="text" class="login-control form-control" placeholder="Enter your idea here" path="title"></form:input>
		    						</div>
		    					</div>
		    				</div>
		    				<div class="row my-2">
		    					<div class="input-group justify-content-center">
		    						<form:label class="col-4 col-md-2 col-form-label" path="content">Description</form:label>
		    						<div class="col-8 col-md-4">
		    							<form:input type="text" class="login-control form-control" placeholder="Enter a description for your idea" path="content"></form:input>
		    						</div>
		    					</div>
		    				</div>
		    				</div>
		    				<div class="row my-2">
		    					<div class="input-group justify-content-center">
		    						<form:label class="col-6 col-sm-2 col-form-label" path="content">Deadline</form:label>
		    						<div class="col-6 col-sm-4">
										<form:input class="form-control text-center" id="date" path="date" placeholder="MM/DD/YYY" type="text" style="max-width: 125px;"></form:input>		    						
									</div>
		    					</div>
		    				</div>
		    				<div class="row my-4 justify-content-center">
		    					<div class="col-6 col-md-4 d-flex justify-content-center align-items-center">
		    						<form:button type="submit" class="btn btn-primary mx-2">Add</form:button>
	    							<a href="/${referrer}/${referrerId}" class="mx-2">Cancel</a>
		    					</div>
	    					</div>
	    				</form:form> 
	    			</div>
	    		</div>
	        </div>
	    </section>
	    
		<c:import url="/resources/footer.jsp" />
	</div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" integrity="sha512-T/tUfKSV1bihCnd+MxKD0Hm1uBBroVYBOYSk1knyvQ9VyZJpc/ALb4P0r6ubwVPSGB2GvjeoMAJJImBG12TiaQ==" crossorigin="anonymous"></script>
	<script>
	    $(document).ready(function(){
	      var date_input=$('input[name="date"]'); //our date input has the name "date"
	      var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
	      var options={
	        format: 'mm/dd/yyyy',
	        container: container,
	        todayHighlight: true,
	        autoclose: true,
	      };
	      date_input.datepicker(options);
	      date_input.datepicker('setDate', 'now');
	    })
	</script>
</body>
</html>