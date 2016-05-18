package com.goit.logic;

import java.util.*;

public class FormulaParser {
    private String userInput;
    private Map<String, SupportedOperators> operationTextForm;

    private List<FormulaElement> formulaRPN;
    private Stack<FormulaElement> operationsStack;
    private List<FormulaTextBlock> parsedFormulaText;
    private List<FormulaElement> formulaIN;

    public FormulaParser(String userInput) {
        initOperationsTextForm();

        this.userInput = userInput.toLowerCase().replaceAll(" ", "");
    }

    public Function parse() {

        parseFormulaTextIntoBlocks();
        convertParsedTextIntoFormulaElements();
        convertFormulaToRPN();

        Function function = new Function();
        function.setFunctionFormulaInRPN(formulaRPN);

        return function;
    }

    public void parseFormulaTextIntoBlocks() {

        parsedFormulaText = new ArrayList<>();
        StringBuilder currentElement = new StringBuilder();
        SymbolCategory previousElementType = SymbolCategory.NA;

        for (char symbol : userInput.toCharArray()) {

            SymbolCategory currentElementType = getSymbolCategory(symbol);

            if (isOperator(currentElementType)
                    || !currentElementType.equals(previousElementType)) {

                addToParsedFormulaText(currentElement.toString(), previousElementType);
                currentElement.setLength(0);

            }
            currentElement.append(symbol);

            previousElementType = currentElementType;
        }
        if (currentElement.length() != 0) {
            addToParsedFormulaText(currentElement.toString(), previousElementType);
        }

//        System.out.println(parsedFormulaText.toString());
    }

    private boolean isOperator(SymbolCategory currentElementType) {
        return !(currentElementType.equals(SymbolCategory.NUMBER)
                || currentElementType.equals(SymbolCategory.OTHER));
    }

    private void addToParsedFormulaText(String text, SymbolCategory elementType) {

        if (text.isEmpty()) {
            return;
        }

        FormulaTextBlock newElement = new FormulaTextBlock(text, elementType);

        SymbolCategory previousElementType = SymbolCategory.NA;
        if (!parsedFormulaText.isEmpty()) {
            FormulaTextBlock lastElement = parsedFormulaText.get(parsedFormulaText.size() - 1);
            previousElementType = lastElement.type;
        }

        if (text.equals("-")
                && (previousElementType.equals(SymbolCategory.NA)
                || previousElementType.equals(SymbolCategory.OPERATOR))) {

            parsedFormulaText.add(new FormulaTextBlock("-1", SymbolCategory.NUMBER));
            parsedFormulaText.add(new FormulaTextBlock("*", SymbolCategory.OPERATOR));

        } else if (elementType.equals(SymbolCategory.OTHER)
                && previousElementType.equals(SymbolCategory.NUMBER)) {

            parsedFormulaText.add(new FormulaTextBlock("*", SymbolCategory.OPERATOR));
            parsedFormulaText.add(newElement);

        } else {
            parsedFormulaText.add(newElement);
        }
    }

    public void convertParsedTextIntoFormulaElements() {

        formulaIN = new ArrayList<>();

        for (FormulaTextBlock textBlock : parsedFormulaText) {
            FormulaElement element = convertTextBlockIntoFormulaElement(textBlock);
            formulaIN.add(element);
        }
//        System.out.println(formulaIN);
    }

    private FormulaElement convertTextBlockIntoFormulaElement(FormulaTextBlock textBlock) {

        FormulaElement formulaElement = new FormulaElement();

        if (textBlock.type.equals(SymbolCategory.NUMBER)) {

            formulaElement.setValue(Double.valueOf(textBlock.text));
            formulaElement.setType(FormulaElementTypes.NUMBER);

        } else if (textBlock.type.equals(SymbolCategory.OPERATOR)) {

            formulaElement.setOperator(findSupportedOperator(textBlock.text));
            formulaElement.setType(FormulaElementTypes.OPERATOR);

        } else if (textBlock.type.equals(SymbolCategory.OPEN_BRACKET)) {

            formulaElement.setOperator(SupportedOperators.OPEN_BRACKET);
            formulaElement.setType(FormulaElementTypes.OPERATOR);

        } else if (textBlock.type.equals(SymbolCategory.CLOSE_BRACKET)) {

            formulaElement.setOperator(SupportedOperators.CLOSE_BRACKET);
            formulaElement.setType(FormulaElementTypes.OPERATOR);

        } else if (textBlock.type.equals(SymbolCategory.OTHER)) {

            if (textBlock.text.equals("e")) {

                formulaElement.setOperator(SupportedOperators.E);
                formulaElement.setType(FormulaElementTypes.CONSTANT);

            } else if (textBlock.text.equals("pi")) {

                formulaElement.setOperator(SupportedOperators.PI);
                formulaElement.setType(FormulaElementTypes.CONSTANT);

            } else {

                formulaElement.setOperator(findSupportedOperator(textBlock.text));
                formulaElement.setType(FormulaElementTypes.FUNCTION);

            }
        }
        return formulaElement;
    }

