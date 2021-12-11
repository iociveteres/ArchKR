package com.emulator;

import javax.swing.*;

public class Memory extends JScrollPane{

    private static final String[] listData = new String[128];
    private static final JList theList = new JList(listData);


    {
        initialSet();
    }

    public Memory()
    {
        super(theList);
    }

    public void initialSet() {
        for (int i = 0; i < 128; i++)
        {
            listData[i] = WorkManager.IntToHex((int)(Math.random() * 2147483647));
        }
        this.updateUI();
    }

    public void setValue(int number, String value) {
        listData[number] = value;
        this.updateUI();
    }

    public String getValue(int number)
    {
        return listData[number];
    }
}
