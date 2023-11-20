package com.larix.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

public class EspressoClient extends JFrame {

    final JTextArea output;

    public EspressoClient() {
        super("Espresso Chat");
        setSize(new Dimension(600, 400));
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        // output
        final JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        outputPanel.add(Box.createGlue(), gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;
        output = new JTextArea();
        output.setEditable(false);
        outputPanel.add(output, gbc);

        final JScrollPane outputScroll = new JScrollPane(outputPanel);
        outputScroll.setBorder(null);
        this.add(outputScroll, BorderLayout.CENTER);

        final JPanel inputPanel = new JPanel();
        final GroupLayout layout = new GroupLayout(inputPanel);
        layout.setAutoCreateGaps(true);
        inputPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        final JSeparator separator = new JSeparator();
        final JLabel infoText = new JLabel("Connected to 127.0.0.1:4123 as Tadashi");

        final JTextArea input = new JTextArea();
        input.setBorder(new EmptyBorder(8, 8, 8, 8));
        input.setFont(new Font(input.getFont().getName(), input.getFont().getStyle(), 14));
        input.setRows(1);

        final Container contentPane = this.getContentPane();
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if ((e.getModifiersEx() & SHIFT_DOWN_MASK) != 0) {
                        input.append("\n");
                    } else {
                        e.consume();
                        System.out.println(input.getText());
                        input.setText(null);
                    }
                }
            }
        });

        input.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLineCount();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLineCount();
            }

            private void updateLineCount() {
                int lineCount = input.getLineCount();
                if (lineCount <= 5) {
                    input.setRows(lineCount);
                    contentPane.revalidate();
                }
            }
        });

        final JScrollPane scrollPane = new JScrollPane(input);
        scrollPane.setBorder(null);

        layout.setHorizontalGroup(layout.createParallelGroup().addComponent(separator).addComponent(infoText).addComponent(scrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(separator).addComponent(infoText).addComponent(scrollPane));
        inputPanel.setLayout(layout);

        this.add(inputPanel, BorderLayout.SOUTH);
    }

    public void start() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
