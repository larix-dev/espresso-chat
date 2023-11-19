package com.larix.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;

import static com.larix.proto.KeyHelper.*;
import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
public class TestEspressoServer {

    @Test
    void testKeyExchange() {
        new Thread(() -> new EspressoServer(4123).start()).start();
        try {
            // wait for server to start
            Thread.sleep(3000);
            // bind to server
            final Socket socket = new Socket("localhost", 4123);

            final KeyPair keyPair = genKeyPair(2048);
            sendPublicKey(keyPair.getPublic(), socket);

            // read public
            final byte[] buf = new byte[1024];
            final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            if(in.read(buf) == -1) {
                fail();
            }

            final PublicKey serverPubKey = decodePublicKey(buf);
            final byte[] secret = genSharedSecret(keyPair.getPrivate(), serverPubKey);

            log.info("Generated secret {}", Arrays.toString(secret));

            socket.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
