package de.energiequant.limamf.compat.protocol;

import java.util.List;

/**
 * Information returned from a device upon {@link GetInfoMessage} to identify a device.
 *
 * @see GetInfoMessage
 */
public class IdentificationInfoMessage extends InfoMessage {
    private final String mobiflightType;
    private final String name;
    private final String serial;
    private final String version;
    private final String coreVersion;

    public IdentificationInfoMessage(CommandMessage msg) {
        super(msg);

        List<String> fields = msg.getFields();
        if (fields.size() != 5) {
            throw new IllegalArgumentException("Unexpected number of fields, got " + fields.size());
        }

        this.mobiflightType = fields.get(0);
        this.name = fields.get(1);
        this.serial = fields.get(2);
        this.version = fields.get(3);
        this.coreVersion = fields.get(4);
    }

    public String getMobiflightType() {
        return mobiflightType;
    }

    public String getName() {
        return name;
    }

    public String getSerial() {
        return serial;
    }

    public String getVersion() {
        return version;
    }

    public String getCoreVersion() {
        return coreVersion;
    }

    @Override
    public String toString() {
        return "IdentificationInfoMessage(mfType=\"" + mobiflightType
            + "\", name=\"" + name
            + "\", serial=\"" + serial
            + "\", version=\"" + version
            + "\", coreVersion=\"" + coreVersion
            + "\")";
    }
}
