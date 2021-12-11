package com.emulator;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Worker {

    public static String OPENED_FILE = "NONE";
    private static String TEXT_FROM_FILE = "";
    private static DefaultListModel<String> lProgram = new DefaultListModel<>();
    private static final ArrayList<String> pcr = new ArrayList<>();
    private static final ArrayList<Command> commands = new ArrayList<>();
    private static int commandDone = 0;

    public static void getTextFromFile() {
        try {
            TEXT_FROM_FILE = "";
            String buffer;
            BufferedReader input = new BufferedReader(new FileReader(OPENED_FILE));

            while ((buffer = input.readLine()) != null) {
                TEXT_FROM_FILE += buffer + "\n";
            }

            System.out.println("TEXT:\n" + TEXT_FROM_FILE);

            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyDataToProgram() {
        if (OPENED_FILE.equals("")) return;
        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(OPENED_FILE));
            String s, lineNumber;
            lProgram.clear();
            int i = 0;
            long index;
            char[] array =
                    { '0', '0', '0', '0' };
            while ((s = inputFile.readLine()) != null) {
                if (!s.equals("")) {
                    for (int j = 0; j < 4; ++j) {
                        index = ((long)(i * 4L / Math.pow(16, 3 - j))) % 16;
                        if (index > 9) {
                            array[j] = (char)('A' + index - 10);
                        }
                        else {
                            array[j] = (char)('0' + index);
                        }
                    }
                    lineNumber = "0x" + String.copyValueOf(array);
                    pcr.add(lineNumber);
                    lProgram.add(i, s);
                    ++i;
                }
            }
            inputFile.close();
            System.out.println("pcr:\n" + pcr);
            System.out.println("lprogram:\n" + lProgram);
            //WorkManager.parseCommand();
            Instructions.setInstructions(lProgram);
        }
        catch (IOException e) {
            lProgram.add(0, "IOExeception");
        }
    }

    public static Command parseCommand(String stringCommand) {

            Command currentCommand = new Command();
            String buffer = stringCommand;
            int stopPosition = -1;

            for (int j = 0; j < buffer.length(); ++j) {
                if (buffer.charAt(j) == ' ') {
                    String operator = buffer.substring(0, j + 1);
                    operator = operator.replaceAll("\\s+","");
                    currentCommand.setOperator(operator);
                    stopPosition = j + 1;
                    break;
                }
            }

            for (int j = stopPosition; j < buffer.length(); ++j) {
                if (buffer.charAt(j) == ' ') {
                    String firstOperand = buffer.substring(stopPosition, j + 1);
                    firstOperand = firstOperand.replaceAll("\\s+","");
                    currentCommand.setFirstOperand(firstOperand);
                    stopPosition = j + 1;
                    break;
                }
            }

            for (int j = stopPosition; j < buffer.length(); ++j) {
                if (buffer.charAt(j) == ' ') {
                    String secondOperand = buffer.substring(stopPosition, j + 1);
                    secondOperand = secondOperand.replaceAll("\\s+","");
                    currentCommand.setSecondOperand(secondOperand);
                    stopPosition = j + 1;
                    break;
                }
            }

            for (int j = stopPosition; j < buffer.length(); ++j) {
                if (buffer.charAt(j) == ' ') {
                    String thirdOperand = buffer.substring(stopPosition, j + 1);
                    thirdOperand = thirdOperand.replaceAll("\\s+","");
                    currentCommand.setThirdOperand(thirdOperand);
                    break;
                }
            }

            commands.add(currentCommand);


        for (Command command : commands) System.out.println(command);
        return currentCommand;
    }

    public static boolean runAction(UI UI){
        boolean canDo = (commandDone < (Instructions.tableCommands.getRowCount()));

        if (canDo) {
            String currentPcrValue = pcr.get(commandDone);
            UI.getSecondGroupSystemRegisters().updateCounter(currentPcrValue);
            Instructions.counter(commandDone);
            executeCommand(UI, commandDone);
            ++commandDone;
        }

        return canDo;
    }

    private static void resetData() {
        commandDone = 0;
        pcr.clear();
        commands.clear();
        lProgram.clear();
        lProgram = new DefaultListModel<>();
        Instructions.setInstructions(lProgram);
        TEXT_FROM_FILE = "";
        OPENED_FILE = "NONE";
    }

    public static void resetSystem(UI UI) {
        System.out.println("Reset System!");
        resetGeneralRegisters(UI);
        resetFloatGeneralRegisters(UI);
        resetSystemRegisters(UI);
        resetProcessorFlags(UI);
        resetFloatProcessorFlags(UI);
        resetTLB(UI);
        resetMemory(UI);
        resetInstructions(UI);
        resetData();
    }

    public static void resetGeneralRegisters(UI UI)
    {
        UI.getGeneralRegisters().resetAll();
    }

    public static void resetFloatGeneralRegisters(UI UI) {
        UI.getFloatGeneralRegisters().resetAll();
    }

    public static void resetSystemRegisters(UI UI) {
        UI.getFirstGroupSystemRegisters().resetAll();
        UI.getSecondGroupSystemRegisters().resetAll();
        UI.getThirdGroupSystemRegisters().resetAll();
    }

    public static void resetProcessorFlags(UI UI)
    {
        UI.getProcessorFlags().resetAll();
    }

    public static void resetFloatProcessorFlags(UI UI)
    {
        UI.getFloatProcessorFlags().resetAll();
    }

    public static void resetTLB(UI UI)
    {
        UI.getTlb().initialSet();
    }

    public static void resetMemory(UI UI)
    {
        UI.getMemory().initialSet();
    }

    public static void resetInstructions(UI UI)
    {
        UI.getInstructions().initialSet();
    }

    private static void executeCommand(UI UI, int index) {
        String commandFromTable = Instructions.tableCommands.getModel().getValueAt(index, 0).toString();
        Command command = parseCommand(commandFromTable);
        String operator = commands.get(index).getOperator();

        String strOperand1 = commands.get(index).getFirstOperand();
        String strOperand2 = commands.get(index).getSecondOperand();
        String strOperand3 = commands.get(index).getThirdOperand();


        int operand1 = Integer.parseInt(strOperand1.substring(2), 16);
        int operand2 = strOperand2.length() > 0 ? Integer.parseInt(strOperand2.substring(2), 16) : 0;
        int operand3 = strOperand3.length() > 0 ? Integer.parseInt(strOperand3.substring(2), 16) : 0;

        String rValue, mValue, mValue1, mValue2, mValueHalfword, rValueHalfword,
                rValue1, rValue2, r0v, rv, s;
        int mValueInt, mValueIntHalfword, rValueInt, rValueIntHalfword, r1, r2, r, indicator;

        switch (operator) {
            case "MOV" -> {
                rValue = UI.getGeneralRegisters().regValue(operand2);
                UI.getGeneralRegisters().updateRegister(operand1, rValue);
            }
            case "LOH" -> {
                mValue = UI.getMemory().getValue(operand2);
                mValueInt = HexToInt(mValue);
                mValueIntHalfword = mValueInt & 65535;
                mValueHalfword = IntToHex(mValueIntHalfword);
                UI.getGeneralRegisters().updateRegister(operand1, mValueHalfword);
            }
            case "LOW" -> {
                mValue = UI.getMemory().getValue(operand2);
                UI.getGeneralRegisters().updateRegister(operand1, mValue);
            }
            case "LODW" -> {
                mValue1 = UI.getMemory().getValue(operand2);
                mValue2 = UI.getMemory().getValue(operand2 + 1);
                UI.getGeneralRegisters().updateRegister(operand1, mValue1);
                UI.getGeneralRegisters().updateRegister(operand1 + 1, mValue2);
            }
            case "STH" -> {
                rValue = UI.getGeneralRegisters().regValue(operand2);
                rValueInt = HexToInt(rValue);
                rValueIntHalfword = rValueInt & 65535;
                rValueHalfword = IntToHex(rValueIntHalfword);
                UI.getMemory().setValue(operand1, rValueHalfword);
            }
            case "STW" -> {
                rValue = UI.getGeneralRegisters().regValue(operand2);
                UI.getMemory().setValue(operand1, rValue);
            }
            case "STDW" -> {
                rValue1 = UI.getGeneralRegisters().regValue(operand2);
                rValue2 = UI.getGeneralRegisters().regValue(operand2 + 1);
                UI.getMemory().setValue(operand1, rValue1);
                UI.getMemory().setValue(operand1 + 1, rValue2);
            }
            case "MOVC" -> {
                strOperand2 = IntToHex(HexToInt(strOperand2));
                UI.getGeneralRegisters().updateRegister(operand1, strOperand2);
            }
            case "MOVB" -> {
                mValue1 = UI.getMemory().getValue(operand2 + operand3);
                mValue2 = UI.getMemory().getValue(operand2 + operand3 + 1);
                UI.getGeneralRegisters().updateRegister(operand1, mValue1);
                UI.getGeneralRegisters().updateRegister(operand1 + 1, mValue2);
            }
            case "ADD" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 + r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "SUB" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 - r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "MUL", "MULU" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 * r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "DIV", "DIVU" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 / r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "CMP" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 - r2);
            }
            case "AND" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 & r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "OR" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 | r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "XOR" -> {
                r1 = HexToInt(UI.getGeneralRegisters().regValue(operand2));
                r2 = HexToInt(UI.getGeneralRegisters().regValue(operand3));
                r0v = IntToHex(r1 ^ r2);
                UI.getGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "SHL" -> {
                r = HexToInt(UI.getGeneralRegisters().regValue(operand1));
                rv = IntToHex(r << 1);
                UI.getGeneralRegisters().updateRegister(operand1, rv);
            }
            case "SHR" -> {
                r = HexToInt(UI.getGeneralRegisters().regValue(operand1));
                rv = IntToHex(r >> 1);
                UI.getGeneralRegisters().updateRegister(operand1, rv);
            }
            case "FADD" -> {
                r1 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand2));
                r2 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand3));
                r0v = IntToHex(r1 + r2);
                UI.getFloatGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "FSUB" -> {
                r1 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand2));
                r2 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand3));
                r0v = IntToHex(r1 - r2);
                UI.getFloatGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "FMUL" -> {
                r1 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand2));
                r2 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand3));
                r0v = IntToHex(r1 * r2);
                UI.getFloatGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "FDIV" -> {
                r1 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand2));
                r2 = HexToInt(UI.getFloatGeneralRegisters().getRegisterValue(operand3));
                r0v = IntToHex(r1 / r2);
                UI.getFloatGeneralRegisters().updateRegister(operand1, r0v);
            }
            case "INT" -> {
                indicator = UI.getProcessorFlags().valueOfFlag(5);
                if (indicator == 0) {
                    JOptionPane.showMessageDialog(null, "Interrupts are impossible", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (indicator == 1) {
                    System.out.println(" " + strOperand1.length());
                    s = pcr.get(commandDone + 1);
                    UI.getSecondGroupSystemRegisters().updateRegister(2, s);
                    UI.getSecondGroupSystemRegisters().updateRegister(3, UI.getProcessorFlags().flags());

                    JOptionPane.showMessageDialog(null, "Call the " + operand1 + " interrupt", "Information", JOptionPane.PLAIN_MESSAGE);
                }
            }
            case "RET" -> {
                UI.getSecondGroupSystemRegisters().updateRegister(2, "0x00000");
                UI.getSecondGroupSystemRegisters().updateRegister(3, "0x00000");
            }
            case "CLRI" -> UI.getProcessorFlags().updateFlag(5, "0");
            case "SETI" -> UI.getProcessorFlags().updateFlag(5, "1");
            case "JZ" -> UI.getProcessorFlags().updateFlag(3, "1");
            case "JNZ" -> UI.getProcessorFlags().updateFlag(3, "0");
            case "JO" -> UI.getProcessorFlags().updateFlag(1, "1");
            case "JNO" -> UI.getProcessorFlags().updateFlag(1, "0");
            case "JS" -> UI.getProcessorFlags().updateFlag(2, "1");
            case "JNS" -> UI.getProcessorFlags().updateFlag(2, "0");
            case "JC" -> UI.getProcessorFlags().updateFlag(0, "1");
            case "JCO" -> UI.getProcessorFlags().updateFlag(0, "0");
            case "RTLBR" -> s = UI.getThirdGroupSystemRegisters().readRegister(1);
            case "RBVA" -> s = UI.getThirdGroupSystemRegisters().readRegister(2);
            case "RFLG" -> s = UI.getSecondGroupSystemRegisters().readRegister(1);
            case "RRFLG" -> s = UI.getSecondGroupSystemRegisters().readRegister(2);
            case "CALL" -> UI.getProcessorFlags().updateFlag(6, "0");
            case "RFE" -> UI.getProcessorFlags().updateFlag(6, "1");
        }
    }

    public static int HexToInt(String hex) {
        int i;
        int res = 0;
        if (hex.indexOf("0x") == 0) hex = hex.substring(2);

        for (i = 0; i < hex.length(); ++i)
        {
            char Symbol = 0;
            Symbol = hex.charAt(i);

            int SymbolValue = Character.digit(Symbol, 16);
            res += SymbolValue * Math.pow(16, hex.length() - 1 - i);
        }

        return res;
    }

    public static String IntToHex(int value) {
        String hex = "0x";
        if (value >= 0) {
            long index;
            for (int i = 0; i < 8; ++i) {
                index = (long)(value / Math.pow(16, 8 - 1 - i)) % 16;
                if (index > 9)
                    hex += (char)('A' + index - 10);
                else
                    hex += (char)(index + '0');
            }

        }
        return hex;
    }
}