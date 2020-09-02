<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    <title>PlainPlanner - Simple, yet effective</title>
</head>
<body>
	<div id="wrapper">
	    <nav class="navbar navbar-expand-sm navbar-dark bg-primary p-0 m-0 overflow-hidden">
	        <a href="index" class="navbar-brand ml-5 p-0 text-dark" style="font-size: 2rem;">PlainPlanner</a>
	        <button class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
	            <span class="navbar-toggler-icon"></span>
	        </button>
	
	        <div class="collapse navbar-collapse" id="navbarCollapse">
	            <ul class="nav navbar-nav ml-auto">
	            	<li class="nav-item">
	                	<a href="register" class="nav-link py-3 px-4">Register</a>
	            	</li>
	                <li class="nav-item">
	                    <a href="signin" class="nav-link py-3 px-4">Sign In</a>
	                </li>
	            </ul>
	        </div>
	    </nav>
	
	    <section id="test" class="mb-5 mt-3">
	    	<div class="container">
			
	        </div>
	    </section>
	    
	    <c:import url="/resources/footer.jsp" />
	</div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI" crossorigin="anonymous"></script>
</body>
</html>