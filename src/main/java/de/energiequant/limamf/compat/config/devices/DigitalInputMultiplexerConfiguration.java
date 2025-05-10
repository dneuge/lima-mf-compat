package de.energiequant.limamf.compat.config.devices;

import static de.energiequant.limamf.compat.utils.Numbers.requireUint8;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hardware description of a digital input multiplexer.
 */
public class DigitalInputMultiplexerConfiguration extends DeviceConfiguration {
    private static final Pattern FIRMWARE_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)\\.(.*)$");

    private final int pinData;
    private final int[] pinsSel;
    private final int numRegisters;

    private DigitalInputMultiplexerConfiguration(int pinData, int[] pinsSel, int numRegisters, String name) {
        super(DeviceType.DIGITAL_INPUT_MULTIPLEXER, name);

        this.pinData = pinData;
        this.pinsSel = pinsSel;
        this.numRegisters = numRegisters;
    }

    /**
     * Parses a {@link DigitalInputMultiplexerConfiguration} from the given protocol encoding used by MobiFlight
     * firmware.
     *
     * @param s protocol encoding used by MobiFlight firmware
     * @return decoded hardware description
     */
    public static DigitalInputMultiplexerConfiguration parseFirmwareFormat(String s) {
        Matcher matcher = FIRMWARE_PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid syntax: \"" + s + "\"");
        }

        return new DigitalInputMultiplexerConfiguration(
            requireUint8(matcher.group(1)),
            new int[]{
                requireUint8(matcher.group(2)),
                requireUint8(matcher.group(3)),
                requireUint8(matcher.group(4)),
                requireUint8(matcher.group(5)),
            },
            requireUint8(matcher.group(6)),
            matcher.group(7)
        );
    }

    @Override
    protected void describeTo(StringBuilder sb) {
        sb.append(", pinData=");
        sb.append(pinData);

        sb.append(", pinsSel=[");
        for (int i = 0; i < pinsSel.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(pinsSel[i]);
        }

        sb.append("], numRegisters=");
        sb.append(numRegisters);
    }
}
