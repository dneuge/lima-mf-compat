package de.energiequant.limamf.compat.protocol;

import java.util.List;

/**
 * Transports an event detected from a rotary encoder.
 */
public class EncoderChangeMessage extends CommandMessage {
    /**
     * The event transported by an {@link EncoderChangeMessage}.
     */
    public enum Event {
        /**
         * Rotary encoder was turned left without triggering fast movement criteria.
         */
        LEFT(0, false, false),
        /**
         * Rotary encoder was turned left in a fast movement, as detected by the module.
         */
        LEFT_FAST(1, false, true),
        /**
         * Rotary encoder was turned right without triggering fast movement criteria.
         */
        RIGHT(2, true, false),
        /**
         * Rotary encoder was turned right in a fast movement, as detected by the module.
         */
        RIGHT_FAST(3, true, true);

        private final int encoding;
        private final boolean clockwise;
        private final boolean fast;

        Event(int encoding, boolean clockwise, boolean fast) {
            this.encoding = encoding;
            this.clockwise = clockwise;
            this.fast = fast;
        }

        /**
         * Indicates the turn direction.
         *
         * @return {@code true} if the encoder was rotated clockwise ("left"), {@code false} if counter-clockwise ("right")
         */
        public boolean isClockwise() {
            return clockwise;
        }

        /**
         * Indicates if fast rotation was detected by the module.
         *
         * @return {@code true} if the encoder was turned in a fast movement, {@code false} if not
         */
        public boolean isFast() {
            return fast;
        }

        private static Event decode(String encoding) {
            return decode(Integer.parseInt(encoding));
        }

        private static Event decode(int encoding) {
            for (Event event : values()) {
                if (event.encoding == encoding) {
                    return event;
                }
            }

            throw new IllegalArgumentException("Unsupported encoding: " + encoding);
        }
    }

    private final String name;
    private final Event event;

    /**
     * Parses the given {@link CommandMessage} to an {@link EncoderChangeMessage}.
     *
     * @param msg message to parse
     */
    public EncoderChangeMessage(CommandMessage msg) {
        super(msg);

        List<String> fields = msg.getFields();
        if (fields.size() != 2) {
            throw new IllegalArgumentException("Unexpected number of fields, got " + fields.size());
        }

        this.name = fields.get(0);
        this.event = Event.decode(fields.get(1));
    }

    /**
     * Returns the name of the encoder input producing the event.
     *
     * @return encoder input name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the encoder event, specifying direction and speed.
     *
     * @return encoder event
     */
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "EncoderChangeMessage(\"" + name + "\", " + event + ")";
    }
}
