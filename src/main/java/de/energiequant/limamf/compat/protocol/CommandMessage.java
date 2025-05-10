package de.energiequant.limamf.compat.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.energiequant.limamf.compat.utils.Numbers;

/**
 * Stripped-down implementation for basic communication with MobiFlight within the constraints of the specific
 * surrounding application.
 */
public class CommandMessage {
    private final CommandType type;
    private final int typeId;
    private final List<String> fields;

    /**
     * standard field separator used by MobiFlight protocol messages
     */
    public static final char FIELD_SEPARATOR = ',';
    /**
     * standard command separator used by MobiFlight protocol messages
     */
    public static final char COMMAND_SEPARATOR = ';';
    /**
     * standard escape character used by MobiFlight protocol messages
     */
    public static final char ESCAPE_CHARACTER = '/'; // not a typo, this is actually a forward slash

    /**
     * Constructs a message for a known {@link CommandType}.
     *
     * @param type   message type
     * @param fields message field values
     */
    protected CommandMessage(CommandType type, List<String> fields) {
        this.type = type;
        this.typeId = Numbers.requireUint8(type.getFirmwareEncoding());
        this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
    }

    /**
     * Constructs a message for an unknown type.
     *
     * @param typeId raw message type ID
     * @param fields message field values
     */
    protected CommandMessage(int typeId, List<String> fields) {
        this.type = CommandType.fromFirmwareEncoding(typeId).orElse(null);
        this.typeId = Numbers.requireUint8(typeId);
        this.fields = Collections.unmodifiableList(new ArrayList<>(fields));
    }

    /**
     * Constructor to use when extending an existing message to a more specific implementation.
     *
     * @param base message to extend
     */
    protected CommandMessage(CommandMessage base) {
        this.type = base.type;
        this.typeId = base.typeId;
        this.fields = base.fields;
    }

    /**
     * Returns the message type, if known.
     *
     * @return known message type
     * @see #getTypeId()
     */
    public Optional<CommandType> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Returns the raw message type ID.
     * <p>
     * This is mainly useful for debugging; use {@link #getType()} to identify known messages instead.
     * </p>
     *
     * @return raw message type ID
     * @see #getType()
     */
    public int getTypeId() {
        return typeId;
    }

    /**
     * Returns all field values transported by the message.
     *
     * @return all field values
     */
    public List<String> getFields() {
        return fields;
    }

    /**
     * Serializes the message for sending it to a MobiFlight module. The encoded message omits the command separator.
     *
     * @return raw encoded message, without command separator
     */
    public String serialize() {
        StringBuilder sb = new StringBuilder();

        sb.append(typeId);

        for (String field : fields) {
            sb.append(FIELD_SEPARATOR);
            serializeField(sb, field);
        }

        return sb.toString();
    }

    private void serializeField(StringBuilder sb, String s) {
        for (char ch : s.toCharArray()) {
            if (ch == ESCAPE_CHARACTER || ch == FIELD_SEPARATOR || ch == COMMAND_SEPARATOR) {
                sb.append(ESCAPE_CHARACTER);
            }

            sb.append(ch);
        }
    }

    /**
     * Checks if the command has been confirmed to be syntactically correct for the given firmware version.
     *
     * @param version firmware version to check against
     * @return {@code true} if tested, {@code false} if untested
     */
    public boolean isTestedVersion(String version) {
        // safe default: assume everything to be untested, initially
        return false;
    }

    /**
     * Checks if the command represents a potentially critical operation such as manipulating physical states or
     * causing issues on the interface.
     * <p>
     * Safe default, also if not tested/unsure, is to assume the worst and indicate a critical operation even when
     * it should theoretically not be.
     * </p>
     *
     * @return {@code true} if potentially critical, {@code false} if assumed to be less severe when sent to interface
     */
    public boolean isCriticalOperation() {
        // safe default: assume everything to be critical unless told otherwise
        return true;
    }

    /**
     * Indicates the direction(s) this message can flow.
     *
     * @return direction of message flow
     */
    public MessageDirection getMessageDirection() {
        return MessageDirection.RECEIVED_ONLY;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CommandMessage(");

        sb.append(typeId);

        if (type != null) {
            sb.append("/");
            sb.append(type);
        }

        for (String field : fields) {
            sb.append(", \"");
            sb.append(field);
            sb.append("\"");
        }

        sb.append(")");

        return sb.toString();
    }
}
