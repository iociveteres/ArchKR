package com.second_brigade;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame frame = new WorkFrame();
            frame.setTitle("RISC-модель (Бубнов Земцов Родионов)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
