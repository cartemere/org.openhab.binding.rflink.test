package org.openhab.binding.rflink.messages;

import java.util.Collection;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.device.RfLinkDevice;
import org.openhab.binding.rflink.device.RfLinkDeviceFactory;
import org.openhab.binding.rflink.device.RfLinkSwitchDevice;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;
import org.openhab.binding.rflink.message.RfLinkMessage;

public class RfLinkSwitchMessageTest {

    public static String INPUT_SWITCH_KAKU_MESSAGE = "20;06;Kaku;ID=41;SWITCH=1;CMD=ON;";
    public static String INPUT_SWITCH_KAKU_MESSAGE2 = "20;46;Kaku;ID=44;SWITCH=4;CMD=OFF;";
    public static String INPUT_SWITCH_NEWKAKU_MESSAGE = "20;3B;NewKaku;ID=cac142;SWITCH=3;CMD=OFF;";
    // newKaku specific command with Dimming info (cf. rutgerputter #39)
    public static String INPUT_SWITCH_NEWKAKU_DIM_MESSAGE = "20;04;NewKaku;ID=000007;SWITCH=2;CMD=SET_LEVEL=14;";
    public static String INPUT_SWITCH_NEWKAKU_ALLON_MESSAGE = "20;3B;NewKaku;ID=cac142;SWITCH=3;CMD=ALLON;";
    public static String INPUT_SWITCH_CONRAD_MESSAGE = "20;41;Conrad RSL2;ID=00010002;SWITCH=03;CMD=ON;";
    public static String OUTPUT_SWITCH_KAKU_MESSAGE = "10;Kaku;0000004d;1;OFF;";
    public static String OUTPUT_SWITCH_KAKU_DIM14_MESSAGE = "10;Kaku;0000004d;1;14;";
    public static String OUTPUT_SWITCH_KAKU_DIM80PERCENT_MESSAGE = "10;Kaku;0000004d;1;12;";
    public static String OUTPUT_SWITCH_KAKU_DIM18_MESSAGE = "10;Kaku;0000004d;1;15;";
    public static String OUTPUT_SWITCH_KAKU_DIM0_MESSAGE = "10;Kaku;0000004d;1;OFF;";

    public static String OUTPUT_SWITCH_HOMECONFORT_MESSAGE = "10;HomeConfort;0001b523;D3;ON;";

    @Test
    public void testEncodeSwitchKakuMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_KAKU_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());
        Assert.assertEquals("deviceId error", "Kaku-41-1", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
    }

    @Test
    public void testEncodeSwitchKakuMessage2() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_KAKU_MESSAGE2);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());
        Assert.assertEquals("deviceId error", "Kaku-44-4", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.CLOSED);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.OFF);
    }

    @Test
    public void testEncodeSwitchNewKakuMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_NEWKAKU_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "NewKaku", device.getProtocol());
        Assert.assertEquals("deviceId error", "NewKaku-cac142-3", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.CLOSED);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.OFF);
    }

    @Test
    public void testEncodeSwitchNewKakuDimMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_NEWKAKU_DIM_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "NewKaku", device.getProtocol());
        Assert.assertEquals("deviceId error", "NewKaku-000007-2", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL, new DecimalType(14));
    }

    @Test
    public void testEncodeSwitchNewKakuAllOnMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_NEWKAKU_ALLON_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "NewKaku", device.getProtocol());
        Assert.assertEquals("deviceId error", "NewKaku-cac142-3", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
    }

    @Test
    public void testEncodeSwitchConradMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_CONRAD_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "ConradRSL2", device.getProtocol());
        Assert.assertEquals("deviceId error", "ConradRSL2-00010002-03", device.getKey());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
    }

    @Test
    public void testDecodeSwitchKakuMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.OFF;
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", device.getKey());
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.OFF);

        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeSwitchKakuDim14Message() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = new DecimalType(14);
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", device.getKey());
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL, (State) command);

        Assert.assertEquals("command error", "14", device.getCommandSuffix());
        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_DIM14_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeSwitchKakuDim80PercentMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = new PercentType(80);
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", device.getKey());
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL, new DecimalType(12));

        Assert.assertEquals("command error", "12", device.getCommandSuffix());
        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_DIM80PERCENT_MESSAGE,
                decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeSwitchKakuDim18Message() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = new DecimalType(18);
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", device.getKey());
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.OPEN);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL, new DecimalType(15));

        Assert.assertEquals("command error", "15", device.getCommandSuffix());
        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_DIM18_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeSwitchKakuDim0Message() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = new DecimalType(0);
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", device.getKey());
        Assert.assertEquals("deviceName error", "Kaku", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_CONTACT, OpenClosedType.CLOSED);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.OFF);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL, new DecimalType(0));

        Assert.assertEquals("command error", "OFF", device.getCommandSuffix());
        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_DIM0_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeHomeConfortMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("HomeConfort-01b523-D3", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.ON;
        RfLinkSwitchDevice device = new RfLinkSwitchDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "HomeConfort-01b523-D3", device.getKey());
        Assert.assertEquals("deviceName error", "HomeConfort", device.getProtocol());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_COMMAND, OnOffType.ON);

        Collection<String> decodedMessages = device.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_HOMECONFORT_MESSAGE, decodedMessages.iterator().next());
    }
}
