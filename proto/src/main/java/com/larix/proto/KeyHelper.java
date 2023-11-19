package com.larix.proto;

import javax.crypto.KeyAgreement;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyHelper {

    public static KeyPair genKeyPair(final int size) {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance("DH");
            gen.initialize(size);
            return gen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static PublicKey decodePublicKey(final byte[] key) {
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance("DH");
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] genSharedSecret(final PrivateKey privateKey, final PublicKey publicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            return keyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void sendPublicKey(final PublicKey publicKey, final Socket socket) {
        try {
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            out.write(publicKey.getEncoded());
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
