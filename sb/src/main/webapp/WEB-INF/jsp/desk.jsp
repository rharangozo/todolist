<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/style.css">
        <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
        <title>Task desk</title>
    </head>

    <body>

        <div id="header">
            <h3>Task desk</h3>
        </div><div id="content-container">

            <div id="left-sidebar">

            </div><div id="main-content">

                <input id="smart-input" type="text" maxlength="50"/>
                <button type="button">+</button>

                <ul>
                    <c:forEach items="${tasks}" var="task">
                        <li>
                            <c:out value="${task.description}"/>
                        </li>
                    </c:forEach>
                </ul>

            </div><div id="right-sidebar">
            </div>
        </div><div id="footer">

        </div>

    </body>
</html>