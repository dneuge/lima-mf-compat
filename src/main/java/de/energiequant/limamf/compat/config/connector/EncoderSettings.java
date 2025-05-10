package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

import de.energiequant.limamf.compat.config.devices.EncoderConfiguration;
import de.energiequant.limamf.compat.protocol.EncoderChangeMessage;

/**
 * Information needed to link a rotary encoder input (as received through {@link EncoderChangeMessage}) to
 * a host-side {@link ConfigItem}.
 */
public class EncoderSettings extends Settings {
    private final String encoderName;

    /**
     * Parses the given settings DOM {@link Node} to {@link InputMultiplexerSettings}.
     *
     * @param settingsNode XML node to parse
     */
    EncoderSettings(Node settingsNode) {
        super(settingsNode);

        if (!getSerial().isPresent()) {
            throw new IllegalArgumentException("encoder must be linked to a serial");
        }

        this.encoderName = getAttribute(settingsNode, "name")
            .orElseThrow(() -> new IllegalArgumentException("missing name for encoder"));
    }

    /**
     * Returns the rotary encoder's device name as referred to by modules in
     * {@link EncoderChangeMessage#getName()} and {@link EncoderConfiguration#getName()}.
     *
     * @return name of rotary encoder input
     */
    public String getEncoderName() {
        return encoderName;
    }
}
