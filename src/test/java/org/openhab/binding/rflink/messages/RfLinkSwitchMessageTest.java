package org.openhab.binding.rflink.messages;

import java.util.Collection;
import java.util.Map;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;

public class RfLinkSwitchMessageTest {

    public static String INPUT_SWITCH_KAKU_MESSAGE = "20;06;Kaku;ID=41;SWITCH=1;CMD=ON;";
    public static String INPUT_SWITCH_KAKU_MESSAGE2 = "20;46;Kaku;ID=44;SWITCH=4;CMD=OFF;";
    public static String INPUT_SWITCH_NEWKAKU_MESSAGE = "20;3B;NewKaku;ID=cac142;SWITCH=3;CMD=OFF;";
    // newKaku specific command with Dimming info (cf. rutgerputter #39)
    public static String INPUT_SWITCH_NEWKAKU_DIM_MESSAGE = "20;04;NewKaku;ID=000007;SWITCH=2;CMD=SET_LEVEL=14;";
    public static String INPUT_SWITCH_CONRAD_MESSAGE = "20;41;Conrad RSL2;ID=00010002;SWITCH=03;CMD=ON;";
    public static String OUTPUT_SWITCH_KAKU_MESSAGE = "10;Kaku;0000004d;1;OFF;";
    public static String OUTPUT_SWITCH_KAKU_DIM_MESSAGE = "10;Kaku;0000004d;1;14;";
    public static String OUTPUT_SWITCH_HOMECONFORT_MESSAGE = "10;HomeConfort;0001b523;D3;ON;";

    @Test
    public void testEncodeSwitchKakuMessage() {
        RfLinkSwitchMessage message = new RfLinkSwitchMessage(INPUT_SWITCH_KAKU_MESSAGE);
        Assert.assertEquals("deviceName error", "Kaku", message.getDeviceName());
        Assert.assertEquals("deviceId error", "Kaku-41-1", message.getDeviceId());
        Assert.assertEquals("deviceId error", OpenClosedType.OPEN, message.contact);
        Assert.assertEquals("deviceId error", OnOffType.ON, message.command);
    }

    @Test
    public void testEncodeSwitchKakuMessage2() {
        RfLinkSwitchMessage message = new RfLinkSwitchMessage(INPUT_SWITCH_KAKU_MESSAGE2);
        Assert.assertEquals("deviceName error", "Kaku", message.getDeviceName());
        Assert.assertEquals("deviceId error", "Kaku-44-4", message.getDeviceId());
        Assert.assertEquals("deviceId error", OpenClosedType.CLOSED, message.contact);
        Assert.assertEquals("deviceId error", OnOffType.OFF, message.command);
    }

    @Test
    public void testEncodeSwitchNewKakuMessage() {
        RfLinkSwitchMessage message = new RfLinkSwitchMessage(INPUT_SWITCH_NEWKAKU_MESSAGE);
        Assert.assertEquals("deviceName error", "NewKaku", message.getDeviceName());
        Assert.assertEquals("deviceId error", "NewKaku-cac142-3", message.getDeviceId());
        Assert.assertEquals("deviceId error", OnOffType.OFF, message.command);
        Assert.assertEquals("deviceId error", OpenClosedType.CLOSED, message.contact);
    }

    @Test
    public void testEncodeSwitchNewKakuDimMessage() {
        RfLinkSwitchMessage message = new RfLinkSwitchMessage(INPUT_SWITCH_NEWKAKU_DIM_MESSAGE);
        Assert.assertEquals("deviceName error", "NewKaku", message.getDeviceName());
        Assert.assertEquals("deviceId error", "NewKaku-000007-2", message.getDeviceId());
        Map<String, State> states = message.getStates();
        Assert.assertTrue("should contain a 'command' state",
                states.containsKey(RfLinkBindingConstants.CHANNEL_COMMAND));
        Assert.assertEquals("unexpected 'command' state", UnDefType.UNDEF,
                states.get(RfLinkBindingConstants.CHANNEL_COMMAND));
        Assert.assertTrue("should contain a 'contact' state",
                states.containsKey(RfLinkBindingConstants.CHANNEL_CONTACT));
        Assert.assertEquals("unexpected 'contact' state", UnDefType.UNDEF,
                states.get(RfLinkBindingConstants.CHANNEL_CONTACT));
        Assert.assertTrue("should contain a 'dimmingLevel' state",
                states.containsKey(RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL));
        Assert.assertEquals("unexpected 'dimmingLevel' state", new DecimalType(14),
                states.get(RfLinkBindingConstants.CHANNEL_DIMMING_LEVEL));
    }

    @Test
    public void testEncodeSwitchConradMessage() {
        RfLinkSwitchMessage message = new RfLinkSwitchMessage(INPUT_SWITCH_CONRAD_MESSAGE);
        Assert.assertEquals("deviceName error", "ConradRSL2", message.getDeviceName());
        Assert.assertEquals("deviceId error", "ConradRSL2-00010002-03", message.getDeviceId());
        Assert.assertEquals("deviceId error", OpenClosedType.OPEN.toString(), message.contact.toString());
        Assert.assertEquals("deviceId error", OnOffType.ON, message.command);
    }

    @Test
    public void testDecodeSwitchKakuMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.OFF;
        RfLinkSwitchMessage message = new RfLinkSwitchMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "Kaku", message.getDeviceName());
        Assert.assertEquals("command error", OnOffType.OFF, message.command);

        Collection<String> decodedMessages = message.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeSwitchKakuDimMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("Kaku-00004d-1", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = DecimalType.valueOf("14");
        RfLinkSwitchMessage message = new RfLinkSwitchMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "Kaku-00004d-1", message.getDeviceId());
        Assert.assertEquals("deviceName error", "Kaku", message.getDeviceName());
        Assert.assertEquals("command error", DecimalType.valueOf("14"), message.command);

        Collection<String> decodedMessages = message.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_KAKU_DIM_MESSAGE, decodedMessages.iterator().next());
    }

    @Test
    public void testDecodeHomeConfortMessage() throws RfLinkNotImpException, RfLinkException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("HomeConfort-01b523-D3", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = OnOffType.ON;
        RfLinkSwitchMessage message = new RfLinkSwitchMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertEquals("deviceId error", "HomeConfort-01b523-D3", message.getDeviceId());
        Assert.assertEquals("deviceName error", "HomeConfort", message.getDeviceName());
        Assert.assertEquals("command error", OnOffType.ON, message.command);

        Collection<String> decodedMessages = message.buildMessages();
        Assert.assertNotNull(decodedMessages);
        Assert.assertEquals(1, decodedMessages.size());
        Assert.assertEquals("message error", OUTPUT_SWITCH_HOMECONFORT_MESSAGE, decodedMessages.iterator().next());
    }
}
