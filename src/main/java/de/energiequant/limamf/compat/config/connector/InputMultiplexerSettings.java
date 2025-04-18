package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.CollectionUtils.exactlyOne;
import static de.energiequant.limamf.compat.utils.DOMUtils.findChildElementsNamed;
import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

import de.energiequant.limamf.compat.utils.Numbers;

public class InputMultiplexerSettings extends Settings {
    private final String serial;
    private final String multiplexerName;
    private final int dataPin;

    public InputMultiplexerSettings(Node settingsNode) {
        super(settingsNode);

        this.serial = getAttribute(settingsNode, "serial")
            .orElseThrow(() -> new IllegalArgumentException("encoder must be linked to a serial"));

        this.multiplexerName = getAttribute(settingsNode, "name")
            .orElseThrow(() -> new IllegalArgumentException("missing name for input multiplexer"));

        Node muxNode = exactlyOne(findChildElementsNamed(settingsNode, "inputMultiplexer"));
        this.dataPin = getAttribute(muxNode, "DataPin")
            .map(Numbers::requireUint8)
            .orElseThrow(() -> new IllegalArgumentException("missing mux data pin declaration"));
    }

    public String getSerial() {
        return serial;
    }

    public String getMultiplexerName() {
        return multiplexerName;
    }

    public int getDataPin() {
        return dataPin;
    }
}
