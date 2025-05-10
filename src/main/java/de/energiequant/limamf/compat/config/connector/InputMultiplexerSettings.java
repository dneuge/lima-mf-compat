package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.CollectionUtils.exactlyOne;
import static de.energiequant.limamf.compat.utils.DOMUtils.findChildElementsNamed;
import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

import de.energiequant.limamf.compat.config.devices.DigitalInputMultiplexerConfiguration;
import de.energiequant.limamf.compat.protocol.DigitalInputMultiplexerChangeMessage;
import de.energiequant.limamf.compat.utils.Numbers;

/**
 * Information needed to link a multiplexed input (as received through {@link DigitalInputMultiplexerChangeMessage}) to
 * a host-side {@link ConfigItem}.
 */
public class InputMultiplexerSettings extends Settings {
    private final String multiplexerName;
    private final int dataPin;

    /**
     * Parses the given settings DOM {@link Node} to {@link InputMultiplexerSettings}.
     *
     * @param settingsNode XML node to parse
     */
    InputMultiplexerSettings(Node settingsNode) {
        super(settingsNode);

        if (!getSerial().isPresent()) {
            throw new IllegalArgumentException("input multiplexer must be linked to a serial");
        }

        this.multiplexerName = getAttribute(settingsNode, "name")
            .orElseThrow(() -> new IllegalArgumentException("missing name for input multiplexer"));

        Node muxNode = exactlyOne(findChildElementsNamed(settingsNode, "inputMultiplexer"));
        this.dataPin = getAttribute(muxNode, "DataPin")
            .map(Numbers::requireUint8)
            .orElseThrow(() -> new IllegalArgumentException("missing mux data pin declaration"));
    }

    /**
     * Returns the multiplexer's device name as referred to by modules in
     * {@link DigitalInputMultiplexerChangeMessage#getName()} and
     * {@link DigitalInputMultiplexerConfiguration#getName()}.
     *
     * @return name of multiplexer input
     */
    public String getMultiplexerName() {
        return multiplexerName;
    }

    /**
     * The multiplexer "channel" as referred to by modules in {@link DigitalInputMultiplexerChangeMessage#getChannel()}.
     * <p>
     * <strong>Do not confuse with the actual component pin</strong> ("pin" attributes of
     * {@link DigitalInputMultiplexerConfiguration}) which describes the first hardware component level. The "data pin"
     * described here actually is the second level accessed through the multiplexer.
     * </p>
     *
     * @return multiplexer channel ID
     */
    public int getDataPin() {
        return dataPin;
    }
}
