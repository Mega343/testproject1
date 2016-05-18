package com.goit.logic;


import java.util.List;
import java.util.Stack;

public class Function implements Calculative {

    private List<FormulaElement> functionFormulaInRPN;


    public void setFunctionFormulaInRPN(List<FormulaElement> functionFormulaInRPN) {
        this.functionFormulaInRPN = functionFormulaInRPN;
    }

    @Override
    public double calculate() {
        try {
            return calculateFunction();
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error in function formula: " + e.getMessage());
        }
    }

    private double calculateFunction() throws Exception{
        Stack<Double> calculationStack = new Stack<>();
        double calculationResult = 0;

        for (FormulaElement element : functionFormulaInRPN) {
            if (element.getType().equals(FormulaElementTypes.NUMBER)) {

                calculationResult = element.getValue();

            }

            else if (element.getType().equals(FormulaElementTypes.CONSTANT)){

                calculationResult = getConstantValue(element.getOperator());

            } else if (element.getType().equals(FormulaElementTypes.OPERATOR)){

                double rightOperand = calculationStack.pop();
                double leftOperand = calculationStack.pop();
                calculationResult = executeOperator(leftOperand, rightOperand, element.getOperator());

            } else if (element.getType().equals(FormulaElementTypes.FUNCTION)){

                double operand = calculationStack.pop();
                calculationResult = executeFunction(operand, element.getOperator());

            }

            calculationStack.add(calculationResult);
        }
        return calculationStack.pop();
    }

    private double executeOperator(double leftOperand, double rightOperand, SupportedOperators operator){
        double result = 0;

        if (operator.equals(SupportedOperators.PLUS)) {
            result = leftOperand + rightOperand;
        } else if (operator.equals(SupportedOperators.MINUS)) {
            result = leftOperand - rightOperand;
        } else if (operator.equals(SupportedOperators.MULT)) {
            result = leftOperand * rightOperand;
        } else if (operator.equals(SupportedOperators.DIV)) {
            result = leftOperand / rightOperand;
        } else if (operator.equals(SupportedOperators.POW)) {
            result = Math.pow(leftOperand, rightOperand);
        }

        return result;
    }

    private double executeFunction(double operand, SupportedOperators operator){
        double result = 0;

        if (operator.equals(SupportedOperators.SQRT)) {
            result = Math.sqrt(operand);
        } else if (operator.equals(SupportedOperators.SIN)) {
            result = Math.sin(operand);
        } else if (operator.equals(SupportedOperators.COS)) {
            result = Math.cos(operand);
        } else if (operator.equals(SupportedOperators.TN)) {
            result = Math.tan(operand);
        } else if (operator.equals(SupportedOperators.LN)) {
            result = Math.log(operand);
        } else if (operator.equals(SupportedOperators.LG)) {
            result = Math.log10(operand);
        }

        return result;
    }

    private double getConstantValue(SupportedOperators constant){
        double result = 0;

        if (constant.equals(SupportedOperators.E)) {
            result = Math.E;
        } else if (constant.equals(SupportedOperators.PI)){
            result = Math.PI;
        }

        return result;
    }

}

