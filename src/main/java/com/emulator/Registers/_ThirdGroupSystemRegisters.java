package com.emulator.Registers;

import javax.swing.*;

public class _ThirdGroupSystemRegisters extends JTable {

    static String[] colNames =
            { "", "" };

    static String[][] data = {
            { "ITP", "0x0000" },
            { "PTP", "0x0000" },
            { "BVA", "0x0000" },
            { "BDP", "0x0000" }};


    public _ThirdGroupSystemRegisters() {
        super(data, colNames);
        getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void setValue(String registerName, String value) {
        String buffer;

        for (byte i = 0; i < 3; i++)
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
        for (byte i = 0; i < 4; i++)
            data[i][1] = "0x0000";

        this.updateUI();
    }

    public String readRegister(int index) {
        return data[index][1];
    }

}
