<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${not empty requestScope.task}">
        <c:set var="task" value="${requestScope.task}"/>        
    </c:when>
    <c:otherwise>
        <c:set var="task" value="${applicationScope['taskPrototype']}"/>
    </c:otherwise>
</c:choose>


<li data-id="${task.id}">

    <span contenteditable="true" class="desc"><c:out value="${task.description}"/></span>

    <div class="tag">
        <c:forEach items="${task.tags}" var="tag">
            <span class="tag"><c:out value="${tag.tagName}"/></span>
        </c:forEach>
    </div>
    
    <a class="rm-btn">-</a>
</li>
