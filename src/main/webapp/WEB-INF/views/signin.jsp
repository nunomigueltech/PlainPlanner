<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
    <script src="resources/js/signin.js" async></script>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
    <title>PlainPlanner - Sign in</title>
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
	            	<li class="nav-item">
	                	<a href="register" class="nav-link py-3 px-4">Register</a>
	            	</li>
	                <li class="nav-item active">
	                    <a href="signin" class="nav-link py-3 px-4">Sign in</a>
	                </li>
	            </ul>
	        </div>
	    </nav>
	
	    <section id="signin-container" class="mb-5 mt-3">
	    	<div class="container">
				<div class="row">
					<div class="col-10 col-md-6 mx-auto">
						<h2 class="text-center">Sign in</h2>
						<c:if test="${not empty message}">
							<div class="alert alert-success">${message}</div>
						</c:if>
						<c:if test="${param.logout ne null}">
							<div class="alert alert-success">You have been logged out.</div>
						</c:if>
						<c:if test="${param.error ne null}">
							<div class="alert alert-danger">Invalid username/password.</div>
						</c:if>
						<form action="/handleSignin" method="post">
							<sec:csrfInput />
							<div class="form-group">
								<label for="username">Username</label>
								<input type="text" class="form-control" id="username" name="username" placeholder="Enter Username">
								<small id="usernameError" class="text-danger" hidden></small>
							</div>
							<div class="form-group">
								<label for="password">Password</label>
								<input type="password" class="form-control" id="password" name="password" placeholder="Enter Password">
								<small class="form-text text-muted">Case-sensitive.</small>
								<small id="passwordError" class="text-danger" hidden></small>
							</div>
							<button type="submit" id="signin-button" class="btn btn-primary d-block w-100">Sign in</button>
							<br>
							<a href="register" class="text-dark">Don't have an account yet? Sign up now!</a>
						</form>
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