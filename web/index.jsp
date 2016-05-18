<%@ page import="com.goit.logic.Main" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Онлайн калькулятор</title>
</head>
<%! private int accessCount = 0; %>
<body BGCOLOR="#ffffcc">
<center>
  <h2>Онлайн калькулятор</h2>
  <form action="CalculateServlet">

    <fieldset style="border:1px #00BFFF solid;" >
      <a href="http://localhost:8080"><img src="calculator.jpg" align="left"width="200" height="120"></a>
      <br> <p>Поддерживаемые математические функции: sin, cos, tn, ln, lg, sqrt, а также возведение в степень и константы Е и ПИ.</p>
      <p>  Формула: <input type="text" name="formula" size=26>

        <input type="submit" value="Расчитать">
      </p>
    </fieldset>
  </form>
</center>


<center><p> Количество обращений к странице с момента загрузки сервера: <%= ++accessCount %> </p></center>
</body>
</html>