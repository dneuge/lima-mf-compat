package de.energiequant.limamf.compat.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Identifies the command described by a {@link CommandMessage}.
 */
public enum CommandType {
    INIT_MODULE(0),
    SET_MODULE(1),
    SET_PIN(2),
    SET_STEPPER(3),
    SET_SERVO(4),
    STATUS(5),
    ENCODER_CHANGE(6),
    BUTTON_CHANGE(7),
    STEPPER_CHANGE(8),
    GET_INFO(9),
    INFO(10),
    SET_CONFIG(11),
    GET_CONFIG(12),
    RESET_CONFIG(13),
    SAVE_CONFIG(14),
    CONFIG_SAVED(15),
    ACTIVATE_CONFIG(16),
    CONFIG_ACTIVATED(17),
    SET_POWER_SAVING_MODE(18),
    SET_NAME(19),
    GEN_NEW_SERIAL(20),
    RESET_STEPPER(21),
    SET_ZERO_STEPPER(22),
    TRIGGER(23),
    RESET_BOARD(24),
    SET_LCD_DISPLAY_I2C(25),
    SET_MODULE_BRIGHTNESS(26),
    SET_SHIFT_REGISTER_PINS(27),
    ANALOG_CHANGE(28),
    INPUT_SHIFTER_CHANGE(29),
    DIG_IN_MUX_CHANGE(30),
    SET_STEPPER_SPEED_ACCEL(31),
    SET_CUSTOM_DEVICE(32),
    DEBUG(255);

    private final int firmwareEncoding;

    private static final Map<Integer, CommandType> BY_FIRMWARE_ENCODING = new HashMap<>();

    static {
        for (CommandType type : values()) {
            CommandType previous = BY_FIRMWARE_ENCODING.put(type.firmwareEncoding, type);
            if (previous != null) {
                throw new IllegalArgumentException(
                    "duplicate encoding " + type.firmwareEncoding
                        + " for " + previous
                        + " and " + type
                );
            }
        }
    }

    CommandType(int firmwareEncoding) {
        if (firmwareEncoding < 0 || firmwareEncoding > 255) {
            throw new IllegalArgumentException("firmware encoding is supposed to be an unsigned byte, got " + firmwareEncoding);
        }

        this.firmwareEncoding = firmwareEncoding;
    }

    /**
     * Returns the corresponding type ID for communication with MobiFlight firmware.
     *
     * @return type ID used by MobiFlight firmware
     */
    public int getFirmwareEncoding() {
        return firmwareEncoding;
    }

    /**
     * Resolves the given type ID, as used by MobiFlight firmware, to a {@link CommandType} enum.
     *
     * @param encoding type ID used by MobiFlight firmware
     * @return corresponding {@link CommandType} enum, if known
     */
    public static Optional<CommandType> fromFirmwareEncoding(int encoding) {
        return Optional.ofNullable(BY_FIRMWARE_ENCODING.get(encoding));
    }
}
