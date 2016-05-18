<%--
  Created by IntelliJ IDEA.
  User: артур
  Date: 16.05.2016
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Онлайн калькулятор</title>
</head>
<body BGCOLOR="#ffffcc">
<center>
    <p>
        <b>Онлайн калькулятор</b>:
    </p>
    <form action="DataBaseServlet">
    <fieldset style="border:1px #00BFFF solid;">

                <a href="http://localhost:8080"><img src="calculator.jpg" align="left" width="200" height="120"></a>

    <p>  <b>Формула: </b> <input type="text" name="formula" size=26  value="<%= request.getAttribute("formula")%> ">
    <p>  <b>Результат: </b> <input type="text" name="result" size=26 value="<%= request.getAttribute("result")%> ">
    <p>
        <input type="submit" value="Добавить результат в базу данных">
    </p>


</fieldset>
</form>
</center>
</body>
</html>
