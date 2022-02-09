<%--
  Created by IntelliJ IDEA.
  User: антон
  Date: 09.02.2022
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<form method="POST" action='meals?id=${meals.mealId}' name="frmAddUser">
    <table>
        <tbody>
        <tr>
            <td>DateTime:</td>
            <td><input type="datetime-local" id="localDate" name="date"
                       value="${meals.dateTime}"/> /></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input type="text" id="description" name="description" placeholder="add description"
                       value="${meals.description}"/> /></td>
        </tr>
        <tr>
            <td>calories:</td>
            <td><input type="text" id="calories" name="calories" placeholder="add calories"
                       value="${meals.calories}"/> /></td>>
        </tr>
        </tbody>
    </table>
    <input type="Submit" value="Save"/>
    <input type="Submit" value="Cancel"/> </form>
<form method="GET" action="meals" name="meals"></form>
</body>
</html>
