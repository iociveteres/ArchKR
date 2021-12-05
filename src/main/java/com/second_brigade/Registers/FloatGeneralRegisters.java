package com.second_brigade.Registers;

import javax.swing.*;
import java.io.Serial;

public class FloatGeneralRegisters extends JTable {

    @Serial
    private static final long serialVersionUID = 1235755361786911970L;

    static String[] colNames =
            {"", ""};

    static String[][] data = {
            {"FR0", ""}, {"FR1", ""}, {"FR2", ""}, {"FR3", ""},
            {"FR4", ""}, {"FR5", ""}, {"FR6", ""}, {"FR7", ""},
            {"FR8", ""}, {"FR9", ""}, {"FR10", ""}, {"FR11", ""},
            {"FR12", ""}, {"FR13", ""}, {"FR14", ""}, {"FR15", ""}};

    public FloatGeneralRegisters() {
        super(data, colNames);
        getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void setValue(String registerName, String value) {
        String buffer;

        for (byte i = 0; i < 7; i++) {
            buffer = data[i][0];
            if (buffer.equalsIgnoreCase(registerName)) {
                data[i][1] = value;
                this.updateUI();
                break;
            }
        }
    }

    public void resetAll() {
        for (byte i = 0; i < 9; i++) {
            data[i][1] = "";
        }
        this.updateUI();
    }

    public void updateRegister(int number, String value) {
        data[number][1] = value;
        this.updateUI();
    }

    public String getRegisterValue(int number) {
        return data[number][1];
    }
}
