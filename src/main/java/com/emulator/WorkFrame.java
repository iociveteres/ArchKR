package com.emulator;

import com.emulator.Registers.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class WorkFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 850;

    private final JFileChooser fileChooser = new JFileChooser();
    private final JToolBar bottomToolbar = new JToolBar();
    private final JButton stepButton = new JButton("Шаг");
    private final JButton runButton = new JButton("Выполнить");


    private final GeneralRegisters generalRegisters = new GeneralRegisters();
    private final FloatGeneralRegisters floatGeneralRegisters = new FloatGeneralRegisters();
    private final FirstGroupSystemRegisters firstGroupSystemRegisters = new FirstGroupSystemRegisters();
    private final SecondGroupSystemRegisters secondGroupSystemRegisters = new SecondGroupSystemRegisters();
    private final ThirdGroupSystemRegisters thirdGroupSystemRegisters = new ThirdGroupSystemRegisters();
    private final ProcessorFlags processorFlags = new ProcessorFlags();
    private final FloatProcessorFlags floatProcessorFlags = new FloatProcessorFlags();
    private final TLB tlb = new TLB();
    private final Memory memory = new Memory();
    private final Instructions instructions = new Instructions();

    ActionListener programExit = e -> {
        Object[] options = { "Выйти", "Отмена" };
        int selectedValue = JOptionPane.showOptionDialog(null, "Вы действительно хотите выйти?",
                "Выход из программы", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if(selectedValue == 0)
            System.exit(0);
    };

    ActionListener open = e -> {
        System.out.println("ActionListener.actionPerformed : open");
        JFrame container = new JFrame();
        fileChooser.showOpenDialog(container);
        try {
            WorkManager.OPENED_FILE = fileChooser.getSelectedFile().getAbsolutePath();

            System.out.println(" " + WorkManager.OPENED_FILE);
            if (!Objects.equals(WorkManager.OPENED_FILE, "")) {
                WorkManager.getTextFromFile();
            }

            WorkManager.copyDataToProgram();
        }
        catch (NullPointerException exception) {
            System.out.println("No file chosen: " + exception.getMessage());
        }

        container.dispose();
    };

    ActionListener reset = e -> {
        System.out.println("ActionListener.actionPerformed : reset");
        stepButton.setEnabled(true);
        runButton.setEnabled(true);
        WorkManager.resetSystem(WorkFrame.this);
        instructions.initialSet();
    };

    ActionListener showHelp = e -> {
        Object[] options = { "Ок" };
        String text =
                """
                        Данная программа позволяет эмулировать работу RISC-процессора.
                        Для того, чтобы загрузить программу, нажмите "Открыть".
                        Чтобы сбросить состояние эмулятора в исходное, нажмите "Сброс".
                        Чтобы выполнить программу полностью, нажмите "Выполнить".
                        Чтобы выполнить очередную инструкцию программы пошагово, нажмите "Шаг".
                        Чтобы изменить инструкцию после загрузки в программу, нажмите на неё в окне программы и отредактируйте её.
                        Чтобы выйти из программы, нажмите "Выход".
                 """;
        int selectedValue = JOptionPane.showOptionDialog(null, text,
                "Помощь", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
    };

    public WorkFrame() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setBounds(screenSize.width/2 - DEFAULT_WIDTH/2, screenSize.height/2 - DEFAULT_HEIGHT/2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenuBar menuBar = new JMenuBar();
        //menuBar.add(createFileMenu());
        JButton openButton = new JButton("Открыть");
        openButton.setBorder(BorderFactory.createEtchedBorder());
        openButton.addActionListener(open);
        menuBar.add(openButton);

        JButton resetButton = new JButton("Сброс");
        resetButton.setBorder(BorderFactory.createEtchedBorder());
        resetButton.addActionListener(reset);
        menuBar.add(resetButton);

        JButton help = new JButton("Помощь");
        help.setBorder(BorderFactory.createEtchedBorder());
        help.addActionListener(showHelp);
        menuBar.add(help);

        JButton exit = new JButton("Выход");
        exit.setBorder(BorderFactory.createEtchedBorder());
        exit.addActionListener(programExit);
        menuBar.add(exit);

        setJMenuBar(menuBar);

        fileChooser.setBounds(DEFAULT_WIDTH/2, DEFAULT_HEIGHT/2, 150, 150);
        fileChooser.setAcceptAllFileFilterUsed(false);

        bottomToolbar.setFloatable(false);

        addComponents();

        addActionListeners();

    }

    private void addComponents() {

        JPanel PanelTLB = new JPanel(new BorderLayout());
        JPanel PanelMemory = new JPanel(new BorderLayout());
        JPanel PanelInstructions = new JPanel(new BorderLayout());
        JPanel PanelProcessorFlags = new JPanel(new BorderLayout());

        PanelTLB.setBorder(new TitledBorder(new EtchedBorder(), "TLB"));
        PanelMemory.setBorder(new TitledBorder(new EtchedBorder(), "Память"));
        PanelInstructions.setBorder(new TitledBorder(new EtchedBorder(), "Инструкции"));
        PanelInstructions.setMaximumSize( new Dimension(
                200,
                Integer.MAX_VALUE
        ) );
        PanelProcessorFlags.setBorder(new TitledBorder(new EtchedBorder(), "Флаги процессора"));

        JPanel PanelGPRegisters = new JPanel(new BorderLayout());
        JPanel PanelFloatGPRegisters = new JPanel(new BorderLayout());
        JPanel PanelSystemRegisters = new JPanel(new BorderLayout());
        JPanel PanelFloatFlags = new JPanel(new BorderLayout());

        PanelGPRegisters.setBorder(new TitledBorder(new EtchedBorder(), "Регистры общего назначения"));
        PanelFloatGPRegisters.setBorder(new TitledBorder(new EtchedBorder(), "Float-регистры общего назначения"));
        PanelSystemRegisters.setBorder(new TitledBorder(new EtchedBorder(), "Системные регистры"));
        PanelFloatFlags.setBorder(new TitledBorder(new EtchedBorder(), "Float-флаги процессора"));

        Box BoxGeneralRegisters = Box.createVerticalBox();
        BoxGeneralRegisters.add(Box.createVerticalStrut(5));
        BoxGeneralRegisters.add(generalRegisters);
        BoxGeneralRegisters.add(Box.createVerticalGlue());
        PanelGPRegisters.add(BoxGeneralRegisters, BorderLayout.CENTER);


        Box BoxProcessorFlags = Box.createVerticalBox();
        BoxProcessorFlags.add(Box.createVerticalStrut(5));
        BoxProcessorFlags.add(processorFlags);
        BoxProcessorFlags.add(Box.createVerticalGlue());
        PanelProcessorFlags.add(BoxProcessorFlags, BorderLayout.CENTER);

        Box BoxFloatGeneralRegisters = Box.createVerticalBox();
        BoxFloatGeneralRegisters.add(Box.createVerticalStrut(5));
        BoxFloatGeneralRegisters.add(floatGeneralRegisters);
        BoxFloatGeneralRegisters.add(Box.createVerticalGlue());
        PanelFloatGPRegisters.add(BoxFloatGeneralRegisters, BorderLayout.CENTER);

        Box BoxFloatProcessorFlags = Box.createVerticalBox();
        BoxFloatProcessorFlags.add(Box.createVerticalStrut(5));
        BoxFloatProcessorFlags.add(floatProcessorFlags);
        BoxFloatProcessorFlags.add(Box.createVerticalGlue());
        PanelFloatFlags.add(BoxFloatProcessorFlags, BorderLayout.NORTH);

        Box Int = Box.createVerticalBox();
        Int.add(PanelProcessorFlags);
        Int.add(Box.createVerticalStrut(5));
        Int.add(PanelGPRegisters);

        Box Float = Box.createVerticalBox();
        Float.add(PanelFloatFlags);
        Float.add(Box.createVerticalStrut(5));
        Float.add(PanelFloatGPRegisters);

        Box Instructions = Box.createVerticalBox();
        PanelInstructions.add(instructions, BorderLayout.CENTER);

        JPanel Buttons = new JPanel();
        Buttons.add(stepButton);
        Buttons.add(runButton);

        PanelInstructions.add(Buttons, BorderLayout.SOUTH);
        Instructions.add(PanelInstructions);
        Instructions.add(Box.createVerticalStrut(5));

        PanelTLB.add(tlb, BorderLayout.CENTER);

        PanelMemory.add(memory, BorderLayout.CENTER);

        Box downRightCenterElement = Box.createVerticalBox();
        downRightCenterElement.add(Box.createVerticalStrut(5));
        downRightCenterElement.add(firstGroupSystemRegisters);
        downRightCenterElement.add(secondGroupSystemRegisters);
        downRightCenterElement.add(thirdGroupSystemRegisters);
        PanelSystemRegisters.add(downRightCenterElement, BorderLayout.CENTER);


        Container content = getContentPane();
        content.setLayout(new GridLayout(2, 3, 5,5));
        content.add(Int);
        content.add(Float);
        content.add(PanelInstructions);
        content.add(PanelTLB);
        content.add(PanelMemory);
        content.add(PanelSystemRegisters);
    }


    private void addActionListeners() {
        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File fileName) {
                return fileName.getAbsolutePath().toLowerCase().endsWith(".txt") | (fileName.isDirectory());
            }

            public String getDescription() {
                return "Text files";
            }
        };
        fileChooser.setFileFilter(fileFilter);

        stepButton.addActionListener(e -> {
            boolean endOfCommands = !WorkManager.runAction(WorkFrame.this);
            if(endOfCommands) {
                JOptionPane.showMessageDialog(null, "Команды выполнены!");
                stepButton.setEnabled(false);
                runButton.setEnabled(false);
            }
        });

        runButton.addActionListener(e -> {
            boolean endOfCommands = true;
            do {
                endOfCommands = !WorkManager.runAction(WorkFrame.this);
                if (endOfCommands) {
                    JOptionPane.showMessageDialog(null, "Команды выполнены!");
                    stepButton.setEnabled(false);
                    runButton.setEnabled(false);
                }
            } while (!endOfCommands);
        });
    }

    public GeneralRegisters getGeneralRegisters() {
        return generalRegisters;
    }

    public FloatGeneralRegisters getFloatGeneralRegisters() {
        return floatGeneralRegisters;
    }

    public FirstGroupSystemRegisters getFirstGroupSystemRegisters() {
        return firstGroupSystemRegisters;
    }

    public SecondGroupSystemRegisters getSecondGroupSystemRegisters() {
        return secondGroupSystemRegisters;
    }

    public ThirdGroupSystemRegisters getThirdGroupSystemRegisters() {
        return thirdGroupSystemRegisters;
    }

    public ProcessorFlags getProcessorFlags() {
        return processorFlags;
    }

    public FloatProcessorFlags getFloatProcessorFlags() {
        return floatProcessorFlags;
    }

    public TLB getTlb() {
        return tlb;
    }

    public Memory getMemory() {
        return memory;
    }

    public Instructions getInstructions() {
        return instructions;
    }
}
