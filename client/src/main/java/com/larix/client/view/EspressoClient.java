package com.larix.client.view;

import com.larix.client.model.Model;

import javax.swing.*;
import java.awt.*;

import static com.formdev.flatlaf.util.SystemInfo.isMacFullWindowContentSupported;
import static java.lang.String.format;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class EspressoClient extends JFrame {

    private final Model model;

    public EspressoClient(final String username, final String host, final int port) {
        super("Espresso Chat");
        setSize(new Dimension(800, 533));
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (isMacFullWindowContentSupported) {
            this.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        }

        this.setLayout(new BorderLayout());

        final OutputPanel outputPanel = new OutputPanel();
        final JScrollPane outputWrapper = new JScrollPane(outputPanel);
        outputWrapper.setBorder(null);
        outputWrapper.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        this.add(outputWrapper, BorderLayout.CENTER);

        model = new Model(outputPanel::writeMessage, username, host, port);

        final InputPanel inputPanel = new InputPanel(getContentPane(), model::sendMessage, format("%s:%d", host, port), username);
        this.add(inputPanel, BorderLayout.SOUTH);
    }

    public void start() {
        setLocationRelativeTo(null);
        setVisible(true);
        model.startSocket();
    }

}
