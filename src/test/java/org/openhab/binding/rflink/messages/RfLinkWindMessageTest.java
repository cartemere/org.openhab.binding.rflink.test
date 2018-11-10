package org.openhab.binding.rflink.messages;

import org.junit.Assert;
import org.junit.Test;

public class RfLinkWindMessageTest {

    public static String INPUT_SWITCH_CRESTA_MESSAGE = "20;47;Cresta;ID=8001;WINDIR=0002;WINSP=0060;WINGS=0088;WINCHL=b0;";
    public static String INPUT_SWITCH_OREGON_MESSAGE = "20;32;Oregon Wind;ID=1a89;WDIR=0045;WINSP=0068;AWINSP=0050;BAT=OK;";
    public static String INPUT_SWITCH_OREGON_MESSAGE2 = "20;4a;Oregon Wind2;ID=3a0d;WDIR=0021;WINSP=0040;AWINSP=005a;BAT=OK;";

    @Test
    public void testEncodeCrestaMessage() {
        RfLinkWindMessage message = new RfLinkWindMessage(INPUT_SWITCH_CRESTA_MESSAGE);
        Assert.assertEquals("deviceName error", "Cresta", message.getDeviceName());
        Assert.assertEquals("deviceId error", "Cresta-8001", message.getDeviceId());
        // ?
        Assert.assertEquals("windSpeed error", 96, message.windSpeed);
        Assert.assertEquals("averageWindSpeed error", 0.0, message.averageWindSpeed, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windDirection error", 45.0, message.windDirection, ComparisonUtils.COMPARISON_DELTA);
        // int ?
        Assert.assertEquals("windGust error", 136, message.windGust, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windChill error", -4.8, message.windChill, ComparisonUtils.COMPARISON_DELTA);
    }

    @Test
    public void testEncodeOregonMessage() {
        RfLinkWindMessage message = new RfLinkWindMessage(INPUT_SWITCH_OREGON_MESSAGE);
        Assert.assertEquals("deviceName error", "OregonWind", message.getDeviceName());
        Assert.assertEquals("deviceId error", "OregonWind-1a89", message.getDeviceId());
        // ?
        Assert.assertEquals("windSpeed error", 104, message.windSpeed);
        Assert.assertEquals("averageWindSpeed error", 8.0, message.averageWindSpeed, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windDirection error", 69.0, message.windDirection, ComparisonUtils.COMPARISON_DELTA);
        // int ?
        Assert.assertEquals("windGust error", 0, message.windGust, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windChill error", 0, message.windChill, ComparisonUtils.COMPARISON_DELTA);
    }

    @Test
    public void testEncodeOregon2Message() {
        RfLinkWindMessage message = new RfLinkWindMessage(INPUT_SWITCH_OREGON_MESSAGE2);
        Assert.assertEquals("deviceName error", "OregonWind2", message.getDeviceName());
        Assert.assertEquals("deviceId error", "OregonWind2-3a0d", message.getDeviceId());
        // ?
        Assert.assertEquals("windSpeed error", 64, message.windSpeed);
        Assert.assertEquals("averageWindSpeed error", 9.0, message.averageWindSpeed, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windDirection error", 33.0, message.windDirection, ComparisonUtils.COMPARISON_DELTA);
        // int ?
        Assert.assertEquals("windGust error", 0, message.windGust, ComparisonUtils.COMPARISON_DELTA);
        Assert.assertEquals("windChill error", 0, message.windChill, ComparisonUtils.COMPARISON_DELTA);
    }
}
