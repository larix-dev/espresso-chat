package com.larix.client.view;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

import static java.awt.geom.Rectangle2D.OUT_BOTTOM;
import static java.lang.String.format;
import static javax.swing.Box.createGlue;

public class OutputPanel extends JPanel {

    private final JTextArea output;

    public OutputPanel() {
        super(new GridBagLayout());

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        add(createGlue(), gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        final DefaultCaret caret = (DefaultCaret) output.getCaret();
        caret.setUpdatePolicy(OUT_BOTTOM);

        add(output, gbc);
    }

    public void writeMessage(final String message) {
        output.append(format("\n%s", message));
    }

}
