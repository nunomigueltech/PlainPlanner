<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a4e465149a.js" crossorigin="anonymous"></script>
    <script src="resources/js/register.js" async></script>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <title>PlainPlanner - Registration</title>
</head>
<body>
	<div id="wrapper">
	    <nav class="navbar navbar-expand-sm navbar-dark bg-primary p-0 m-0 overflow-hidden">
	        <a href="index" class="navbar-brand ml-5 p-0 text-dark" style="font-size: 2rem;">PlainPlanner</a>
	        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	
	        <div class="collapse navbar-collapse" id="navbarCollapse">
	            <ul class="navbar-nav ml-auto">
	            	<li class="nav-item active">
	                	<a href="register" class="nav-link py-3 px-4">Register</a>
	            	</li>
	                <li class="nav-item">
	                    <a href="signin" class="nav-link py-3 px-4">Sign in</a>
	                </li>
	            </ul>
	        </div>
	    </nav>
	
	    <section id="signin-container" class="mb-5 mt-3">
	    	<div class="container">
				<div class="row">
					<div class="col-10 col-md-6 mx-auto">
						<h2 class="text-center">Registration</h2>
						<c:if test="${ not empty error }">
							<div class="alert alert-danger">${error}</div>
						</c:if>
						<form:form action="/register" method="POST" modelAttribute="user">
							<div class="form-group">
								<form:label for="username" path="username">Username</form:label>
								<form:input type="text" class="form-control" id="username" path="username" placeholder="Enter Username" />
								<small id="usernameHelp" class="form-text text-muted">Only alphanumeric chararcters are allowed.</small>
								<c:if test="${not empty usernameError}">
									<small class="form-text text-danger">${usernameError}</small>
								</c:if>
								<small id="username-message" class="form-text text-danger" hidden></small>
							</div>
							<div class="form-group">
								<form:label for="password" path="password">Password</form:label>
								<form:input type="password" class="form-control" id="password" path="password" placeholder="Enter Password" />
								<small class="form-text text-muted">Passwords must be between 8 and 50 characters long, as well as contain at least 1 lowercase character, 1 uppercase character, and 1 number. </small>
								<small class="form-text text-muted">Optionally, you can include the following special characters: *.!@$%^&:;<>,.?~_+-=|</small>
								<small class="password-error form-text text-danger" hidden></small>
							</div>
							<div class="form-group">
								<form:label for="confirm-password" path="confirmPassword">Repeat Password</form:label>
								<form:input type="password" class="form-control" id="confirm-password" path="confirmPassword" placeholder="Re-enter Password" />
								<small class="password-error form-text text-danger" hidden></small>
							</div>
							<button id="register-button" type="submit" class="btn btn-primary d-block w-100">Register</button>
							<br>
							<a href="signin" class="text-dark">Already have an account? Sign in here.</a>
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
</body>
</html>