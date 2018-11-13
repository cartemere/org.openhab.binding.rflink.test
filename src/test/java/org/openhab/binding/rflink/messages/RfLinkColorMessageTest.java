package org.openhab.binding.rflink.messages;

import java.util.Map;

import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.config.RfLinkDeviceConfiguration;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;

public class RfLinkColorMessageTest {

    public static String INPUT_MILIGHT_SWITCH_MESSAGE = "20;01;MiLightv1;ID=F746;SWITCH=00;RGBW=3c00;CMD=ON;";
    public static String INPUT_MILIGHT_SWITCH_MESSAGE2 = "20;17;MiLightv1;ID=F746;SWITCH=02;RGBW=8690;CMD=ON;";
    public static String OUTPUT_MILIGHT_ON_MESSAGE = "10;MiLightv1;F746;00;3c00;ON;";
    public static String OUTPUT_MILIGHT_BRIGHT_MESSAGE = "10;MiLightv1;F746;01;34BC;BRIGHT;";

    @Test
    public void testEncodeMilightRGBWMessage() {
        RfLinkColorMessage message = new RfLinkColorMessage(INPUT_MILIGHT_SWITCH_MESSAGE);
        Assert.assertEquals("deviceName error", "MiLightv1", message.getDeviceName());
        Assert.assertEquals("deviceId error", "MiLightv1-F746-00", message.getDeviceId());
        Map<String, State> states = message.getStates();
        State color = states.get(RfLinkBindingConstants.CHANNEL_COLOR);
        HSBType expectedHSB = new HSBType("39,100,0");
        Assert.assertNotNull("should have state : " + RfLinkBindingConstants.CHANNEL_COLOR, color);
        Assert.assertEquals("should have state type : " + expectedHSB.getClass(), expectedHSB.getClass(),
                color.getClass());
        Assert.assertEquals("unexpected HSB value", expectedHSB, color);
    }

    @Test
    public void testEncodeMilightRGBWMessage2() {
        RfLinkColorMessage message = new RfLinkColorMessage(INPUT_MILIGHT_SWITCH_MESSAGE2);
        Assert.assertEquals("deviceName error", "MiLightv1", message.getDeviceName());
        Assert.assertEquals("deviceId error", "MiLightv1-F746-02", message.getDeviceId());
        Map<String, State> states = message.getStates();
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
        RfLinkColorMessage message = new RfLinkColorMessage();
        message.initializeFromChannel(config, channelId, command);
        Assert.assertNotNull(message);
        byte[] output = message.decodeMessage("");
        Assert.assertNotNull(output);
        // can't go further for now... only binary outputs
        StringBuilder string = new StringBuilder();
        for (byte outputByte : output) {
            string.append(outputByte).append('.');
        }
        Assert.assertEquals("Unexpected binary command to be sent",
                "49.48.59.77.105.76.105.103.104.116.118.49.59.48.48.48.48.70.55.52.54.59.48.48.59.56.53.56.69.59.67.79.76.79.82.59.13.59.10.49.48.59.77.105.76.105.103.104.116.118.49.59.48.48.48.48.70.55.52.54.59.48.48.59.56.53.56.69.59.66.82.73.71.72.84.59.59.10.",
                string.toString());
    }

}
