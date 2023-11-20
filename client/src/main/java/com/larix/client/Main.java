package com.larix.client;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class Main {

    public static void main(final String[] args) {
        createLookAndFeel();
        new EspressoClient().start();
    }

    private static void createLookAndFeel() {
        //FlatIntelliJLaf.setup();
        FlatDarculaLaf.setup();
        UIManager.put("JScrollPane.arc", 16);
        UIManager.put("JTextArea.arc", 16);
    }

}