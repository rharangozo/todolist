<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${not empty requestScope.task}">
        
        <c:set var="taskId" value="${requestScope.task.id}"/>
        <c:set var="taskDescription" value="${requestScope.task.description}"/>
        
        <c:set var="tags" value="${requestScope.task.tags}"/> 
        
    </c:when>
    <c:otherwise>

        <c:set var="taskId" value="\${taskId}"/>
        <c:set var="taskDescription" value="\${taskDescription}"/>
        
    </c:otherwise>
</c:choose>


<li data-id="${taskId}">

    <span contenteditable="true" class="desc"><c:out value="${taskDescription}"/></span>

    <c:forEach items="${tags}" var="tag">
        <span class="tag"><c:out value="${tag.tagName}"/></span>
    </c:forEach>

    <a class="rm-btn">-</a>
</li>
