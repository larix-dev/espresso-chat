package com.larix;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EspressoServer {

    private final List<ServerProtocol> clients = new ArrayList<>();
    private static final int PORT = 4123;

    public EspressoServer() {

    }

    public void start() {
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server started on port {}", PORT);

            while (!Thread.interrupted()) {
                final Socket clientSocket = serverSocket.accept();
                log.info("Client connected {}", clientSocket);
                new ServerProtocol(clientSocket, clients).start();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
