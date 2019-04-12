package org.openhab.binding.rflink.messages;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.device.RfLinkWindDevice;
import org.openhab.binding.rflink.message.RfLinkMessage;
import org.openhab.binding.rflink.packet.RfLinkPacket;

public class RfLinkWindMessageTest {

    public static RfLinkPacket INPUT_SWITCH_CRESTA_MESSAGE = MessageTestFactory
            .inputPacket("20;47;Cresta;ID=8001;WINDIR=0002;WINSP=0060;WINGS=0088;WINCHL=b0;");
    public static RfLinkPacket INPUT_SWITCH_OREGON_MESSAGE = MessageTestFactory
            .inputPacket("20;32;Oregon Wind;ID=1a89;WDIR=0045;WINSP=0068;AWINSP=0050;BAT=OK;");
    public static RfLinkPacket INPUT_SWITCH_OREGON_MESSAGE2 = MessageTestFactory
            .inputPacket("20;4a;Oregon Wind2;ID=3a0d;WDIR=0021;WINSP=0040;AWINSP=005a;BAT=OK;");

    @Test
    public void testEncodeCrestaMessage() {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_CRESTA_MESSAGE);
        RfLinkWindDevice device = new RfLinkWindDevice();
        device.initializeFromMessage(null, message);
        Assert.assertEquals("deviceName error", "Cresta", device.getProtocol());
        Assert.assertEquals("deviceId error", "Cresta-8001", device.getKey());
        // ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_SPEED, new DecimalType(96));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_AVERAGE_WIND_SPEED, new DecimalType(0.0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_DIRECTION, new DecimalType(45.0));
        // int ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_GUST, new DecimalType(136));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_CHILL, new DecimalType(17.6));
    }

    @Test
    public void testEncodeOregonMessage() {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_OREGON_MESSAGE);
        RfLinkWindDevice device = new RfLinkWindDevice();
        device.initializeFromMessage(null, message);
        Assert.assertEquals("deviceName error", "OregonWind", device.getProtocol());
        Assert.assertEquals("deviceId error", "OregonWind-1a89", device.getKey());
        // ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_SPEED, new DecimalType(104));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_AVERAGE_WIND_SPEED, new DecimalType(8.0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_DIRECTION, new DecimalType(69.0));
        // int ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_GUST, new DecimalType(0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_CHILL, new DecimalType(0));
    }

    @Test
    public void testEncodeOregon2Message() {
        RfLinkMessage message = new RfLinkMessage(INPUT_SWITCH_OREGON_MESSAGE2);
        RfLinkWindDevice device = new RfLinkWindDevice();
        device.initializeFromMessage(null, message);
        Assert.assertEquals("deviceName error", "OregonWind2", device.getProtocol());
        Assert.assertEquals("deviceId error", "OregonWind2-3a0d", device.getKey());
        // ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_SPEED, new DecimalType(64));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_AVERAGE_WIND_SPEED, new DecimalType(9.0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_DIRECTION, new DecimalType(33.0));
        // int ?
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_GUST, new DecimalType(0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_WIND_CHILL, new DecimalType(0));
    }
}
