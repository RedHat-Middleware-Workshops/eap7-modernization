<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	Object counterObj = session.getAttribute("counter");
	int counter = 0;
	if(counterObj != null && counterObj instanceof Integer) {
		counter = ((Integer) counterObj).intValue();
	}
	counter++;
	session.setAttribute("counter",new Integer(counter));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cluster Test</title>
</head>
<body>
	<h1>Cluster Test</h1>
	<p>You have visited this application <%= counter %> times.</p>
	<br/>
	<p>This page is being served from <b><%= System.getProperty("server.name") %></b></p>
</body>
</html>
