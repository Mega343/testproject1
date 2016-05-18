package com.goit.logic;



public class FormulaElement {

    private double value;
    private SupportedOperators operator;
    private FormulaElementTypes type;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public SupportedOperators getOperator() {
        return operator;
    }

    public void setOperator(SupportedOperators operator) {
        this.operator = operator;
    }

    public FormulaElementTypes getType() {
        return type;
    }

    public void setType(FormulaElementTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String result = null;
        if (this.getType().equals(FormulaElementTypes.NUMBER)){
            result = String.valueOf(value);
        } else if(this.getType().equals(FormulaElementTypes.OPERATOR)
                || this.getType().equals(FormulaElementTypes.FUNCTION)
                || this.getType().equals(FormulaElementTypes.CONSTANT)){
            result = operator.toString();
        }

        return result;
    }

}