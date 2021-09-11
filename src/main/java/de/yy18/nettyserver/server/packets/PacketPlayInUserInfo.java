package de.yy18.nettyserver.server.packets;

import de.yy18.nettyserver.server.user.User;
import de.yy18.nettyserver.server.user.UserManager;
import lombok.NonNull;

import java.io.IOException;
import java.net.InetSocketAddress;

public class PacketPlayInUserInfo extends PacketPlayIn{

    private final InetSocketAddress iNetSocketAddress;

    public PacketPlayInUserInfo(final byte[] bytes, @NonNull final InetSocketAddress iNetSocketAddress) {
        super(bytes);
        this.iNetSocketAddress = iNetSocketAddress;
    }

    @Override
    public void decodePacket() {
        try {
            final short packetNumber = super.readShort();
            final int length = super.readInt();
            final String name = super.readString(length);
            final User current = UserManager.getINSTANCE().getUserByInetSocketAddress(iNetSocketAddress);
            current.setUserName(name);
            UserManager.getINSTANCE().updateUser(iNetSocketAddress, current);
            if(current.getUserName().equalsIgnoreCase("unknown")
                    ||current.getUserName().contains(" ")) {
                PacketPlayOutHandler.sendPacket(new PacketPlayOutUserInfoResponse(false), current);
                UserManager.getINSTANCE().closeConnection(current);
            } else {
                PacketPlayOutHandler.sendPacket(new PacketPlayOutUserInfoResponse(true), current);
            }
        } catch (IOException ignored) {
        }
    }

}