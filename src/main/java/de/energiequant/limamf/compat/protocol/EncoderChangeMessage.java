package de.energiequant.limamf.compat.protocol;

import java.util.List;

/**
 * Transports an event detected from a rotary encoder.
 */
public class EncoderChangeMessage extends CommandMessage {
    public static final int TYPE_ID = 6;

    public enum Event {
        LEFT(0, false, false),
        LEFT_FAST(1, false, true),
        RIGHT(2, true, false),
        RIGHT_FAST(3, true, true);

        private final int encoding;
        private final boolean clockwise;
        private final boolean fast;

        Event(int encoding, boolean clockwise, boolean fast) {
            this.encoding = encoding;
            this.clockwise = clockwise;
            this.fast = fast;
        }

        public boolean isClockwise() {
            return clockwise;
        }

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

    public EncoderChangeMessage(CommandMessage msg) {
        super(msg);

        List<String> fields = msg.getFields();
        if (fields.size() != 2) {
            throw new IllegalArgumentException("Unexpected number of fields, got " + fields.size());
        }

        this.name = fields.get(0);
        this.event = Event.decode(fields.get(1));
    }

    public String getName() {
        return name;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "EncoderChangeMessage(\"" + name + "\", " + event + ")";
    }
}
