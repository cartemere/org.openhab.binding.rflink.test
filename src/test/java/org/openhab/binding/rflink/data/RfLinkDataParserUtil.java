package org.openhab.binding.rflink.data;

import org.junit.Assert;
import org.junit.Test;
import org.openhab.binding.rflink.messages.RfLinkDataParser;

public class RfLinkDataParserUtil {

    @Test
    public void parseInt() {
        int value = RfLinkDataParser.parseToInt("99");
        Assert.assertEquals(99, value);
    }

    @Test
    public void parseHexaToUnsignedDecimal() {
        double value = RfLinkDataParser.parseHexaToUnsignedDecimal("8d");
        Assert.assertEquals(14.1d, value, 0.001d);
    }

    @Test
    public void parseHexaToSignedDecimal_Negative() {
        double value = RfLinkDataParser.parseHexaToSignedDecimal("80DC");
        Assert.assertEquals(-22.0d, value, 0.001d);
    }

    @Test
    public void parseHexaToSignedDecimal_Positive() {
        double value = RfLinkDataParser.parseHexaToSignedDecimal("C0");
        Assert.assertEquals(19.2d, value, 0.001d);
    }

    @Test
    public void parseIntTo360Direction() {
        double value = RfLinkDataParser.parseIntTo360Direction("00");
        Assert.assertEquals(0.0d, value, 0.001d);
        value = RfLinkDataParser.parseIntTo360Direction("5");
        Assert.assertEquals(112.5d, value, 0.001d);
        value = RfLinkDataParser.parseIntTo360Direction("10");
        Assert.assertEquals(225.0d, value, 0.001d);
    }

}
