package com.goit.servlets;


import com.goit.database.DBWorker;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/DataBaseServlet")
public class DBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setStatus(200);
        DBWorker dbWorker = new DBWorker();
        PrintWriter out = response.getWriter();
        String result = request.getParameter("result");
        String userInputFormula = request.getParameter("formula");


        if (!result.equals("Некоректный ввод ")) {
            dbWorker.addFormulaToDatabase(userInputFormula, result);


        } else {
            out.println(
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" +
                            "http://www.w3.org/TR/html4/loose.dtd\">\n" +
                            "<html> \n" +
                            "<head> \n" +
                            "<meta http-equiv=\"Content-Type\" content=\"text/html;" +
                            "charset=ISO-8859-1\"> \n" +
                            "<title> My first jsp  </title> \n" +
                            "</head> \n" +
                            "<body> \n" +
                            "Oops! Cannot add incorrect formula to database" +
                            "</font> \n" +
                            "</body> \n" +
                            "</html>"

            );
        }

        try {
                java.sql.Statement statement = dbWorker.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM results");
                out.println(" <table align=\"center\" border=\"2\" bordercolor = \"#B0E0E6\" width=\"500\"> " +
                        "<tr><td> id </td><td>Function</td><td> Result </td><td></td></tr>"

                );
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String function = resultSet.getString(2);
                    String resultFunctionCalculate = resultSet.getString(3);

                    out.println(

                            "<tr><td>" + id + "</td><td>" + function + "</td><td>" + resultFunctionCalculate + "</td><td><a href=\"http://localhost:8080/DeleteServlet?id=" + id + "\"><img src=\"delete1.png\"  width=\"20\" height=\"20\"></a></td></tr>"

                    );
                }
                out.println(
                        "</table><p>" +
                                "<center> <a href=\"http://localhost:8080\"><img src=\"firstpage.jpg\"  width=\"200\" height=\"60\" ></a> </center>"
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }
}

