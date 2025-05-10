package de.energiequant.limamf.compat.config.devices;

/**
 * Base class for all hardware components (devices in MobiFlight terms) described in an {@link InterfaceConfiguration}.
 */
public abstract class DeviceConfiguration {
    private final DeviceType type;
    private final String name;

    DeviceConfiguration(DeviceType type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Returns the hardware component type.
     *
     * @return hardware component (device) type
     */
    public DeviceType getType() {
        return type;
    }

    /**
     * Returns the name used to refer to this hardware component.
     *
     * @return name used for this component
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());

        sb.append("(");
        sb.append(type);
        sb.append(", \"");
        sb.append(name);
        sb.append("\"");

        describeTo(sb);

        sb.append(")");

        return sb.toString();
    }

    /**
     * Can be implemented to add more information for {@link #toString()} output. Should start with a comma as delimiter
     * from previous output.
     *
     * @param sb {@link StringBuilder} to append more information to
     */
    protected void describeTo(StringBuilder sb) {
        // extension point
    }
}
