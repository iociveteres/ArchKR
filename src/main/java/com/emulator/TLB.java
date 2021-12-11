package com.emulator;

import javax.swing.*;

public class TLB extends JScrollPane{

    private static final String[] listData = new String[128];
    private static final JList theList = new JList(listData);

    {
        initialSet();
    }

    public TLB()
    {
        super(theList);

    }

    public void initialSet() {
        for (int i = 0; i < 128; i++)
        {
            listData[i] = WorkManager.IntToHex((int)(Math.random() * 1000) + 1000);
        }
        this.updateUI();
    }

    public void setValue(int number, String value) {
        listData[number] = value;
        this.updateUI();
    }
}
