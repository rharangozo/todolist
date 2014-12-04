<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
        <title>Task desk</title>
    </head>

    <body>
        <h3>Task desk</h3><br>

        <c:forEach items="${tasks}" var="task">
            <c:out value="${task.id}"/>
            <c:out value="${task.description}"/>
            </br>
        </c:forEach>

    </body>
</html>