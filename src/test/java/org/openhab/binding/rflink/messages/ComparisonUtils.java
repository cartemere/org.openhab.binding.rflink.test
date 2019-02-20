package org.openhab.binding.rflink.messages;

import org.eclipse.smarthome.core.types.State;

public class ComparisonUtils {

    public static final double COMPARISON_DELTA = 0.001;

    public static State getFromStates(RfLinkMessage message, String key) {
        return message.getStates().get(key);
    }
}
