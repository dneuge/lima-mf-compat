package de.energiequant.limamf.compat.protocol;

import java.util.List;

import de.energiequant.limamf.compat.config.connector.ConnectorConfiguration;
import de.energiequant.limamf.compat.config.connector.InputMultiplexerSettings;
import de.energiequant.limamf.compat.utils.Numbers;

/**
 * Transports an event detected from a multiplexed digital input.
 */
public class DigitalInputMultiplexerChangeMessage extends CommandMessage {
    private final String name;
    private final int channel;
    private final Event event;

    /**
     * The event transported by a {@link DigitalInputMultiplexerChangeMessage}.
     * <p>
     * Note that the events are named according to MobiFlight firmware source code, although, depending on the connected
     * component, the event may not actually correspond to button press/release. Use {@link #isActive()} instead of
     * comparing enums if a boolean is needed or the names are confusing for the component being interacted with.
     * </p>
     */
    public enum Event {
        /**
         * Indicates that the digital input was toggled on, i.e. a button press (if the input is a button/switch).
         */
        PRESS(0, true),
        /**
         * Indicates that the digital input was toggled off, i.e. a button release (if the input is a button/switch).
         */
        RELEASE(1, false);

        private final int encoding;
        private final boolean state;

        Event(int encoding, boolean state) {
            this.encoding = encoding;
            this.state = state;
        }

        /**
         * Returns the input's digital state as a plain boolean.
         *
         * @return {@code true} if the digital input is on, {@code false} if it is off
         */
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

    /**
     * Parses the given {@link CommandMessage} to a {@link DigitalInputMultiplexerChangeMessage}.
     *
     * @param msg message to parse
     */
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

    /**
     * Returns the name of the multiplexer producing the event.
     *
     * @return multiplexer name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the channel ID of the multiplexer producing the event.
     * <p>
     * Instead of hard-coding numeric channel IDs it may be preferable to alias them to names by matching
     * {@link InputMultiplexerSettings} from a host-side {@link ConnectorConfiguration} for the module.
     * </p>
     *
     * @return multiplexer channel ID
     */
    public int getChannel() {
        return channel;
    }

    /**
     * Returns the digital event.
     *
     * @return digital event
     */
    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "DigitalInputMultiplexerChangeMessage(\"" + name + "\", channel " + channel + ", " + event + ")";
    }
}
