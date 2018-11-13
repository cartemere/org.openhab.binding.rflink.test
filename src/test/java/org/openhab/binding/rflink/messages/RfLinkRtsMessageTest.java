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
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;

public class RfLinkRtsMessageTest {

    public static String INPUT_RTS_SWITCH_MESSAGE = "20;39;RTS;ID=1a602a;SWITCH=01;CMD=DOWN;";
    public static String OUTPUT_RTS_DOWN_MESSAGE = "10;RTS;00OFOFF1;1;DOWN;";
    public static String OUTPUT_RTS_UP_MESSAGE = "10;RTS;00OFOFF1;1;UP;";
    public static String OUTPUT_RTS_STOP_MESSAGE = "10;RTS;00OFOFF1;1;STOP;";
    public static String OUTPUT_RTS_ON_MESSAGE = "10;RTS;00OFOFF1;1;UP;";
    public static String OUTPUT_RTS_OFF_MESSAGE = "10;RTS;00OFOFF1;1;DOWN;";

    @Test
    public void testEncodeMessage() {
        RfLinkRtsMessage message = new RfLinkRtsMessage(INPUT_RTS_SWITCH_MESSAGE);
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("deviceId error", "RTS-1a602a", message.getDeviceId());
    }

    @Test
    public void testDecodeShutterMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-OFOFF1-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = UpDownType.DOWN;
        RfLinkRtsMessage message = new RfLinkRtsMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-OFOFF1-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("command error", UpDownType.DOWN, message.command);
        Collection<String> decodedMessages = message.decodeMessagesAsString("");
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_DOWN_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeCommandMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-OFOFF1-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_COMMAND);
        Command command = OnOffType.ON;
        RfLinkRtsMessage message = new RfLinkRtsMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-OFOFF1-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("command error", UpDownType.UP, message.command);
        Collection<String> decodedMessages = message.decodeMessagesAsString("");
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_ON_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeShutterReverseMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-OFOFF1-1", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = UpDownType.DOWN;
        RfLinkRtsMessage message = new RfLinkRtsMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-OFOFF1-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("command error", UpDownType.UP, message.command);
        Collection<String> decodedMessages = message.decodeMessagesAsString("");
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_UP_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeCommandReverseMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-OFOFF1-1", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.ON;
        RfLinkRtsMessage message = new RfLinkRtsMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-OFOFF1-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("command error", UpDownType.DOWN, message.command);

        Collection<String> decodedMessages = message.decodeMessagesAsString("");
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_OFF_MESSAGE, decodedMessages.iterator().next());

    }

    @Test
    public void testDecodeShutterStopMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("RTS-OFOFF1-1", true);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = StopMoveType.STOP;
        RfLinkRtsMessage message = new RfLinkRtsMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "RTS-OFOFF1-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "RTS", message.getDeviceName());
        Assert.assertEquals("command error", StopMoveType.STOP, message.command);

        Collection<String> decodedMessages = message.decodeMessagesAsString("");
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_RTS_STOP_MESSAGE, decodedMessages.iterator().next());
    }
}
