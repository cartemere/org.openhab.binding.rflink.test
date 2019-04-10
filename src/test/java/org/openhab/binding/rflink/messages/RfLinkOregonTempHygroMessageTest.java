package org.openhab.binding.rflink.messages;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.RfLinkBindingConstants;
import org.openhab.binding.rflink.device.RfLinkDevice;
import org.openhab.binding.rflink.device.RfLinkDeviceFactory;
import org.openhab.binding.rflink.exceptions.RfLinkException;
import org.openhab.binding.rflink.exceptions.RfLinkNotImpException;
import org.openhab.binding.rflink.message.RfLinkMessage;
import org.openhab.binding.rflink.packet.RfLinkPacket;

public class RfLinkOregonTempHygroMessageTest {

    public static RfLinkPacket INPUT_ORE_TEMPHYGRO_MESSAGE = MessageTestFactory
            .inputPacket("20;74;Oregon TempHygro;ID=0ACC;TEMP=00be;HUM=40;BAT=OK;");
    public static RfLinkPacket INPUT_ORE_TEMPHYGRO_MESSAGE2 = MessageTestFactory
            .inputPacket("20;b3;Oregon TempHygro;ID=1a2d;TEMP=00dd;HUM=43;BAT=LOW;");

    @Test
    public void testProcessPacket1() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_ORE_TEMPHYGRO_MESSAGE);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "OregonTempHygro", device.getProtocol());
        Assert.assertEquals("deviceId error", "OregonTempHygro-0ACC", device.getKey());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_LOW_BATTERY, OnOffType.OFF);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_TEMPERATURE, new DecimalType(19.0));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_HUMIDITY, new DecimalType(40));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_HUMIDITY_STATUS, new StringType("UNKNOWN"));
    }

    @Test
    public void testProcessPacket2() throws RfLinkException, RfLinkNotImpException {
        RfLinkMessage message = new RfLinkMessage(INPUT_ORE_TEMPHYGRO_MESSAGE2);
        RfLinkDevice device = RfLinkDeviceFactory.createDeviceFromMessage(message);
        device.initializeFromMessage(message);
        Assert.assertEquals("deviceName error", "OregonTempHygro", device.getProtocol());
        Assert.assertEquals("deviceId error", "OregonTempHygro-1a2d", device.getKey());

        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_LOW_BATTERY, OnOffType.ON);
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_TEMPERATURE, new DecimalType(22.1));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_HUMIDITY, new DecimalType(43));
        ComparisonUtils.checkState(device, RfLinkBindingConstants.CHANNEL_HUMIDITY_STATUS, new StringType("UNKNOWN"));
    }
}
