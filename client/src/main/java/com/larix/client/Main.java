package com.larix.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme;
import com.formdev.flatlaf.util.SystemInfo;
import com.larix.client.view.EspressoClient;

import javax.swing.*;

import static java.lang.Integer.parseInt;
import static java.lang.System.exit;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class Main {

    public static void main(final String[] args) {
        if (SystemInfo.isMacOS) {
            System.setProperty("apple.awt.application.name", "Espresso Chat");
            System.setProperty("apple.awt.application.appearance", "system");
        }

        SwingUtilities.invokeLater(Main::startApp);
    }

    private static void startApp() {
        FlatMaterialOceanicIJTheme.setup();

        final JTextField username = new JTextField(25);
        final JTextField host = new JTextField(25);
        final JTextField port = new JTextField(25);

        final Object[] fields = {"Username", username, "Host", host, "Port", port};
        //final JFrame dialogRoot = new JFrame();
        //dialogRoot.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);

        final int option = showConfirmDialog(null, fields, "Espresso Chat", OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            new EspressoClient(username.getText(), host.getText(), parseInt(port.getText())).start();
        } else {
            exit(0);
        }
    }

}