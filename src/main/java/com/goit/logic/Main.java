package com.goit.logic;


public class Main {


    public static String calculateUserFormula(String userInputFormula) {
        String result;
        double resultCalculateFormula;
        try {
            FormulaParser parser = new FormulaParser(userInputFormula);
            Function function = parser.parse();
            resultCalculateFormula = function.calculate();
            result = Double.toString(resultCalculateFormula);
        }
        catch (Exception e) {
           return "Некоректный ввод";
        }

        return result;
    }
}
