package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import de.energiequant.limamf.compat.utils.Maps;

public class Display implements ModuleBindable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Display.class);

    private final String serial;

    private static final Map<String, Function<Node, Display>> DECODERS_BY_TYPE = Maps.createHashMap(
        Maps.entry("Output", OutputDisplay::new)
    );

    protected Display(Node displayNode) {
        this.serial = getAttribute(displayNode, "serial")
            .orElseThrow(() -> new IllegalArgumentException("display must be linked to a serial"));
    }

    public static Optional<Display> fromXML(Node displayNode) {
        String type = getAttribute(displayNode, "type")
            .orElseThrow(() -> new IllegalArgumentException("display must specify type"));

        if ("-".equals(type)) {
            // dummy entry
            return Optional.empty();
        }

        Function<Node, Display> decoder = DECODERS_BY_TYPE.get(type);
        if (decoder == null) {
            LOGGER.warn("no display decoder for type {}", type);
            return Optional.of(new Display(displayNode));
        }

        return Optional.of(decoder.apply(displayNode));
    }

    @Override
    public Optional<String> getSerial() {
        return Optional.of(serial);
    }
}
