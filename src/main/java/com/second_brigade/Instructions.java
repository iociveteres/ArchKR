package com.second_brigade;

import javax.swing.*;
import java.io.Serial;

public class Instructions extends JScrollPane{

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String[] listData = new String[128];
    private static final JList<String> theList = new JList<>(listData);

    {
        initialSet();
    }

    public Instructions()
    {
        super(theList);
    }

    public void initialSet()
    {
        for (int i = 0; i < 12; i++)
        {
            listData[i] = "\t";
        }
        this.updateUI();
    }

    public void setValue(int number, String value)
    {
        listData[number] = value;
        this.updateUI();
    }

    public static void setInstructions(DefaultListModel<String> m) {
        for (int i = 0; i < 12; i++) {
            listData[i] = " ";
        }
        theList.updateUI();

        for (int i = 0; i < m.getSize(); i++)
        {
            listData[i] = "" + m.getElementAt(i);
        }
        theList.validate();
        theList.updateUI();
    }

    public static void counter(int i)
    {
        theList.setSelectedIndex(i);
    }
}
