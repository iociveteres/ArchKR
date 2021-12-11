package com.emulator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.Serial;
import java.util.Vector;

public class Instructions extends JScrollPane{

    @Serial
    private static final long serialVersionUID = 1L;

    private static Vector<Vector<String>> commandsStrings = new Vector<Vector<String>>();
    static Vector<String> columnsHeader = new Vector<String>() {{add("Инструкции");}};
    static DefaultTableModel tableModelCommands = new DefaultTableModel(commandsStrings, columnsHeader);
    static JTable tableCommands = new JTable(tableModelCommands);

    public Instructions()
    {
        super();
        this.setViewportView(tableCommands);;
    }

    public static void setInstructions(DefaultListModel<String> m) {
        /*
        for (int i = 0; i < 12; i++) {
            listData[i] = " ";
        }
        theList.updateUI();
        */
        commandsStrings.clear();
        for (int i = 0; i < m.getSize(); i++)
        {
            commandsStrings.add(i, new Vector<>());
            commandsStrings.get(i).add(m.getElementAt(i));

        }
        tableModelCommands.fireTableDataChanged();
    }

    public static void counter(int i)
    {
        tableCommands.setRowSelectionInterval(i, i);
    }

    public void initialSet() {
        commandsStrings.clear();
        tableModelCommands.fireTableDataChanged();
    }
}
