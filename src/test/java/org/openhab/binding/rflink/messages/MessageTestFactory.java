package org.openhab.binding.rflink.messages;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.packet.RfLinkPacket;
import org.openhab.binding.rflink.packet.RfLinkPacketType;

public class MessageTestFactory {

    public static RfLinkDeviceConfiguration getDeviceConfiguration(String deviceId, boolean isReversed) {
        RfLinkDeviceConfiguration config = new RfLinkDeviceConfiguration();
        config.deviceId = deviceId;
        config.isCommandReversed = isReversed;
        return config;
    }

    public static ChannelUID getChannel(String channelType) {
        ChannelUID channelId = new ChannelUID("bla:bli:blo:" + channelType);
        return channelId;
    }

    public static RfLinkPacket inputPacket(String packetContent) {
        return new RfLinkPacket(RfLinkPacketType.INPUT, packetContent);
    }

    public static RfLinkPacket outputPacket(String packetContent) {
        return new RfLinkPacket(RfLinkPacketType.OUTPUT, packetContent);
    }
}
