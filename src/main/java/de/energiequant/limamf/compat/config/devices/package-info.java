/**
 * This package contains classes describing module-side MobiFlight firmware configuration. Such configuration describes
 * low-level hardware components inside the module, called "devices" by MobiFlight, and their aliases as used for
 * communicating over the serial protocol. Such module-side configuration is grouped by
 * {@link de.energiequant.limamf.compat.config.devices.InterfaceConfiguration}. The currently active configuration can
 * be requested from a hardware module by sending a {@link de.energiequant.limamf.compat.protocol.GetConfigMessage}
 * request.
 * <p>
 * Devices are generally just defined on the first hardware level, i.e. "what the microcontroller directly interacts
 * with". Information about multiplexed signals or component-specific interaction (e.g. support for PWM) is beyond the
 * scope of this hardware description and is instead defined in the host-side runtime configuration
 * ({@link de.energiequant.limamf.compat.config.connector.ConnectorConfiguration}).
 * </p>
 */
package de.energiequant.limamf.compat.config.devices;