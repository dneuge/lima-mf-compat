package de.energiequant.limamf.compat.protocol;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Requests configuration to be returned (replied to with {@link ConfigurationInfoMessage}).
 *
 * @see InfoMessage
 */
public class GetConfigMessage extends CommandMessage {
    private static final Set<String> TESTED_FIRMWARE_VERSIONS = new HashSet<>(Arrays.asList(
        "2.5.1"
    ));

    public GetConfigMessage() {
        super(CommandType.GET_CONFIG, Collections.emptyList());
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
