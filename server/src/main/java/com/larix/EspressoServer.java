package com.larix;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyAgreement;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class EspressoServer {

    private final List<Socket> clients = new ArrayList<>();
    private static final int PORT = 4123;

    public EspressoServer() {

    }

    public void start() {
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server started on port {}", PORT);

            while (!Thread.interrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                new Thread(() -> {
                    try {
                        final DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));

                        while (true) {
                            final byte[] buffer = new byte[1024];
                            final boolean closed = in.read(buffer) == -1;
                            System.out.println("Received from client: " + Arrays.toString(buffer));
                            //broadcastToClients("Client " + clientSocket + " says: " + inputLine);
                            if (closed) {
                                break;
                            }

                            if (!clients.contains(clientSocket)) {
                                KeyFactory keyFactory = KeyFactory.getInstance("DH");
                                X509EncodedKeySpec spec = new X509EncodedKeySpec(buffer);
                                PublicKey clientPubKey = keyFactory.generatePublic(spec);

                                KeyPairGenerator gen = KeyPairGenerator.getInstance("DH");
                                gen.initialize(2048);
                                KeyPair keyPair = gen.generateKeyPair();

                                KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
                                keyAgreement.init(keyPair.getPrivate());
                                keyAgreement.doPhase(clientPubKey, true);

                                byte[] secret = keyAgreement.generateSecret();
                                System.out.println(Arrays.toString(secret));

                                byte[] pubKey = keyPair.getPublic().getEncoded();

                                OutputStream out = clientSocket.getOutputStream();
                                out.write(pubKey);

                                clients.add(clientSocket);
                            }
                        }
                        clients.remove(clientSocket);
                        System.out.println("Client disconnected: " + clientSocket);

                    } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void keyExchange(Socket clientSocket, KeyPair serverKeyPair) {

    }

    private void broadcastToClients(String message) {
        for (Socket client : clients) {
            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
