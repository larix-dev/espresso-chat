package com.larix.server;

import com.larix.proto.EncryptionHelper;
import com.larix.proto.MessagePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.indunet.fastproto.FastProto;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static com.larix.proto.EncryptionHelper.encrypt;
import static com.larix.proto.KeyHelper.*;
import static com.larix.proto.SocketHelper.readToBuffer;

@Slf4j
@RequiredArgsConstructor
public class ServerProtocol implements Runnable {

    private static final int END_OF_STREAM = -1;

    private final Socket socket;
    private final List<ServerProtocol> clients;
    private SecretKey sharedSecret;

    @Override
    public void run() {
        try (final DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {
            final byte[] buf = new byte[1024];
            while (readToBuffer(buf, in)) {
                log.info("Received from client {}", Arrays.toString(buf));

                if (clients.contains(this)) {
                    final byte[] decrypted = EncryptionHelper.decrypt(buf, sharedSecret);
                    log.info("Decrypted message {}", decrypted);
                    final MessagePacket message = FastProto.decode(decrypted, MessagePacket.class);
                    log.info("Message {}", message);
                    broadcastMessage(message);
                } else {
                    sharedSecret = doKeyExchange(decodePublicKey(buf));
                    clients.add(this);
                }
            }
            clients.remove(this);
            log.info("Client disconnected {}", socket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(final MessagePacket message) {
        try {
            final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            final byte[] encrypted = encrypt(FastProto.encode(message, 1024), sharedSecret);
            out.write(encrypted);
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void broadcastMessage(final MessagePacket message) {
        clients.forEach(client -> client.sendMessage(message));
    }

    private SecretKey doKeyExchange(final PublicKey publicKey) {
        final KeyPair keyPair = genKeyPair();
        sendPublicKey(keyPair.getPublic(), socket);
        return genSharedSecret(keyPair.getPrivate(), publicKey);
    }

    public void start() {
        new Thread(this).start();
    }
}
