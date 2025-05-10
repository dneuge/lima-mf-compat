package de.energiequant.limamf.compat.config.connector;

import static de.energiequant.limamf.compat.utils.CollectionUtils.atMostOne;
import static de.energiequant.limamf.compat.utils.CollectionUtils.exactlyOne;
import static de.energiequant.limamf.compat.utils.DOMUtils.findChildElementsNamed;
import static de.energiequant.limamf.compat.utils.DOMUtils.getAttribute;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public class ConfigItem implements ConfigNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigItem.class);

    private final UUID guid;
    private final Direction direction;
    private final Settings settings;
    private boolean active;
    private String description;

    public enum Direction {
        INPUT("inputs"),
        OUTPUT("outputs");

        private final String xmlParentElementName;

        Direction(String xmlParentElementName) {
            this.xmlParentElementName = xmlParentElementName;
        }

        static Optional<Direction> fromXmlParentElementName(String xmlParentElementName) {
            for (Direction direction : values()) {
                if (direction.xmlParentElementName.equals(xmlParentElementName)) {
                    return Optional.of(direction);
                }
            }

            return Optional.empty();
        }
    }

    private ConfigItem(UUID guid, Direction direction, Settings settings) {
        this.guid = guid;
        this.direction = direction;
        this.settings = settings;
    }

    public UUID getGUID() {
        return guid;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isActive() {
        return active;
    }

    public ConfigItem setActive(boolean active) {
        this.active = active;
        return this;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public ConfigItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public Settings getSettings() {
        return settings;
    }

    static ConfigItem fromXML(ConfigItem.Direction direction, Node node) {
        UUID guid = getAttribute(node, "guid").map(UUID::fromString).orElseThrow(() -> new IllegalArgumentException("config item without GUID"));

        Settings settings = Settings.fromXML(exactlyOne(findChildElementsNamed(node, "settings")));
        ConfigItem out = new ConfigItem(guid, direction, settings);

        atMostOne(findChildElementsNamed(node, "active"))
            .map(Node::getTextContent)
            .map(Boolean::parseBoolean)
            .ifPresent(out::setActive);

        atMostOne(findChildElementsNamed(node, "description"))
            .map(Node::getTextContent)
            .ifPresent(out::setDescription);

        return out;
    }

    @Override
    public Collection<Object> getChildren() {
        return Collections.singletonList(settings);
    }
}
