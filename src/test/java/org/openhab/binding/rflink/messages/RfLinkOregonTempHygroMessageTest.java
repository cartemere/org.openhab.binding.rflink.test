package org.openhab.binding.rflink.messages;

import org.junit.Assert;
import org.junit.Test;

public class RfLinkOregonTempHygroMessageTest {

    public static String INPUT_ORE_TEMPHYGRO_MESSAGE = "20;74;Oregon TempHygro;ID=0ACC;TEMP=00be;HUM=40;BAT=OK;";
    public static String INPUT_ORE_TEMPHYGRO_MESSAGE2 = "20;b3;Oregon TempHygro;ID=1a2d;TEMP=00dd;HUM=43;BAT=LOW;";

    @Test
    public void testDecodeMessage1() {
        RfLinkOregonTempHygroMessage message = new RfLinkOregonTempHygroMessage(INPUT_ORE_TEMPHYGRO_MESSAGE);
        Assert.assertEquals("deviceName error", "OregonTempHygro", message.getProtocol());
        Assert.assertEquals("deviceId error", "OregonTempHygro-0ACC", message.getDeviceIdKey());
        Assert.assertEquals("battery status error", RfLinkOregonTempHygroMessage.Commands.OFF, message.battery_status);
        Assert.assertEquals("humidity value error", 40, message.humidity);
        Assert.assertEquals("humidity status error", "UNKNOWN", message.humidity_status);
        Assert.assertEquals("temperature error", 19.0, message.temperature, 0.0001);
    }

    @Test
    public void testDecodeMessage2() {
        RfLinkOregonTempHygroMessage message = new RfLinkOregonTempHygroMessage(INPUT_ORE_TEMPHYGRO_MESSAGE2);
        Assert.assertEquals("deviceName error", "OregonTempHygro", message.getProtocol());
        Assert.assertEquals("deviceId error", "OregonTempHygro-1a2d", message.getDeviceIdKey());
        Assert.assertEquals("battery status error", RfLinkOregonTempHygroMessage.Commands.ON, message.battery_status);
        Assert.assertEquals("humidity value error", 43, message.humidity);
        Assert.assertEquals("humidity status error", "UNKNOWN", message.humidity_status);
        Assert.assertEquals("temperature error", 22.1, message.temperature, 0.0001);
    }
}
