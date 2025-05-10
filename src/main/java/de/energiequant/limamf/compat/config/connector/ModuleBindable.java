package de.energiequant.limamf.compat.config.connector;

import java.util.Optional;

/**
 * Base interface for configuration aspects which can be used to bind a MobiFlight module.
 */
public interface ModuleBindable {
    /**
     * Returns the associated MobiFlight module serial.
     * <p>
     * Note that the serial may be a combination of a hardware module name and module serial ID.
     * </p>
     *
     * @return associated MobiFlight module serial
     */
    Optional<String> getSerial();
}
