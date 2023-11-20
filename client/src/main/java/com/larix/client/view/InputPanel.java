package com.larix.client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

import static java.lang.String.format;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class InputPanel extends JPanel {

    public InputPanel(final Container contentPane, final Consumer<String> messageSender, final String server, final String username) {
        setBorder(new EmptyBorder(8, 8, 8, 8));

        final JSeparator separator = new JSeparator();
        final JLabel infoText = new JLabel(format("Connected to %s as %s", server, username));

        final JTextArea input = new InputTextArea(contentPane, messageSender);
        final JScrollPane inputWrapper = new JScrollPane(input);
        inputWrapper.setBorder(null);
        inputWrapper.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        final GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(separator).addComponent(infoText).addComponent(inputWrapper));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(separator).addComponent(infoText).addComponent(inputWrapper));
        setLayout(layout);
    }

}
