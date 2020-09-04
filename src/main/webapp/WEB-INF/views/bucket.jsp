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
    <link rel="stylesheet" type="text/css" href="../resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="../resources/css/style.css">
    <title>PlainPlanner - Bucket</title>
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
						<div class="row">
							<div class="col">
								<h2>${bucket.name}</h2>
							</div>
						</div>
						<c:if test="${!bucket.description.isEmpty()}">
							<div class="row">
								<div class="col">
									<h6>"${bucket.description}"</h6>
								</div>
							</div>
						</c:if>
						<div class="row justify-content-center">
							<div class="col-6 col-sm-12">
								<a class="btn btn-primary btn-sm m-2" href="/buckets">Back to Buckets</a>
								<a class="btn btn-primary btn-sm m-2" href="/buckets"><i class="fas fa-edit"></i> Edit Bucket</a>
								<c:choose>
									<c:when test="${bucket.isDeletionPermitted()}">
										<a class="btn btn-primary btn-sm m-2" href="/buckets"><i class="fas fa-trash"></i> Delete Bucket</a>
									</c:when>
									<c:otherwise>
										<a class="btn btn-primary btn-sm m-2 disabled" href="/buckets"><i class="fas fa-trash"></i> Delete Bucket</a>
									</c:otherwise>
								</c:choose>
								
							</div>
						</div>
	    			</div>
	    			<div class="card-body">
	    				<div class="row">
	    					<div class="col">
	    						<ul>
	    							
	    						</ul>
	    					</div>
	    				</div>
						<div class="row">
							<div class="col">
								<h4 class="card-title">Tasks & Ideas 
								<a class="btn btn-primary btn-sm ml-3" href="/addIdea/bucket/${bucket.id}"><i class="fas fa-plus-circle"></i> New Idea</a>
								<a class="btn btn-primary btn-sm ml-1" href="/addTask/bucket/${bucket.id}"><i class="fas fa-plus-circle"></i> New Task</a>
								</h4>
								<hr>
								<ul class="list-group list-group-flush">
									<c:choose>
										<c:when test="${not empty ideas}">
											<c:forEach items="${ideas}" var="idea">
												 <li class="list-group-item">
												 	<h4><a href="/idea/bucket/${bucket.id}/${idea.id}">${idea.title}</a></h4>
												 	<c:if test="${idea.isTask()}">
											    		<h6 class="mb-0">Due: ${idea.deadline}</h6>
											    	</c:if>
											    	<c:if test="${idea.isComplete()}">
												    	<h6 class="text-warning font-weight-bold">Completed</h6>
												   	</c:if>
												   	<c:choose>
												   		<c:when test="${not empty idea.description }">
												   			<h6 class="card-subtitle text-muted mt-2">${idea.description }</h6>
												   		</c:when>
												   		<c:otherwise>
												   			<h6 class="card-subtitle text-muted mt-2">No description.</h6>
												   		</c:otherwise>
												   	</c:choose>
												   	<a class="btn btn-primary btn-sm mt-3" href="/deleteIdea/bucket/${bucket.id}/${idea.id}"><i class="fas fa-trash"></i> Delete</a>
												 </li>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<li class="list-group-item">
												<p>This bucket doesn't contain any tasks or ideas.</p>
											</li>
										</c:otherwise>
									</c:choose>								
								</ul>
							</div>
						</div>
	    			</div>
	    		</div>
	        </div>
	    </section>
	    
		<c:import url="/resources/footer.jsp" />
	</div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</body>
</html>