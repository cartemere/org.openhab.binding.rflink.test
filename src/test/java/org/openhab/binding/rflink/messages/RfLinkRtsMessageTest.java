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

public class RfLinkRtsMessageTest {

    public static String INPUT_RTS_SWITCH_MESSAGE = "20;39;RTS;ID=1a602a;SWITCH=0;CMD=DOWN;";
    public static String OUTPUT_RTS_DOWN_MESSAGE = "10;RTS;000F0FF1;0;DOWN;";
    public static String OUTPUT_RTS_UP_MESSAGE = "10;RTS;000F0FF1;0;UP;";
    public static String OUTPUT_RTS_STOP_MESSAGE = "10;RTS;000F0FF1;0;STOP;";
    public static String OUTPUT_RTS_ON_MESSAGE = "10;RTS;000F0FF1;0;UP;";
    public static String OUTPUT_RTS_OFF_MESSAGE = "10;RTS;000F0FF1;0;DOWN;";

    @Test
    public void testEncodeMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_RTS_SWITCH_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message); // for now, generate Switch device
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "RTS", device.getProtocol());
        Assert.assertEquals("deviceId error", "RTS-1a602a-0", device.getKey());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, null);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_SHUTTER, OnOffType.OFF);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, UpDownType.DOWN);
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

        Collection<String> decodedMessages = device.buildMessages();
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

        Collection<String> decodedMessages = device.buildMessages();
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

        Collection<String> decodedMessages = device.buildMessages();
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

        Collection<String> decodedMessages = device.buildMessages();
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

        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_STOP_MESSAGE, decodedMessages.iterator().next());
    }
}
