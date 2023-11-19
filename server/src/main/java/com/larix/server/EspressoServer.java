package com.larix.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EspressoServer {

    private final List<ServerProtocol> clients = new ArrayList<>();
    private final int port;

    public EspressoServer(final int port) {
        this.port = port;
    }

    public void start() {
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server started on port {}", port);

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
