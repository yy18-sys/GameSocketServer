package de.yy18.nettyserver.server.user;

import de.yy18.nettyserver.server.thread.InputStreamListener;
import de.yy18.nettyserver.server.util.DateParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.util.UUID;

public final class User {

    @Getter
    private final UUID uuid = UUID.randomUUID();
    @Getter
    @Setter
    private String userName = "Unknown_User";
    private final long timeConnected;
    private final Socket socket;
    private final InetSocketAddress inetSocketAddress;
    private final InputStreamListener inputStreamListener;

    public User(@NonNull final Socket socket) throws IOException {
        this.socket = socket;
        this.timeConnected = System.currentTimeMillis();
        this.inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        inputStreamListener = (InputStreamListener) new InputStreamListener(socket, this.uuid).start();
        UserManager.getINSTANCE().add(this);
    }

    public String toConsole() throws ParseException {

        return "{[uuid]: "+uuid+", [name]: "+userName+", [ipv6address]: "+inetSocketAddress.getAddress()+", [port]: "
                +inetSocketAddress.getPort()+", [connected]: "+ DateParser.parseDateTime(timeConnected) +"}";
    }

    public Socket getSocket() {
        return socket;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    public InputStreamListener getInputStreamListener() {
        return inputStreamListener;
    }

}
