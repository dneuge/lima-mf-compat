/**
 * This package contains classes describing host-side MobiFlight Connector runtime configuration, generally provided as
 * {@code .mcc} and grouped as a {@link de.energiequant.limamf.compat.config.connector.ConnectorConfiguration}.
 * Such configuration is not exchanged with modules but instead describe host-side interpretation and
 * behaviour, eventually resulting in events sent either to a flight simulator (input) or to a MobiFlight hardware
 * module (output, e.g. driving indicator or backlight LEDs).
 * <p>
 * While not reimplementing the exact MobiFlight data structures or configuration hierarchies, the element and
 * attribute names have been retained based on {@code .mcc} files. While this helps keeping track of the actual
 * file format, it can also cause some confusion due to different meanings of terms like "pin" on host- and device-side.
 * </p>
 */
package de.energiequant.limamf.compat.config.connector;
