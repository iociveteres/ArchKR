package com.emulator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TLB extends JScrollPane{

    private static Vector<Vector<String>> commandsStrings = new Vector<Vector<String>>();
    static Vector<String> columnsHeader = new Vector<String>() {{add("Адрес"); add("Значение");}};
    static DefaultTableModel tableModelTLB = new DefaultTableModel(commandsStrings, columnsHeader);
    static JTable tableTLB = new JTable(tableModelTLB);

    public TLB()
    {
        super();
        this.setViewportView(tableTLB);;
    }

    {
        initialSet();
    }

    public void initialSet() {
        for (int i = 0; i < 128; i++)
        {
            final int a = i;
            tableModelTLB.addRow(new Vector<>() {{add(WorkManager.IntToHex(a)); add(WorkManager.IntToHex(0));}});
        }
        this.updateUI();
        tableModelTLB.fireTableDataChanged();
    }
}
