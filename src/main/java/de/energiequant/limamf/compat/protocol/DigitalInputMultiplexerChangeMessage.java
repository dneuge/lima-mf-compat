package de.energiequant.limamf.compat.protocol;

import java.util.List;

import de.energiequant.limamf.compat.utils.Numbers;

public class DigitalInputMultiplexerChangeMessage extends CommandMessage {
    public static final int TYPE_ID = 30;

    private final String name;
    private final int channel;
    private final Event event;

    public enum Event {
        PRESS(0, true),
        RELEASE(1, false);

        private final int encoding;
        private final boolean state;

        Event(int encoding, boolean state) {
            this.encoding = encoding;
            this.state = state;
        }

        public boolean isActive() {
            return state;
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

    public DigitalInputMultiplexerChangeMessage(CommandMessage msg) {
        super(msg);

        List<String> fields = msg.getFields();
        if (fields.size() != 3) {
            throw new IllegalArgumentException("Unexpected number of fields, got " + fields.size());
        }

        this.name = fields.get(0);
        this.channel = Numbers.requireUint8(fields.get(1));
        this.event = Event.decode(fields.get(2));
    }

    public String getName() {
        return name;
    }

    public int getChannel() {
        return channel;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "DigitalInputMultiplexerChangeMessage(\"" + name + "\", channel " + channel + ", " + event + ")";
    }
}
