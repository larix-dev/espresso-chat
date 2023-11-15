package com.larix;

import javax.swing.*;
import java.awt.*;

public class EspressoClient extends JFrame {

    public EspressoClient() {
        super("Espresso Chat");
        setSize(new Dimension(600, 400));
        setMinimumSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void start() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
