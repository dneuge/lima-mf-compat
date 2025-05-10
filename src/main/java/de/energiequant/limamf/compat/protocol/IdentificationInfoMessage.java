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

    /**
     * Parses the given {@link CommandMessage} to an {@link IdentificationInfoMessage}.
     *
     * @param msg message to parse
     */
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

    /**
     * Returns the indicated MobiFlight firmware/hardware interface type.
     *
     * @return MobiFlight firmware/hardware interface type
     */
    public String getMobiflightType() {
        return mobiflightType;
    }

    /**
     * Returns the indicated MobiFlight module name.
     *
     * @return MobiFlight module name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the indicated MobiFlight module serial ID.
     *
     * @return MobiFlight module serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Returns the indicated MobiFlight module "version".
     * <p>
     * Note that a second "core version" also gets indicated: {@link #getCoreVersion()}
     * </p>
     *
     * @return MobiFlight module "version"
     * @see #getCoreVersion()
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the indicated MobiFlight module "core version".
     * <p>
     * Note that a second "version" also gets indicated: {@link #getVersion()}
     * </p>
     *
     * @return MobiFlight module "core version"
     * @see #getVersion()
     */
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
