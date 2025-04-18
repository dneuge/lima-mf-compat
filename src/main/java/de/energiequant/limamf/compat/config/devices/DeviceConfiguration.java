package de.energiequant.limamf.compat.config.devices;

public abstract class DeviceConfiguration {
    private final DeviceType type;
    private final String name;

    DeviceConfiguration(DeviceType type, String name) {
        this.type = type;
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

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

    protected void describeTo(StringBuilder sb) {
        // extension point
    }
}
