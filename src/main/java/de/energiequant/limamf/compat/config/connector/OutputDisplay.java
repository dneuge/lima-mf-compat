package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

import de.energiequant.limamf.compat.config.devices.DeviceConfiguration;
import de.energiequant.limamf.compat.config.devices.OutputConfiguration;
import de.energiequant.limamf.compat.protocol.GetConfigMessage;
import de.energiequant.limamf.compat.protocol.SetPinMessage;
import de.energiequant.limamf.compat.utils.Numbers;

/**
 * Information needed to control an output described on module-side by {@link OutputConfiguration} via a
 * {@link SetPinMessage}.
 */
public class OutputDisplay extends Display {
    private final String pin;
    private final int pinBrightness;
    private final boolean pwm;

    /**
     * Parses the given settings DOM {@link Node} to {@link OutputDisplay}.
     *
     * @param displayNode XML node to parse
     */
    OutputDisplay(Node displayNode) {
        super(displayNode);

        this.pin = getAttribute(displayNode, "pin")
            .orElseThrow(() -> new IllegalArgumentException("output display must specify pin"));

        this.pinBrightness = getAttribute(displayNode, "pinBrightness")
            .map(Numbers::requireUint8)
            .orElseThrow(() -> new IllegalArgumentException("output display must specify pin brightness"));

        this.pwm = getAttribute(displayNode, "pinPwm")
            .map(Boolean::parseBoolean)
            .orElse(false);

        if (!pwm && (pinBrightness != 0 && pinBrightness != 255)) {
            throw new IllegalArgumentException("inconsistent setting: brightness " + pinBrightness + " specified without PWM");
        }
    }

    /**
     * Returns the module's device name.
     * <p>
     * <strong>Do not confuse with the actual component pin</strong> ({@link OutputConfiguration#getPin()}) which
     * describes the hardware component ID within the physical module. The "pin" returned here actually is the name
     * ({@link OutputConfiguration#getName()}), not the component ID. {@link SetPinMessage} needs the component ID,
     * so you will need to resolve this "pin" using a {@link DeviceConfiguration} (preferably read from the device by
     * {@link GetConfigMessage}) before being able to use it.
     * </p>
     *
     * @return device name used by module to refer to output pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * The maximum value (brightness) supposed to be set.
     *
     * @return maximum value supposed to be set
     */
    public int getPinBrightness() {
        return pinBrightness;
    }

    /**
     * Indicates whether the output is permitted to be controlled via PWM or if it is supposed to be set to a stead
     * on/off signal.
     *
     * @return {@code true} if PWM is permitted, {@code false} if PWM should not be used
     */
    public boolean usePWM() {
        return pwm;
    }
}
