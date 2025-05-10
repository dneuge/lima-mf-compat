package de.energiequant.limamf.compat.protocol;

import static de.energiequant.limamf.compat.protocol.CommandMessage.ESCAPE_CHARACTER;
import static de.energiequant.limamf.compat.protocol.CommandMessage.FIELD_SEPARATOR;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.UnaryOperator;

import de.energiequant.limamf.compat.utils.Maps;

/**
 * Decodes {@link CommandMessage}s serialized according to MobiFlight firmware protocol.
 */
public class CommandMessageDecoder {
    private static final Map<CommandType, UnaryOperator<CommandMessage>> DECODERS_BY_COMMAND_TYPE = Maps.createEnumMap(
        CommandType.class,
        Maps.entry(CommandType.DIG_IN_MUX_CHANGE, DigitalInputMultiplexerChangeMessage::new),
        Maps.entry(CommandType.ENCODER_CHANGE, EncoderChangeMessage::new),
        Maps.entry(CommandType.INFO, InfoMessage::decode)
    );

    /**
     * Decodes a single {@link CommandMessage} from the given raw protocol message.
     *
     * @param s raw protocol message to decode
     * @return decoded {@link CommandMessage}
     */
    public CommandMessage deserialize(String s) {
        LinkedList<String> fields = new LinkedList<>();

        StringBuilder collector = new StringBuilder();

        boolean inEscape = false;
        for (char ch : s.toCharArray()) {
            if (inEscape) {
                collector.append(ch);
                inEscape = false;
            } else if (ch == ESCAPE_CHARACTER) {
                inEscape = true;
            } else if (ch == FIELD_SEPARATOR) {
                fields.add(collector.toString());
                collector = new StringBuilder();
            } else {
                collector.append(ch);
            }
        }
        if (inEscape) {
            throw new IllegalArgumentException("Open escape on command message: \"" + s + "\"");
        }
        fields.add(collector.toString());

        String typeIdString = fields.removeFirst();
        if (typeIdString.isEmpty()) {
            throw new IllegalArgumentException("Missing type on command message: \"" + s + "\"");
        }
        int typeId;
        try {
            typeId = Integer.parseInt(typeIdString);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Failed to parse type from command message (not an integer): \"" + s + "\"", ex);
        }
        if (typeId < 0 || typeId > 255) {
            throw new IllegalArgumentException("Failed to parse type from command message (out of range): \"" + s + "\"");
        }

        CommandMessage msg = new CommandMessage(typeId, fields);

        UnaryOperator<CommandMessage> decoder = msg.getType()
                                                   .map(DECODERS_BY_COMMAND_TYPE::get)
                                                   .orElse(null);
        if (decoder != null) {
            try {
                msg = decoder.apply(msg);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Failed to decode command message: \"" + s + "\"", ex);
            }
        }

        return msg;
    }
}
