package org.openhab.binding.rflink.messages;

import java.util.Collection;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StopMoveType;
import org.eclipse.smarthome.core.library.types.UpDownType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.device.RfLinkDevice;
import org.openhab.binding.rflink.device.RfLinkDeviceFactory;
import org.openhab.binding.rflink.device.RfLinkRtsDevice;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;
import org.openhab.binding.rflink.message.RfLinkMessage;
import org.openhab.binding.rflink.packet.RfLinkPacket;

public class RfLinkRtsMessageTest {

    public static RfLinkPacket INPUT_RTS_SWITCH_MESSAGE = MessageTestFactory
            .inputPacket("20;39;RTS;ID=1a602a;SWITCH=1;CMD=DOWN;");
    public static RfLinkPacket OUTPUT_RTS_DOWN_MESSAGE = MessageTestFactory.outputPacket("10;RTS;0F0FF1;0;DOWN;");
    public static RfLinkPacket OUTPUT_RTS_UP_MESSAGE = MessageTestFactory.outputPacket("10;RTS;0F0FF1;0;UP;");
    public static RfLinkPacket OUTPUT_RTS_STOP_MESSAGE = MessageTestFactory.outputPacket("10;RTS;0F0FF1;0;STOP;");
    public static RfLinkPacket OUTPUT_RTS_ON_MESSAGE = MessageTestFactory.outputPacket("10;RTS;0F0FF1;0;UP;");
    public static RfLinkPacket OUTPUT_RTS_OFF_MESSAGE = MessageTestFactory.outputPacket("10;RTS;0F0FF1;0;DOWN;");

    @Test
    public void testEncodeMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_RTS_SWITCH_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(null, message);
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());
        Assert.assertEquals("deviceId error", "RTS-1a602a-1", device.getKey());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, null);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_SHUTTER, OnOffType.OFF);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.DOWN);
    }

    @Test
    public void testEchoInputMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_RTS_SWITCH_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        RfLinkDeviceConfiguration config = new RfLinkDeviceConfiguration();
        config.echoPattern = "ID=12345;SWITCH=0";
        device.initializeFromMessage(config, message);
        Collection<RfLinkPacket> echoPackets = device.buildEchoPackets();
        Assert.assertNotNull(echoPackets);
        Assert.assertFalse(echoPackets.isEmpty());
        Assert.assertEquals(1, echoPackets.size());
        RfLinkPacket echoPacket = echoPackets.iterator().next();

        RfLinkMessage echoMessage = new RfLinkMessage(echoPacket);
        RfLinkDevice echoDevice = RfLinkDeviceFactory.createDeviceFromMessage(echoMessage);
        echoDevice.initializeFromMessage(null, echoMessage);
        Assert.assertEquals("deviceName error", "RTS", echoDevice.getProtocol());
        Assert.assertEquals("deviceId error", "RTS-12345-0", echoDevice.getKey());

        ComparisonUtils.checkState(echoDevice, RfLinkBindingConstants.CHANNEL_CONTACT, null);
        ComparisonUtils.checkState(echoDevice, RfLinkBindingConstants.CHANNEL_SHUTTER, OnOffType.OFF);
        ComparisonUtils.checkState(echoDevice, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.DOWN);
    }

    @Test
    public void testDecodeShutterMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-0F0FF1-0", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = UpDownType.DOWN;
        RfLinkRtsDevice device = new RfLinkRtsDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-0F0FF1-0", device.getKey());
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.DOWN);

        Collection<RfLinkPacket> decodedMessages = device.buildOutputPackets();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_DOWN_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeCommandMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-0F0FF1-0", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_COMMAND);
        Command command = OnOffType.ON;
        RfLinkRtsDevice device = new RfLinkRtsDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-0F0FF1-0", device.getKey());
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.UP);

        Collection<RfLinkPacket> decodedMessages = device.buildOutputPackets();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_ON_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeShutterReverseMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-0F0FF1-0", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = UpDownType.DOWN;
        RfLinkRtsDevice device = new RfLinkRtsDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-0F0FF1-0", device.getKey());
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.UP);

        Collection<RfLinkPacket> decodedMessages = device.buildOutputPackets();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_UP_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeCommandReverseMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-0F0FF1-0", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.ON;
        RfLinkRtsDevice device = new RfLinkRtsDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-0F0FF1-0", device.getKey());
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.DOWN);

        Collection<RfLinkPacket> decodedMessages = device.buildOutputPackets();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_OFF_MESSAGE, decodedMessages.iterator().next());

    }

    @Test
    public void testDecodeShutterStopMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-0F0FF1-0", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = StopMoveType.STOP;
        RfLinkRtsDevice device = new RfLinkRtsDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-0F0FF1-0", device.getKey());
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());
        Assert.assertEquals("command error", StopMoveType.STOP, device.command);

        Collection<RfLinkPacket> decodedMessages = device.buildOutputPackets();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_STOP_MESSAGE, decodedMessages.iterator().next());
    }
}
