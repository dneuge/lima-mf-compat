package de.energiequant.limamf.compat.config.devices;

import static de.energiequant.limamf.compat.utils.Numbers.requireUint8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hardware description of a generic output channel.
 */
public class OutputConfiguration extends DeviceConfiguration {
    private static final Pattern FIRMWARE_PATTERN = Pattern.compile("^(\\d+)\\.(.*)$");

    private final int pin;

    private OutputConfiguration(int pin, String name) {
        super(DeviceType.OUTPUT, name);

        this.pin = pin;
    }

    public int getPin() {
        return pin;
    }

    public static OutputConfiguration parseFirmwareFormat(String s) {
        Matcher matcher = FIRMWARE_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid syntax: \"" + s + "\"");
        }

        return new OutputConfiguration(
            requireUint8(matcher.group(1)),
            matcher.group(2)
        );
    }

    @Override
    protected void describeTo(StringBuilder sb) {
        sb.append(", pin=");
        sb.append(pin);
    }
}
