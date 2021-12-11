package com.emulator;

public class Command {

    private String operator = "";
    private String firstOperand = "";
    private String secondOperand = "";
    private String thirdOperand = "";

    public void setOperator(String operator){
        this.operator = operator;
    }

    public String getOperator() {
        return operator;
    }

    public void setFirstOperand(String firstOperand) {
        this.firstOperand = firstOperand;
    }

    public String getFirstOperand() {
        return firstOperand;
    }

    public void setSecondOperand(String secondOperand) {
        this.secondOperand = secondOperand;
    }

    public String getSecondOperand() {
        return secondOperand;
    }

    public void setThirdOperand(String thirdOperand) {
        this.thirdOperand = thirdOperand;
    }

    public String getThirdOperand() {
        return thirdOperand;
    }

    @Override
    public String toString() {
        return operator + " " + firstOperand + " " + secondOperand + " " + thirdOperand;
    }
}
