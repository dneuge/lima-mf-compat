package de.energiequant.limamf.compat.protocol;

import static de.energiequant.limamf.compat.utils.Numbers.requireInt16;
import static de.energiequant.limamf.compat.utils.Numbers.requireUint8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    protected SetPinMessage(int pin, int state) {
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer pin;
        private Integer state;

        public Builder manipulatingPin(int pin) {
            this.pin = requireUint8(pin);
            return this;
        }

        public Builder manipulating(OutputConfiguration outputConfiguration) {
            return manipulatingPin(outputConfiguration.getPin());
        }

        public Builder manipulating(DeviceConfiguration deviceConfiguration) {
            if (deviceConfiguration instanceof OutputConfiguration) {
                return manipulating((OutputConfiguration) deviceConfiguration);
            }

            throw new IllegalArgumentException("Unsupported type: " + deviceConfiguration);
        }

        public Builder enable() {
            this.state = STATE_DIGITAL_ON;
            return this;
        }

        public Builder disable() {
            this.state = STATE_DIGITAL_OFF;
            return this;
        }

        public Builder setPwmDutyCycleFraction(double fraction) {
            this.state = Numbers.limit(Math.round(fraction * 255), 0, 255);
            return this;
        }

        public Builder setPwmDutyCycleValue(int value) {
            this.state = requireInt16(value);
            return this;
        }

        public Builder setDigitalState(boolean state) {
            if (state) {
                return enable();
            } else {
                return disable();
            }
        }

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

