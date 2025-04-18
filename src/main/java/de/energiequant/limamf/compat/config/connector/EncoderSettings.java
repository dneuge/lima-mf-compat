package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

public class EncoderSettings extends Settings {
    private final String serial;
    private final String encoderName;

    public EncoderSettings(Node settingsNode) {
        super(settingsNode);

        this.serial = getAttribute(settingsNode, "serial")
            .orElseThrow(() -> new IllegalArgumentException("encoder must be linked to a serial"));

        this.encoderName = getAttribute(settingsNode, "name")
            .orElseThrow(() -> new IllegalArgumentException("missing name for encoder"));
    }

    public String getSerial() {
        return serial;
    }

    public String getEncoderName() {
        return encoderName;
    }
}
