package de.energiequant.limamf.compat.protocol;

import static de.energiequant.limamf.compat.utils.Numbers.requireInt16;
import static de.energiequant.limamf.compat.utils.Numbers.requireUint8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.energiequant.limamf.compat.config.connector.OutputDisplay;
import de.energiequant.limamf.compat.config.devices.DeviceConfiguration;
import de.energiequant.limamf.compat.config.devices.OutputConfiguration;
import de.energiequant.limamf.compat.utils.Numbers;

/**
 * Changes the state of a physical output pin. {@link #STATE_DIGITAL_ON} and {@link #STATE_DIGITAL_OFF} trigger
 * a digital (constant) on/off state while any other value causes PWM to be used. Negative values are technically
 * possible but may yield unwanted results, value range should probably be restricted to 0..255.
 */
public class SetPinMessage extends CommandMessage {
    private static final Set<String> TESTED_FIRMWARE_VERSIONS = new HashSet<>(Arrays.asList(
        "2.5.1"
    ));

    private static final int STATE_DIGITAL_ON = 0xFF;
    private static final int STATE_DIGITAL_OFF = 0x00;

    private final int pin;
    private final int state;

    private SetPinMessage(int pin, int state) {
        super(CommandType.SET_PIN, encodeFields(pin, state));

        this.pin = pin;
        this.state = state;
    }

    private static List<String> encodeFields(int pin, int state) {
        List<String> fields = new ArrayList<>();

        fields.add(Integer.toString(requireInt16(pin)));
        fields.add(Integer.toString(requireInt16(state)));

        return fields;
    }

    @Override
    public boolean isTestedVersion(String version) {
        return TESTED_FIRMWARE_VERSIONS.contains(version);
    }

    @Override
    public boolean isCriticalOperation() {
        // command alters electrical state and timings on arbitrary pins without any safety checks in firmware
        // => confirmed critical
        return true;
    }

    @Override
    public MessageDirection getMessageDirection() {
        return MessageDirection.SENT_ONLY;
    }

    @Override
    public String toString() {
        return "SetPinMessage(pin=" + pin + ", state=" + state + ")";
    }

    /**
     * Checks if the given value is understood as digital (fully on/off, no PWM) in terms of a {@link SetPinMessage}.
     *
     * @param value value to check
     * @return {@code true} if the value represents a digital state, {@code false} if it requires PWM
     */
    public static boolean isDigitalState(int value) {
        return (value == STATE_DIGITAL_ON) || (value == STATE_DIGITAL_OFF);
    }

    /**
     * Creates a new {@link Builder} for {@link SetPinMessage}s.
     *
     * @return {@link Builder} for {@link SetPinMessage}s
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder creating {@link SetPinMessage}s.
     */
    public static class Builder {
        private Integer pin;
        private Integer state;

        /**
         * Sets the ID of the hardware pin to be manipulated.
         * Consider using one of the other methods accepting a {@link DeviceConfiguration} instead to avoid mistakes.
         *
         * @param pin pin ID
         * @return same instance for method-chaining
         */
        public Builder manipulatingPin(int pin) {
            this.pin = requireUint8(pin);
            return this;
        }

        /**
         * Sets the hardware pin to be manipulated according to the provided {@link OutputConfiguration}.
         *
         * @param outputConfiguration configuration specifying pin to manipulate
         * @return same instance for method-chaining
         */
        public Builder manipulating(OutputConfiguration outputConfiguration) {
            return manipulatingPin(outputConfiguration.getPin());
        }

        /**
         * Sets the hardware pin to be manipulated according to the provided {@link DeviceConfiguration}.
         *
         * @param deviceConfiguration configuration specifying pin to manipulate
         * @return same instance for method-chaining
         */
        public Builder manipulating(DeviceConfiguration deviceConfiguration) {
            if (deviceConfiguration instanceof OutputConfiguration) {
                return manipulating((OutputConfiguration) deviceConfiguration);
            }

            throw new IllegalArgumentException("Unsupported type: " + deviceConfiguration);
        }

        /**
         * Sets the output pin value to digitally (fully) enabled.
         *
         * @return same instance for method-chaining
         */
        public Builder enable() {
            this.state = STATE_DIGITAL_ON;
            return this;
        }

        /**
         * Sets the output pin value to digitally (fully) disabled.
         *
         * @return same instance for method-chaining
         */
        public Builder disable() {
            this.state = STATE_DIGITAL_OFF;
            return this;
        }

        /**
         * Sets the output pin value according to the given fraction. Out-of-range values are clamped to permitted
         * minimum/maximum.
         * <p>
         * <strong>Caution:</strong> MobiFlight firmware provides the resulting value to the underlying hardware IO API.
         * PWM generally means that the hardware pin will be rapidly toggled on/off instead of applying a steady
         * fractional voltage. Rapid toggling may damage incompatible electric components.
         * <strong>Apply only to pins whose connected hardware is known to support PWM.</strong>
         * You may want to check host-side if configuration files permit/deny PWM; see {@link OutputDisplay#usePWM()}.
         * </p>
         *
         * @param fraction fraction to set output pin to
         * @return same instance for method-chaining
         */
        public Builder setPwmDutyCycleFraction(double fraction) {
            this.state = Numbers.limit(Math.round(fraction * 255), 0, 255);
            return this;
        }

        /**
         * Sets the output pin to the specified value.
         * <p>
         * <strong>Caution:</strong> MobiFlight firmware provides the resulting value to the underlying hardware IO API.
         * PWM generally means that the hardware pin will be rapidly toggled on/off instead of applying a steady
         * fractional voltage. Rapid toggling may damage incompatible electric components.
         * <strong>Apply only to pins whose connected hardware is known to support PWM.</strong>
         * You may want to check host-side if configuration files permit/deny PWM; see {@link OutputDisplay#usePWM()}.
         * </p>
         * <p>
         * <strong>Extra caution is advised when setting values out of unsigned 8-bit range.</strong> Inspection of
         * MobiFlight firmware source code shows that 16-bit signed integers are supported on protocol level but the
         * effect is unclear. <strong>The result may depend on firmware version and interface module
         * specifics.</strong> If in doubt, limit the value to 8-bit unsigned integer range
         * ({@link Numbers#requireUint8(int)}) or use {@link #setPwmDutyCycleFraction(double)} instead.
         * </p>
         *
         * @param value fraction to set output pin to
         * @return same instance for method-chaining
         * @see #setPwmDutyCycleFraction(double)
         */
        public Builder setPwmDutyCycleValue(int value) {
            this.state = requireInt16(value);
            return this;
        }

        /**
         * Sets the output pin to fully on/off according to the given state.
         *
         * @param state {@code true} enables the output pin (fully on), {@code false} disables it (fully off)
         * @return same instance for method-chaining
         */
        public Builder setDigitalState(boolean state) {
            if (state) {
                return enable();
            } else {
                return disable();
            }
        }

        /**
         * Constructs a {@link SetPinMessage} as configured.
         *
         * @return {@link SetPinMessage} according to configuration
         */
        public SetPinMessage build() {
            if (pin == null) {
                throw new IllegalArgumentException("pin must be defined");
            }

            if (state == null) {
                throw new IllegalArgumentException("state/duty cycle must be defined");
            }

            return new SetPinMessage(pin, state);
        }
    }
}

