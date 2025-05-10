package de.energiequant.limamf.compat.protocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Requests device identification to be returned (replied to with {@link IdentificationInfoMessage}).
 *
 * @see IdentificationInfoMessage
 */
public class GetInfoMessage extends CommandMessage {
    private static final Set<String> TESTED_FIRMWARE_VERSIONS = new HashSet<>(Arrays.asList(
        "2.5.1"
    ));

    public GetInfoMessage() {
        super(CommandType.GET_INFO, Collections.emptyList());
    }

    @Override
    public boolean isCriticalOperation() {
        return false;
    }

    @Override
    public boolean isTestedVersion(String version) {
        return TESTED_FIRMWARE_VERSIONS.contains(version);
    }

    @Override
    public MessageDirection getMessageDirection() {
        return MessageDirection.SENT_ONLY;
    }
}
