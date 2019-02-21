package org.openhab.binding.rflink.messages;

import org.eclipse.smarthome.core.types.State;
import org.junit.Assert;
import org.openhab.binding.rflink.device.RfLinkDevice;

public class ComparisonUtils<T> {

    public static void checkState(RfLinkDevice device, String key, State expectedState) {
        Assert.assertEquals(key + " error", expectedState, device.getState(key));
    }
}
