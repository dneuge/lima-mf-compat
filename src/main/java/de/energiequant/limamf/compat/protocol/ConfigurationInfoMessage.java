package de.energiequant.limamf.compat.protocol;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.energiequant.limamf.compat.config.devices.InterfaceConfiguration;

/**
 * Configuration returned from a device upon {@link GetConfigMessage}.
 *
 * @see GetConfigMessage
 */
public class ConfigurationInfoMessage extends InfoMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationInfoMessage.class);

    private final String rawConfiguration;
    private final InterfaceConfiguration configuration;

    /**
     * Parses the given {@link CommandMessage} to a {@link ConfigurationInfoMessage}.
     *
     * @param msg message to parse
     */
    public ConfigurationInfoMessage(CommandMessage msg) {
        super(msg);

        List<String> fields = msg.getFields();
        if (fields.size() != 1) {
            throw new IllegalArgumentException("Unexpected number of fields, got " + fields.size());
        }

        rawConfiguration = fields.get(0);

        InterfaceConfiguration parsed = null;
        try {
            parsed = InterfaceConfiguration.parseFirmwareFormat(rawConfiguration);
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("failed to parse interface configuration", ex);
        }
        configuration = parsed;
    }

    /**
     * Returns the raw encoded device-side configuration.
     * <p>
     * This is mainly useful for debugging; you probably want to use {@link #getConfiguration()} instead.
     * </p>
     *
     * @return raw configuration received from module
     */
    public String getRawConfiguration() {
        return rawConfiguration;
    }

    /**
     * Returns the {@link InterfaceConfiguration} active on the module at time of reception.
     *
     * @return {@link InterfaceConfiguration} active on module
     */
    public InterfaceConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        if (configuration != null) {
            return "ConfigurationInfoMessage(" + configuration + ")";
        } else {
            return "ConfigurationInfoMessage(unparseable, raw=\"" + rawConfiguration + "\")";
        }
    }
}
