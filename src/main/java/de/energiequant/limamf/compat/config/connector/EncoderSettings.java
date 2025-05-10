package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

public class EncoderSettings extends Settings {
    private final String encoderName;

    EncoderSettings(Node settingsNode) {
        super(settingsNode);

        if (!getSerial().isPresent()) {
            throw new IllegalArgumentException("encoder must be linked to a serial");
        }

        this.encoderName = getAttribute(settingsNode, "name")
            .orElseThrow(() -> new IllegalArgumentException("missing name for encoder"));
    }

    public String getEncoderName() {
        return encoderName;
    }
}
