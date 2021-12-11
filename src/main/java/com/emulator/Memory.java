package com.emulator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class Memory extends JScrollPane{

    private static Vector<Vector<String>> commandsStrings = new Vector<Vector<String>>();
    static Vector<String> columnsHeader = new Vector<String>() {{add("Адрес"); add("Значение");}};
    static DefaultTableModel tableModelMemory = new DefaultTableModel(commandsStrings, columnsHeader);
    static JTable tableMemory = new JTable(tableModelMemory);

    public Memory()
    {
        super();
        this.setViewportView(tableMemory);;
    }

    {
        initialSet();
    }

    public void initialSet() {
        for (int i = 0; i < 128; i++)
        {
            final int a = i;
            tableModelMemory.addRow(new Vector<>() {{add(WorkManager.IntToHex(a)); add(WorkManager.IntToHex(0));}});
        }
        this.updateUI();
        tableModelMemory.fireTableDataChanged();
    }

    public void setValue(int number, String value) {
        tableMemory.getModel().setValueAt(value, number, 1);
        tableModelMemory.fireTableDataChanged();
    }

    public String getValue(int number)
    {
        return tableMemory.getModel().getValueAt(number, 1).toString();
    }
}
