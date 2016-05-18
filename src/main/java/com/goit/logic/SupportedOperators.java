package com.goit.logic;

public enum SupportedOperators {
    OPEN_BRACKET(0), CLOSE_BRACKET(1),
    PLUS(2), MINUS(2),
    MULT(3), DIV(3),
    POW(4), SQRT(4), SIN(4), COS(4), TN(4), LN(4), LG(4),
    E(0), PI (0);

    private int priority;

    SupportedOperators(int priority){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}