package com.larix.client;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {

    public static void main(final String[] args) {
        createLookAndFeel();
        new EspressoClient().start();
    }

    private static void createLookAndFeel() {
        //FlatIntelliJLaf.setup();
        FlatDarculaLaf.setup();
    }

}