<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${not empty requestScope.task}">
        <c:set var="task" value="${requestScope.task}"/>    
    </c:when>
    <c:otherwise>
        <jsp:useBean id="task" scope="page" class="rh.domain.Task">
            <jsp:setProperty name="task" property="id" value="0"/>
            <jsp:setProperty name="task" property="description" value=""/>
        </jsp:useBean>
    </c:otherwise>
</c:choose>


<li data-id="${task.id}">

    <span contenteditable="true" 
          class="desc"><c:out value="${task.description}"/></span>

    <c:forEach items="${task.tags}" var="tag">
        <span class="tag"><c:out value="${tag.tagName}"/></span>
    </c:forEach>

    <a class="rm-btn">-</a>
</li>
