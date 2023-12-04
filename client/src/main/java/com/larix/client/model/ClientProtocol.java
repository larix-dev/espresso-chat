package com.larix.client.model;

import com.larix.proto.EncryptionHelper;
import com.larix.proto.MessagePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.indunet.fastproto.FastProto;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.function.Consumer;

import static com.larix.proto.EncryptionHelper.encrypt;
import static com.larix.proto.KeyHelper.*;
import static com.larix.proto.SocketHelper.readToBuffer;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class ClientProtocol implements Runnable {

    private final Socket socket;
    private final Consumer<String> appender;
    private final String username;
    private SecretKey sharedSecret;

    @Override
    public void run() {
        final KeyPair keyPair = genKeyPair();
        sendPublicKey(keyPair.getPublic(), socket);

        try (final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
            final byte[] buf = new byte[1024];
            while (readToBuffer(buf, in)) {
                log.info("Received from server {}", Arrays.toString(buf));

                if (sharedSecret != null) {
                    final byte[] decrypted = EncryptionHelper.decrypt(buf, sharedSecret);
                    log.info("Decrypted message {}", decrypted);
                    final MessagePacket message = FastProto.decode(decrypted, MessagePacket.class);
                    log.info("Message {}", message);
                    appender.accept(format("<%s> %s", message.getUsername(), message.getMessage()));
                } else {
                    sharedSecret = genSharedSecret(keyPair.getPrivate(), decodePublicKey(buf));
                }
            }
            log.info("Disconnected from server {}", socket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(final String messageString) {
        final MessagePacket message = new MessagePacket(username, messageString);

        try {
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            final byte[] encrypted = encrypt(FastProto.encode(message, 1024), sharedSecret);
            out.write(encrypted);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
