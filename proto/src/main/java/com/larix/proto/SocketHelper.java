package com.larix.proto;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Arrays.fill;

public class SocketHelper {

    private static final int END_OF_STREAM = -1;

    public static boolean readToBuffer(final byte[] buf, final InputStream in) {
        try {
            fill(buf, (byte) 0);
            return in.read(buf) != END_OF_STREAM;
        } catch (IOException e) {
            if(e.getMessage().equals("Socket closed")) {
                return false;
            }
            throw new RuntimeException(e);
        }
    }

}
