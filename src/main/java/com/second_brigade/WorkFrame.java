package com.second_brigade;

import com.second_brigade.Registers.*;

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
    private static final int DEFAULT_HEIGHT = 550;

    private final JFileChooser fileChooser = new JFileChooser();
    private final JToolBar bottomToolbar = new JToolBar();
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

    public WorkFrame() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setBounds(screenSize.width/2 - DEFAULT_WIDTH/2, screenSize.height/2 - DEFAULT_HEIGHT/2, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        JButton exit = new JButton();
        exit.setText("Выход");
        exit.setBorder(BorderFactory.createEtchedBorder());
        exit.addActionListener(programExit);
        menuBar.add(exit);
        setJMenuBar(menuBar);

        createFileMenu();

        fileChooser.setBounds(DEFAULT_WIDTH/2, DEFAULT_HEIGHT/2, 150, 150);
        fileChooser.setAcceptAllFileFilterUsed(false);

        bottomToolbar.setFloatable(false);

        addComponents();

        addActionListeners();

    }

    private void addComponents() {

        JPanel leftTopPanel = new JPanel(new BorderLayout());
        JPanel leftCenterTopPanel = new JPanel(new BorderLayout());
        JPanel rightCenterTopPanel = new JPanel(new BorderLayout());
        JPanel rightTopPanel = new JPanel(new BorderLayout());

        leftTopPanel.setBorder(new TitledBorder(new EtchedBorder(), "TLB"));
        leftCenterTopPanel.setBorder(new TitledBorder(new EtchedBorder(), "Память"));
        rightCenterTopPanel.setBorder(new TitledBorder(new EtchedBorder(), "Инструкции"));
        rightTopPanel.setBorder(new TitledBorder(new EtchedBorder(), "Флаги процессора"));

        JPanel leftDownPanel = new JPanel(new BorderLayout());
        JPanel leftCenterDownPanel = new JPanel(new BorderLayout());
        JPanel rightCenterDownPanel = new JPanel(new BorderLayout());
        JPanel rightDownPanel = new JPanel(new BorderLayout());

        leftDownPanel.setBorder(new TitledBorder(new EtchedBorder(), "Регистры общего назначения"));
        leftCenterDownPanel.setBorder(new TitledBorder(new EtchedBorder(), "Float-регистры общего назначения"));
        rightCenterDownPanel.setBorder(new TitledBorder(new EtchedBorder(), "Системные регистры"));
        rightDownPanel.setBorder(new TitledBorder(new EtchedBorder(), "Float-флаги процессора"));

        Box horizontalTop = Box.createHorizontalBox();

        horizontalTop.add(Box.createHorizontalStrut(5));
        leftTopPanel.add(tlb, BorderLayout.CENTER);
        horizontalTop.add(leftTopPanel);

        horizontalTop.add(Box.createHorizontalStrut(5));
        leftCenterTopPanel.add(memory, BorderLayout.CENTER);
        horizontalTop.add(leftCenterTopPanel);

        horizontalTop.add(Box.createHorizontalStrut(5));
        rightCenterTopPanel.add(instructions, BorderLayout.CENTER);
        horizontalTop.add(rightCenterTopPanel);

        horizontalTop.add(Box.createHorizontalStrut(5));
        rightTopPanel.add(processorFlags);
        horizontalTop.add(rightTopPanel, BorderLayout.CENTER);


        Box horizontalDown = Box.createHorizontalBox();
        horizontalDown.add(Box.createHorizontalStrut(5));

        Box downLeftElement = Box.createVerticalBox();
        downLeftElement.add(Box.createVerticalStrut(5));
        downLeftElement.add(generalRegisters);
        downLeftElement.add(Box.createVerticalGlue());
        leftDownPanel.add(downLeftElement, BorderLayout.CENTER);

        Box downLeftCenterElement = Box.createVerticalBox();
        downLeftCenterElement.add(Box.createVerticalStrut(5));
        downLeftCenterElement.add(floatGeneralRegisters);
        downLeftCenterElement.add(Box.createVerticalGlue());
        leftCenterDownPanel.add(downLeftCenterElement, BorderLayout.CENTER);

        Box downRightCenterElement = Box.createVerticalBox();
        downRightCenterElement.add(Box.createVerticalStrut(5));
        downRightCenterElement.add(firstGroupSystemRegisters);
        downRightCenterElement.add(Box.createVerticalStrut(5));
        downRightCenterElement.add(secondGroupSystemRegisters);
        downRightCenterElement.add(Box.createVerticalStrut(5));
        downRightCenterElement.add(thirdGroupSystemRegisters);
        rightCenterDownPanel.add(downRightCenterElement, BorderLayout.CENTER);

        Box downRightElement = Box.createVerticalBox();
        downRightElement.add(Box.createVerticalStrut(5));
        downRightElement.add(floatProcessorFlags);
        downRightElement.add(Box.createVerticalGlue());
        rightDownPanel.add(downRightElement, BorderLayout.NORTH);

        horizontalDown.add(leftDownPanel);
        horizontalDown.add(Box.createHorizontalGlue());
        horizontalDown.add(Box.createHorizontalStrut(5));

        horizontalDown.add(leftCenterDownPanel);
        horizontalDown.add(Box.createHorizontalGlue());
        horizontalDown.add(Box.createHorizontalStrut(5));

        horizontalDown.add(rightCenterDownPanel);
        horizontalDown.add(Box.createHorizontalGlue());
        horizontalDown.add(Box.createHorizontalStrut(5));

        horizontalDown.add(rightDownPanel);
        horizontalDown.add(Box.createHorizontalGlue());
        horizontalDown.add(Box.createHorizontalStrut(5));


        Box mainLayout = Box.createVerticalBox();
        mainLayout.add(horizontalTop);
        mainLayout.add(Box.createVerticalStrut(20));
        mainLayout.add(horizontalDown);
        mainLayout.add(Box.createVerticalGlue());
        mainLayout.add(Box.createVerticalStrut(20));


        for (int i = 0; i < 45; ++i) {
            bottomToolbar.addSeparator();
        }
        bottomToolbar.add(runButton);

        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(mainLayout, BorderLayout.CENTER);
        content.add(bottomToolbar, BorderLayout.SOUTH);
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

        runButton.addActionListener(e -> {

            boolean endOfCommands = !WorkManager.runAction(WorkFrame.this);
            if(endOfCommands) {
                JOptionPane.showMessageDialog(null, "Команды закончились!");
                runButton.setEnabled(false);
            }
        });
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть");
        JMenuItem reset = new JMenuItem("Сброс");

        fileMenu.add(open);
        fileMenu.add(reset);
        fileMenu.addSeparator();

        open.addActionListener(e -> {
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
        });

        reset.addActionListener(e -> {
            System.out.println("ActionListener.actionPerformed : reset");
            runButton.setEnabled(true);
            WorkManager.resetSystem(WorkFrame.this);
            instructions.initialSet();
        });

        return fileMenu;
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
