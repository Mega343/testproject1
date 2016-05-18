package com.goit.servlets;

import com.goit.logic.Main;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CalculateServlet")
public class SimpleServlet extends HttpServlet {

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

        String userInputFormula = request.getParameter("formula");
        String result = Main.calculateUserFormula(userInputFormula);
        request.setAttribute("result", result);
        request.setAttribute("formula", userInputFormula);
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
