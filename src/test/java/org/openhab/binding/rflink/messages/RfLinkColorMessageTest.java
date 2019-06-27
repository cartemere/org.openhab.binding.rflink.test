package org.openhab.binding.rflink.messages;

import java.util.Collection;
import java.util.Map;

import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.device.RfLinkColorDevice;
import org.openhab.binding.rflink.device.RfLinkDevice;
import org.openhab.binding.rflink.device.RfLinkDeviceFactory;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;
import org.openhab.binding.rflink.message.RfLinkMessage;
import org.openhab.binding.rflink.packet.RfLinkPacket;

public class RfLinkColorMessageTest {

    public static RfLinkPacket INPUT_MILIGHT_SWITCH_MESSAGE = MessageTestFactory
            .inputPacket("20;01;MiLightv1;ID=F746;SWITCH=00;RGBW=3c00;CMD=ON;");
    public static RfLinkPacket INPUT_MILIGHT_SWITCH_MESSAGE2 = MessageTestFactory
            .inputPacket("20;17;MiLightv1;ID=F746;SWITCH=02;RGBW=8690;CMD=ON;");
    public static RfLinkPacket OUTPUT_MILIGHT_COLOR_MESSAGE = MessageTestFactory
            .outputPacket("10;MiLightv1;00F746;00;858E;COLOR;");
    public static RfLinkPacket OUTPUT_MILIGHT_BRIGHT_MESSAGE = MessageTestFactory
            .outputPacket("10;MiLightv1;00F746;00;858E;BRIGHT;");

    @Test
    public void testEncodeMilightRGBWMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_MILIGHT_SWITCH_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(null, message);

        Assert.assertEquals("deviceName error", "MiLightv1", device.getProtocol());
        Assert.assertEquals("deviceId error", "MiLightv1-F746-00", device.getKey());
        Map<String, State> states = device.getStates();
        State color = states.get(RfLinkBindingConstants.CHANNEL_COLOR);
        HSBType expectedHSB = new HSBType("39,100,0");
        Assert.assertNotNull("should have state : " + RfLinkBindingConstants.CHANNEL_COLOR, color);
        Assert.assertEquals("should have state type : " + expectedHSB.getClass(), expectedHSB.getClass(),
                color.getClass());
        Assert.assertEquals("unexpected HSB value", expectedHSB, color);
    }

    @Test
    public void testEncodeMilightRGBWMessage2() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_MILIGHT_SWITCH_MESSAGE2);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(null, message);

        Assert.assertEquals("deviceName error", "MiLightv1", device.getProtocol());
        Assert.assertEquals("deviceId error", "MiLightv1-F746-02", device.getKey());
        Map<String, State> states = device.getStates();
        State color = states.get(RfLinkBindingConstants.CHANNEL_COLOR);
        HSBType expectedHSB = new HSBType("144,100,56");
        Assert.assertNotNull("should have state : " + RfLinkBindingConstants.CHANNEL_COLOR, color);
        Assert.assertEquals("should have state type : " + expectedHSB.getClass(), expectedHSB.getClass(),
                color.getClass());
        Assert.assertEquals("unexpected HSB value", expectedHSB, color);
    }

    @Test
    public void testDecodeMilightRGBWMessage() throws RfLinkException, RfLinkNotImpException {
        RfLinkDeviceConfiguration config = MessageTestFactory.getDeviceConfiguration("MiLightv1-F746-00", false);
        ChannelUID channelId = MessageTestFactory.getChannel(RfLinkBindingConstants.CHANNEL_SHUTTER);
        Command command = new HSBType("144,100,56");
        RfLinkColorDevice device = new RfLinkColorDevice();
        device.initializeFromChannel(config, channelId, command);
        Assert.assertNotNull(device);
        // can't go further for now... only binary outputs
        Collection<RfLinkPacket> packets = device.buildOutputPackets();
        Assert.assertEquals("expect 2 messages", 2, packets.size());
        Assert.assertTrue("Should contain COLOR action : " + OUTPUT_MILIGHT_COLOR_MESSAGE,
                packets.contains(OUTPUT_MILIGHT_COLOR_MESSAGE));
        Assert.assertTrue("Should contain BRIGHT action : " + OUTPUT_MILIGHT_BRIGHT_MESSAGE,
                packets.contains(OUTPUT_MILIGHT_BRIGHT_MESSAGE));
    }

}
