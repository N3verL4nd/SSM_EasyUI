<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: N3verL4nd
  Date: 2017/7/5
  Time: 8:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>用户列表</title>
</head>
<body>
    <table border="1">
        <tr>
            <th>id</th>
            <th>name</th>
            <th>age</th>
            <th>birth</th>
            <th>email</th>
        </tr>
        <c:forEach items="${requestScope.persons}" var="person">
            <tr>
                <td>${person.id}</td>
                <td>${person.name}</td>
                <td>${person.age}</td>
                <td>${person.birth}</td>
                <td>${person.email}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
