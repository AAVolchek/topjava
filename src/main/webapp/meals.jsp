<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>


<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<p><a href="Meals?action=insert">Add Meal</a></p>
<table border="1">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${meals}" var="meals">
        <tr style="color:${meals.isExcess() == true ? "red" : "green"}">
            <td>${meals.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${meals.getDescription()}</td>
            <td>${meals.getCalories()}</td>
            <td></td>
            <td></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>