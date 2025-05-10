package de.energiequant.limamf.compat.protocol;

import java.util.List;

/**
 * Generic message type transporting information requested from a device.
 *
 * @see GetInfoMessage
 */
public abstract class InfoMessage extends CommandMessage {
    protected InfoMessage(CommandMessage msg) {
        super(msg);
    }

    public static InfoMessage decode(CommandMessage msg) {
        // Info messages can transport either device identification or configurations.
        // A vague guess we rely on is that device configurations usually start with a digit on the first field,
        // while it would be unusual for a MobiFlight interface type name to start non-alphabetic.

        List<String> fields = msg.getFields();
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("at least one field is required");
        }

        String firstField = fields.get(0);
        if (!firstField.isEmpty() && !Character.isDigit(firstField.charAt(0))) {
            return new IdentificationInfoMessage(msg);
        } else {
            return new ConfigurationInfoMessage(msg);
        }
    }
}
