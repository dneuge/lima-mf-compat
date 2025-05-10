package de.energiequant.limamf.compat.config.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A hardware component (device in MobiFlight terms) to interface with.
 */
public enum DeviceType {
    NOT_SET(0),
    BUTTON(1),
    ENCODER_SINGLE_DETENT(2),
    OUTPUT(3),
    LED_SEGMENT_DEPRECATED(4, DeviceType.DEPRECATED),
    STEPPER_DEPRECATED_1(5, DeviceType.DEPRECATED),
    SERVO(6),
    LCD_DISPLAY_I2C(7),
    ENCODER(8),
    STEPPER_DEPRECATED_2(9, DeviceType.DEPRECATED),
    OUTPUT_SHIFTER(10),
    ANALOG_INPUT_DEPRECATED(11, DeviceType.DEPRECATED),
    INPUT_SHIFTER(12),
    MUX_DRIVER(13),
    DIGITAL_INPUT_MULTIPLEXER(14),
    STEPPER(15),
    LED_SEGMENT_MULTI(16),
    CUSTOM_DEVICE(17),
    ANALOG_INPUT(18);

    private static final boolean DEPRECATED = true;

    private final int firmwareEncoding;
    private final boolean deprecated;

    private static final Map<Integer, DeviceType> BY_FIRMWARE_ENCODING = new HashMap<>();

    static {
        for (DeviceType deviceType : values()) {
            DeviceType previous = BY_FIRMWARE_ENCODING.put(deviceType.firmwareEncoding, deviceType);
            if (previous != null) {
                throw new IllegalArgumentException(
                    "duplicate encoding " + deviceType.firmwareEncoding
                        + " for " + previous
                        + " and " + deviceType
                );
            }
        }
    }

    DeviceType(int firmwareEncoding) {
        this(firmwareEncoding, false);
    }

    DeviceType(int firmwareEncoding, boolean deprecated) {
        if (firmwareEncoding < 0 || firmwareEncoding > 255) {
            throw new IllegalArgumentException("firmware encoding is supposed to be an unsigned byte, got " + firmwareEncoding);
        }

        this.firmwareEncoding = firmwareEncoding;
        this.deprecated = deprecated;
    }

    /**
     * Returns the corresponding device type ID for communication with MobiFlight firmware.
     *
     * @return device type ID used by MobiFlight firmware
     */
    public int getFirmwareEncoding() {
        return firmwareEncoding;
    }

    /**
     * Indicates whether the device type has been marked as deprecated in MobiFlight firmware source code.
     *
     * @return {@code true} if marked as deprecated in MobiFlight firmware, {@code false} if not
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Resolves the given device type ID, as used by MobiFlight firmware, to a {@link DeviceType} enum.
     *
     * @param encoding device type ID used by MobiFlight firmware
     * @return corresponding {@link DeviceType} enum, if known
     */
    public static Optional<DeviceType> fromFirmwareEncoding(int encoding) {
        return Optional.ofNullable(BY_FIRMWARE_ENCODING.get(encoding));
    }
}
