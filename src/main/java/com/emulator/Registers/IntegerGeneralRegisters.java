package com.emulator.Registers;

import javax.swing.*;

public class IntegerGeneralRegisters extends JTable {

    static String[] colNames =
            { "", "" };

    static String[][] data = { { "RG0", "" }, { "RG1", "" }, { "RG2", "" }, { "RG3", "" },
            { "RG4", "" }, { "RG5", "" }, { "RG6", "" }, { "RG7", "" },
            { "RG8", "" }, { "RG9", "" }, { "RG10", "" }, { "RG11", "" },
            { "RG12", "" }, { "RG13", "" }, { "RG14", "" }, { "RG15", "" }, };

    public IntegerGeneralRegisters()
    {
        super(data, colNames);
        getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void setValue(String registerName, String value)
    {
        String buffer;

        for (byte i = 0; i < 8; i++)
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
        for (byte i = 0; i < 16; i++)
        {
            data[i][1] = "";
        }
        this.updateUI();
    }

    public void updateRegister(int number, String value)
    {
        data[number][1] = value;
        this.updateUI();
    }

    public String regValue(int number)
    {
        String s = data[number][1];
        return s;
    }
}
