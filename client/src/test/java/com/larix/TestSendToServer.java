package com.larix;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyAgreement;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class TestSendToServer {

    @Test
    void sendToServer() {
        try {
            final Socket socket = new Socket("localhost", 4123);

            final KeyPairGenerator gen = KeyPairGenerator.getInstance("DH");
            gen.initialize(2048);
            final KeyPair keyPair = gen.generateKeyPair();
            final byte[] pubKey = keyPair.getPublic().getEncoded();
            System.out.println(pubKey.length);
            System.out.println(Arrays.toString(pubKey));

            final OutputStream out = socket.getOutputStream();
            out.write(pubKey);
            out.flush();

            final byte[] buf = new byte[1024];
            final int size = socket.getInputStream().read(buf);

            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(buf);
            PublicKey serverPubKey = keyFactory.generatePublic(spec);

            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(keyPair.getPrivate());
            keyAgreement.doPhase(serverPubKey, true);

            byte[] secret = keyAgreement.generateSecret();
            System.out.println(Arrays.toString(secret));

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
