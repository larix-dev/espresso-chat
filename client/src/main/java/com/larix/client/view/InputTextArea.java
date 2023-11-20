package com.larix.client.view;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_ENTER;

public class InputTextArea extends JTextArea {

    public InputTextArea(final Container contentPane, final Consumer<String> messageSender) {
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setFont(new Font(getFont().getName(), getFont().getStyle(), 14));
        setRows(1);
        setLineWrap(true);
        setWrapStyleWord(true);

        addKeyListener(new InputKeyListener(this, messageSender));
        getDocument().addDocumentListener(new InputDocumentListener(contentPane, this));
        contentPane.addComponentListener(new InputComponentAdapter(this, contentPane));
    }

    @RequiredArgsConstructor
    private static class InputComponentAdapter extends ComponentAdapter {

        private final JTextArea input;
        private final Container contentPane;

        @Override
        public void componentResized(ComponentEvent e) {
            if (updateLineCount(input)) {
                contentPane.revalidate();
            }
        }
    }

    @RequiredArgsConstructor
    private static class InputKeyListener extends KeyAdapter {

        private final JTextArea input;
        private final Consumer<String> messageSender;

        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() == VK_ENTER) {
                if ((e.getModifiersEx() & SHIFT_DOWN_MASK) != 0) {
                    input.append("\n");
                } else {
                    e.consume();
                    messageSender.accept(input.getText());
                    input.setText(null);
                }
            }
        }
    }

    @RequiredArgsConstructor
    private static class InputDocumentListener implements DocumentListener {

        private final Container contentPane;
        private final JTextArea input;

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAndRevalidate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAndRevalidate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateAndRevalidate();
        }

        private void updateAndRevalidate() {
            if (updateLineCount(input)) {
                contentPane.revalidate();
            }
        }

    }

    private static boolean updateLineCount(final JTextArea input) {
        final FontMetrics metrics = input.getGraphics().getFontMetrics(input.getFont());

        final double textWidth = metrics.stringWidth(input.getText());
        final double compWidth = input.getVisibleRect().getWidth() - input.getInsets().left - input.getInsets().right;
        final int lines = (int) Math.ceil(textWidth / compWidth) + input.getLineCount() - 1;

        if (lines <= 5) {
            input.setRows(lines);
            return true;
        }
        return false;
    }

}
