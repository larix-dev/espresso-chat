package com.larix.client.model;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import static java.lang.String.format;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

@Slf4j
public class Model {

    final ClientProtocol protocol;

    public Model(final Consumer<String> appender, final String username, final String host, final int port) {
        try {
            final Socket socket = new Socket(host, port);
            protocol = new ClientProtocol(socket, appender, username);
        } catch (IOException e) {
            log.error("Could not connect to server {}:{}", host, port);
            //showMessageDialog(null, format("Could not connect to server %s:%s", host, port), "Error", ERROR_MESSAGE);
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(final String string) {
        protocol.sendMessage(string);
    }

    public void startSocket() {
        new Thread(protocol).start();
    }

}
