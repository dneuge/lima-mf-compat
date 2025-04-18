package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import org.w3c.dom.Node;

import de.energiequant.limamf.compat.utils.Numbers;

public class OutputDisplay extends Display {
    private final String pin;
    private final int pinBrightness;
    private final boolean pwm;

    public OutputDisplay(Node displayNode) {
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

    public String getPin() {
        return pin;
    }

    public int getPinBrightness() {
        return pinBrightness;
    }

    public boolean usePWM() {
        return pwm;
    }
}
