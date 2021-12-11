package com.emulator;

import javax.swing.*;

public class IntegerProcessorFlags extends JTable {

    static String[] colNames =
            { "", "", "", "", "", "", "" };

    static String[][] data = {
            { "C", "O", "S", "Z", "T", "I", "U" },
            { "0", "0", "0", "0", "0", "0", "0" } };

    public IntegerProcessorFlags() {
        super(data, colNames);
        for(int i = 0; i < 7; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(20);
        }
    }

    public void setFlag(String flagName, boolean state) {
        String buffer, buffer2;

        buffer = state ? "1" : "0";

        for (byte i = 0; i < 7; i++) {
            buffer2 = data[0][i];
            if (buffer2.equalsIgnoreCase(flagName)) {
                data[1][i] = buffer;
                this.updateUI();
                break;
            }
        }

    }

    public void resetAll() {
        for (byte i = 0; i < 7; i++)
        {
            data[1][i] = "0";
        }
        this.updateUI();
    }

    public void updateFlag(int index, String s) {
        data[1][index] = s;
        this.updateUI();
    }

    public int valueOfFlag(int index)
    {
        return Integer.parseInt(data[1][index]);
    }

    public String flags() {
        StringBuilder s = new StringBuilder();
        String current;

        for (byte i = 0; i < 7; i++) {
            current = data[1][i];
            s.append(current);
        }

        return "0x" + s;
    }
}
