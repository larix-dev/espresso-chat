package com.larix;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.util.Arrays;

import static com.larix.proto.KeyHelper.*;

public class TestSendToServer {

    @Test
    void sendToServer() {
        try {
            final Socket socket = new Socket("localhost", 4123);

            final KeyPair keyPair = genKeyPair(2048);

            // send public
            socket.getOutputStream().write(keyPair.getPublic().getEncoded());

            // read public
            final byte[] buf = new byte[1024];
            final int size = socket.getInputStream().read(buf);

            final PublicKey serverPubKey = decodePublicKey(buf);
            final byte[] secret = genSharedSecret(keyPair.getPrivate(), serverPubKey);

            System.out.println(Arrays.toString(secret));

            socket.getOutputStream().write(new byte[]{1, 2, 3, 4, 5});

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
