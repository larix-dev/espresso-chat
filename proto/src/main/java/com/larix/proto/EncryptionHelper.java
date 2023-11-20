package com.larix.proto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class EncryptionHelper {

    public static byte[] encrypt(final byte[] data, final SecretKey key) {
        return doCipher(data, key, ENCRYPT_MODE);
    }

    public static byte[] decrypt(final byte[] data, final SecretKey key) {
        return doCipher(data, key, DECRYPT_MODE);
    }

    private static byte[] doCipher(final byte[] data, final SecretKey key, final int mode) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(mode, key);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

}
