package com.emulator;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame frame = new WorkFrame();
            frame.setTitle("Мохнаткин, Волконенков, Сергеев, вариант 9");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(false);
        });
    }
}
