package com.larix.proto;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class KeyHelper {

    public static KeyPair genKeyPair() {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance("DH");
            gen.initialize(2048);
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

    public static SecretKey genSharedSecret(final PrivateKey privateKey, final PublicKey publicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(publicKey, true);
            final MessageDigest hash = MessageDigest.getInstance("SHA-256");
            final byte[] secretBytes = hash.digest(keyAgreement.generateSecret());
            return new SecretKeySpec(secretBytes, "AES");
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
