package com.emulator.Registers;

import javax.swing.*;

public class _FirstGroupSystemRegisters extends JTable {

    static String[] colNames =
            { "", "" };

    static String[][] data = {
            { "SR0", "" },
            { "SR1", "" },
            { "SR2", "" },
            { "SR3", "" }};

    public _FirstGroupSystemRegisters()
    {
        super(data, colNames);
        getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void setValue(String registerName, String value)
    {
        String buffer;

        for (byte i = 0; i < 4; i++)
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

    public void resetAll()
    {
        for (byte i = 0; i < 4; i++)
        {
            data[i][1] = "";
        }
        this.updateUI();
    }
}
