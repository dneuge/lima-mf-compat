package de.energiequant.limamf.compat.config.devices;

import static de.energiequant.limamf.compat.utils.Numbers.requireUint8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hardware description of a rotary encoder.
 */
public class EncoderConfiguration extends DeviceConfiguration {
    private static final Pattern FIRMWARE_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(.*)$");

    private final int pin1;
    private final int pin2;
    private final int type;

    EncoderConfiguration(int pin1, int pin2, int type, String name) {
        super(DeviceType.ENCODER, name);

        this.pin1 = pin1;
        this.pin2 = pin2;
        this.type = type;
    }

    public static EncoderConfiguration parseFirmwareFormat(String s) {
        Matcher matcher = FIRMWARE_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid syntax: \"" + s + "\"");
        }

        return new EncoderConfiguration(
            requireUint8(matcher.group(1)),
            requireUint8(matcher.group(2)),
            requireUint8(matcher.group(3)),
            matcher.group(4)
        );
    }

    @Override
    protected void describeTo(StringBuilder sb) {
        sb.append(", pin1=");
        sb.append(pin1);

        sb.append(", pin2=");
        sb.append(pin2);

        sb.append(", type=");
        sb.append(type);
    }
}
