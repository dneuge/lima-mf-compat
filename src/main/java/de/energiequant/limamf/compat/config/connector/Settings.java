package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.CollectionUtils.atMostOne;
import static de.energiequant.limamf.compat.utils.DOMUtils.findChildElementsNamed;
import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import de.energiequant.limamf.compat.utils.Maps;

/**
 * Base class holding "settings" in terms of MobiFlight configuration.
 */
public class Settings implements ModuleBindable, ConfigNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(Settings.class);

    private final Display display;
    private final String serial;

    private static final Map<String, Function<Node, Settings>> DECODERS_BY_TYPE = Maps.createHashMap(
        Maps.entry("Encoder", EncoderSettings::new),
        Maps.entry("InputMultiplexer", InputMultiplexerSettings::new)
    );

    /**
     * Base constructor reading common information from given DOM node.
     *
     * @param node node to parse
     */
    protected Settings(Node node) {
        this.display = atMostOne(findChildElementsNamed(node, "display"))
            .flatMap(Display::fromXML)
            .orElse(null);

        this.serial = getAttribute(node, "serial").orElse(null);
    }

    /**
     * Returns the {@link Display}, if configured.
     *
     * @return associated {@link Display}
     */
    public Optional<Display> getDisplay() {
        return Optional.ofNullable(display);
    }

    @Override
    public Optional<String> getSerial() {
        return Optional.ofNullable(serial);
    }

    /**
     * Parses the given XML DOM {@link Node} to {@link Settings}, using specific implementations if available.
     *
     * @param node XML DOM {@link Node} to parse
     * @return parsed {@link Settings}
     */
    static Settings fromXML(Node node) {
        String type = getAttribute(node, "type").orElse(null);
        if (type == null) {
            return new Settings(node);
        }

        Function<Node, Settings> decoder = DECODERS_BY_TYPE.get(type);
        if (decoder == null) {
            LOGGER.warn("no settings decoder for {}", type);
            return new Settings(node);
        }

        return decoder.apply(node);
    }

    @Override
    public Collection<Object> getChildren() {
        if (display != null) {
            return Collections.singletonList(display);
        }

        return Collections.emptyList();
    }
}
