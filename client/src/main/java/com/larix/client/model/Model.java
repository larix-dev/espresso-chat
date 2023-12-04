package com.larix.client.model;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import static java.lang.Runtime.getRuntime;

@Slf4j
public class Model {

    final ClientProtocol protocol;
    final Socket socket;

    public Model(final Consumer<String> appender, final String username, final String host, final int port) {
        try {
            socket = new Socket(host, port);
            protocol = new ClientProtocol(socket, appender, username);
        } catch (IOException e) {
            log.error("Could not connect to server {}:{}", host, port);
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(final String string) {
        protocol.sendMessage(string);
    }

    public void startSocket() {
        new Thread(protocol).start();
        getRuntime().addShutdownHook(new Thread(this::stopSocket));
    }

    private void stopSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
