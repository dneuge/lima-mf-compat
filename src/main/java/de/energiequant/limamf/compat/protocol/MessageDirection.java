package de.energiequant.limamf.compat.protocol;

/**
 * Marks the direction in which a MobiFlight protocol message can flow.
 */
public enum MessageDirection {
    /**
     * The message is only supposed to be received from an interface.
     */
    RECEIVED_ONLY,
    /**
     * The message is only supposed to be sent to an interface. Reception from an interface is unexpected.
     */
    SENT_ONLY,
    /**
     * The message is valid in both directions.
     */
    BOTH;
}
