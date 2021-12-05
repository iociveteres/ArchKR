package com.second_brigade.Registers;

import javax.swing.*;

public class SecondGroupSystemRegisters extends JTable {

    static String[] colNames =
            { "", "" };

    static String[][] data = {
            { "PCR", "0x0000" },
            { "FLG", "0x0000" },
            { "FFLG", "0x0000" },
            { "IPC", "0x0000" },
            { "IFL", "0x0000" }};

    public SecondGroupSystemRegisters() {
        super(data, colNames);
        getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void setValue(String registerName, String value) {
        String buffer;

        for (byte i = 0; i < 5; i++)
        {
            buffer = data[i][0];
            if (buffer.equalsIgnoreCase(registerName))
            {
                data[i][1] = value;
                this.updateUI();
                break;
            }
        }
    }

    public void resetAll() {
        for (byte i = 0; i < 5; i++)
        {
            data[i][1] = "0x0000";
        }
        this.updateUI();
    }

    public void updateCounter(String s) {
        data[0][1] = s;
        this.updateUI();
    }

    public void updateRegister(int index, String s) {
        data[index][1] = s;
        this.updateUI();
    }

    public String readRegister(int index) {
        return data[index][1];
    }
}
