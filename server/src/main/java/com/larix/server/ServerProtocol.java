package com.larix.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static com.larix.proto.KeyHelper.*;
import static java.util.Arrays.fill;

@Slf4j
@RequiredArgsConstructor
public class ServerProtocol implements Runnable {

    private static final int END_OF_STREAM = -1;

    private final Socket socket;
    private final List<ServerProtocol> clients;
    private byte[] secret;

    @Override
    public void run() {
        try (final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
            final byte[] buf = new byte[1024];
            while (readToBuffer(buf, in)) {
                log.info("Received from client {}", Arrays.toString(buf));

                if (clients.contains(this)) {
                    // send to clients
                } else {
                    doKeyExchange(decodePublicKey(buf));
                    clients.add(this);
                }
            }
            clients.remove(this);
            log.info("Client disconnected {}", socket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean readToBuffer(final byte[] buf, final InputStream in) {
        try {
            fill(buf, (byte) 0);
            return in.read(buf) != END_OF_STREAM;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doKeyExchange(final PublicKey publicKey) {
        final KeyPair keyPair = genKeyPair(2048);
        secret = genSharedSecret(keyPair.getPrivate(), publicKey);
        sendPublicKey(keyPair.getPublic(), socket);
    }

    public void start() {
        new Thread(this).start();
    }
}
