package com.larix;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import org.indunet.fastproto.FastProto;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

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