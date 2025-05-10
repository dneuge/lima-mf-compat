/**
 * This package implements the protocol used by MobiFlight firmware over (text-based) serial communication channels.
 * <p>
 * The protocol works by sending {@link de.energiequant.limamf.compat.protocol.CommandMessage} in both directions.
 * Each message is delimited by a {@link de.energiequant.limamf.compat.protocol.CommandMessage#COMMAND_SEPARATOR}
 * (<code>{@value de.energiequant.limamf.compat.protocol.CommandMessage#COMMAND_SEPARATOR}</code>). Line breaks are not
 * used for message separation. Messages always start with a numeric type ID, followed by a type-specific number of
 * fields, delimited by {@link de.energiequant.limamf.compat.protocol.CommandMessage#FIELD_SEPARATOR}s
 * (<code>{@value de.energiequant.limamf.compat.protocol.CommandMessage#FIELD_SEPARATOR}</code>). Conflicting characters
 * can be escaped using {@link de.energiequant.limamf.compat.protocol.CommandMessage#ESCAPE_CHARACTER}
 * (<code>{@value de.energiequant.limamf.compat.protocol.CommandMessage#ESCAPE_CHARACTER}</code>) (which may however
 * not be consistently used by all fields/message types).
 * </p>
 * <p>
 * All messages can be decoded using
 * {@link de.energiequant.limamf.compat.protocol.CommandMessageDecoder#deserialize(java.lang.String)}. Supported
 * messages will decode to specialized classes while unsupported messages will still decode numeric type information
 * and field values, just lacking any further interpretation.
 * </p>
 * <p>
 * Outbound messages can be serialized after instantiating/building one of the specialized classes.
 * </p>
 * <p>
 * Note that this library does not implement (platform-dependent) device communication but only provides protocol
 * (de)serialization.
 * </p>
 */
package de.energiequant.limamf.compat.protocol;