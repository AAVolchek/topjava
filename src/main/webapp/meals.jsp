<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>


<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<b><a href="meals?action=edit">Add Meal</a></b>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${meals}" var="meals">
        <tr style="color:${meals.excess ? "red" : "green"}">
            <td>${f:formatLocalDateTime(meals.dateTime,"yyyy-MM-dd HH:mm")}</td>
            <td>${meals.description}</td>
            <td>${meals.calories}</td>
            <td><a href="meals?action=update&id=${meals.mealId}">Update</a></td>
            <td><a href="meals?action=delete&id=${meals.mealId}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>