    private SupportedOperators findSupportedOperator(String text) {
        SupportedOperators operator = operationTextForm.get(text);
        if (operator != null) {
            return operator;
        } else {
            throw new UnsupportedOperationException(text);
        }
    }

    public void convertFormulaToRPN() {

        formulaRPN = new ArrayList<>();
        operationsStack = new Stack<>();

        for (FormulaElement formulaElement : formulaIN) {
            processFormulaElement(formulaElement);
        }

        while (!operationsStack.isEmpty()) {
            formulaRPN.add(operationsStack.pop());
        }

//        System.out.println(formulaRPN.toString());
    }

    private void processFormulaElement(FormulaElement element) {

        if (element.getType().equals(FormulaElementTypes.NUMBER)
                || element.getType().equals(FormulaElementTypes.CONSTANT)) {

            formulaRPN.add(element);

        } else if (element.getType().equals(FormulaElementTypes.OPERATOR)
                || element.getType().equals(FormulaElementTypes.FUNCTION)) {

            if (element.getOperator().equals(SupportedOperators.CLOSE_BRACKET)) {

                popFromStackUntilOpenBracket();

            } else if (element.getOperator().equals(SupportedOperators.OPEN_BRACKET)) {

                operationsStack.add(element);

            } else {

                popFromStackWhileHigherPriority(element);

                operationsStack.add(element);
            }
        }
    }

    private void popFromStackWhileHigherPriority(FormulaElement element) {
        int currentOperatorPriority = element.getOperator().getPriority();
        int previousOperatorPriority = 0;

        if (!operationsStack.isEmpty()) {
            FormulaElement previousOperator = operationsStack.peek();
            previousOperatorPriority = previousOperator.getOperator().getPriority();
        }

        FormulaElement currentOperator;
        while (!operationsStack.isEmpty()
                && previousOperatorPriority >= currentOperatorPriority) {

            currentOperator = operationsStack.pop();

            if (!currentOperator.getOperator().equals(SupportedOperators.CLOSE_BRACKET)) {
                formulaRPN.add(currentOperator);
            }

            if (!operationsStack.isEmpty()) {
                FormulaElement previousOperator = operationsStack.peek();
                previousOperatorPriority = previousOperator.getOperator().getPriority();
            }
        }

    }

    private void popFromStackUntilOpenBracket() {
        while (!operationsStack.isEmpty()) {

            FormulaElement currentOperator = operationsStack.pop();
            if (currentOperator.getOperator().equals(SupportedOperators.OPEN_BRACKET)) {
                break;
            }
            formulaRPN.add(currentOperator);

        }
    }

    private void initOperationsTextForm() {

        operationTextForm = new HashMap<>();
        operationTextForm.put("(", SupportedOperators.OPEN_BRACKET);
        operationTextForm.put(")", SupportedOperators.CLOSE_BRACKET);
        operationTextForm.put("+", SupportedOperators.PLUS);
        operationTextForm.put("-", SupportedOperators.MINUS);
        operationTextForm.put("*", SupportedOperators.MULT);
        operationTextForm.put("/", SupportedOperators.DIV);
        operationTextForm.put("^", SupportedOperators.POW);
        operationTextForm.put("sqrt", SupportedOperators.SQRT);
        operationTextForm.put("sin", SupportedOperators.SIN);
        operationTextForm.put("cos", SupportedOperators.COS);
        operationTextForm.put("tn", SupportedOperators.TN);
        operationTextForm.put("ln", SupportedOperators.LN);
        operationTextForm.put("lg", SupportedOperators.LG);
    }

    private SymbolCategory getSymbolCategory(char symbol) {
        SymbolCategory result;

        if (isNumeric(symbol)) {
            result = SymbolCategory.NUMBER;
        } else if (symbol == '(') {
            result = SymbolCategory.OPEN_BRACKET;
        } else if (symbol == ')') {
            result = SymbolCategory.CLOSE_BRACKET;
        } else if (symbol == '+'
                || symbol == '-'
                || symbol == '/'
                || symbol == '*'
                || symbol == '^') {
            result = SymbolCategory.OPERATOR;
        } else {
            result = SymbolCategory.OTHER;
        }

        return result;
    }

    private boolean isNumeric(char symbol) {
        boolean result = false;
        int symbolCode = (int) symbol;

        if (symbolCode >= 48 && symbolCode <= 57) {
            result = true;
        }

        if (symbol == '.') {
            result = true;
        }

        return result;
    }

    enum SymbolCategory {NUMBER, OPEN_BRACKET, CLOSE_BRACKET, OPERATOR, OTHER, NA}

    private class FormulaTextBlock {
        public String text;
        public SymbolCategory type;

        public FormulaTextBlock(String textBlock, SymbolCategory textType) {
            this.text = textBlock;
            this.type = textType;
        }

        @Override
        public String toString() {
            return "{" + text + ", " + type + '}';
        }
    }

    public List<FormulaTextBlock> getParsedFormulaText() {
        return parsedFormulaText;
    }

    public List<FormulaElement> getFormulaIN() {
        return formulaIN;
    }

    public List<FormulaElement> getFormulaRPN() {
        return formulaRPN;
    }

}