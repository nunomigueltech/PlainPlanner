<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a4e465149a.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <title>PlainPlanner - Settings</title>
</head>
<body>
	<div id="wrapper">
		<nav class="navbar navbar-expand-md navbar-dark bg-primary p-0 m-0 position-relative">
	        <a href="index" class="navbar-brand ml-5 p-0 text-dark" style="font-size: 2rem;">PlainPlanner</a>
	        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	
	        <div class="collapse navbar-collapse" id="navbarCollapse">
	            <ul class="nav navbar-nav ml-auto ">
	            	<li class="nav-item">
	                	<a href="dashboard" class="nav-link py-3 px-2">Dashboard</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="projects" class="nav-link py-3 px-2">Projects</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="buckets" class="nav-link py-3 px-2">Buckets</a>
	            	</li>
	            	<li class="nav-item">
	                	<a href="notes" class="nav-link py-3 px-2">Notes</a>
	            	</li>
	                <li class="nav-item dropdown" style="min-width:185px;">
	                	<a class="nav-link dropdown-toggle py-3 px-2" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" style="text-transform: capitalize;">
	                		<i class="fas fa-user-circle mx-2 d-none d-md-inline"></i> 
	                		<security:authorize access="isAuthenticated()">
							    <security:authentication property="principal.username" /> 
							</security:authorize>
	                	</a>
	                	<div class="dropdown-menu m-0 rounded-0 border-top-0" style="right:0%; left:auto; min-width:185px;">
	                		<a class="dropdown-item" href="statistics">Statistics</a>
	                		<a class="dropdown-item" href="settings">Settings</a>
	                		<div class="dropdown-divider"></div>
	                		<a class="dropdown-item" href="about">About PlainPlanner</a>
	                		<form action="/signout" method="post">
				    			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				    			<button type="submit" class="btn btn-link dropdown-item py-0">Sign Out</button>
				    		</form>
	                	</div>
	                </li>
	            </ul>
	        </div>
	    </nav>
	
	    <section id="settings-section">
	    	<div class="container">
	    		<div class="card mt-3">
	    			<div class="card-header text-white text-center" style="background-color: #3A3535">
	    				<h2 class="card-title text-center">My Settings</h2>
	    			</div>
	    			<div class="card-body">
	    				<c:if test="${not empty settings}">
	    					<ul class="list-group list-group-flush">
		    					<c:forEach items="${settings}" var="item">
								    <li class="list-group-item">
								    	<h4>${item.getKey()}</h4>  
								    	<h6>Enabled: ${item.getValue()}</h6>
								    	<c:choose>
								    		<c:when test="${item.getValue()}">
								    			<a class="btn btn-primary btn-sm ml-3 disabled" href="/toggleSetting/${item.getKey()}">Enabled</a>
								    		</c:when>
								    		<c:otherwise>
								    			<a class="btn btn-primary btn-sm ml-3" href="/toggleSetting/${item.getKey()}">Enable</a>
								    		</c:otherwise>
								    	</c:choose>
								    	<c:choose>
								    		<c:when test="${!item.getValue()}">
								    			<a class="btn btn-primary btn-sm ml-3 disabled" href="/toggleSetting/${item.getKey()}">Disabled</a>
								    		</c:when>
								    		<c:otherwise>
								    			<a class="btn btn-primary btn-sm ml-3" href="/toggleSetting/${item.getKey()}">Disable</a>
								    		</c:otherwise>
								    	</c:choose>
								    </li>
								</c:forEach>
	    					</ul>
						</c:if>
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