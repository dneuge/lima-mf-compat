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

/**
 * A general configuration item inside a {@link ConnectorConfiguration}.
 */
public class ConfigItem implements ConfigNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigItem.class);

    private final UUID guid;
    private final Direction direction;
    private final Settings settings;
    private boolean active;
    private String description;

    /**
     * The direction of a {@link ConfigItem}.
     */
    public enum Direction {
        /**
         * The {@link ConfigItem} describes an input (e.g. receiving events from a module).
         */
        INPUT("inputs"),
        /**
         * The {@link ConfigItem} describes an output (e.g. sending events to a module).
         */
        OUTPUT("outputs");

        private final String xmlParentElementName;

        Direction(String xmlParentElementName) {
            this.xmlParentElementName = xmlParentElementName;
        }

        /**
         * Resolves the given XML element name to a {@link Direction}, if known.
         *
         * @param xmlParentElementName name of XML parent element to resolve
         * @return {@link Direction} enum, if known
         */
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

    /**
     * Returns the MobiFlight configuration item GUID ({@link UUID in Java}.
     *
     * @return configuration item GUID
     */
    public UUID getGUID() {
        return guid;
    }

    /**
     * Returns the direction this {@link ConfigItem} is intended to operate in.
     *
     * @return intended direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Indicates whether this {@link ConfigItem} is enabled or disabled.
     *
     * @return {@code true} if enabled, {@code false} if disabled
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Marks the {@link ConfigItem} as enabled or disabled.
     *
     * @param active {@code true} marks the item as enabled; {@code false} marks it as disabled
     * @return same instance for method-chaining
     */
    public ConfigItem setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Returns the text used to describe this {@link ConfigItem}, if present.
     *
     * @return description text
     */
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    /**
     * Sets the description text for this {@link ConfigItem}.
     *
     * @param description description text
     * @return same instance for method-chaining
     */
    public ConfigItem setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the {@link Settings} of this {@link ConfigItem}.
     *
     * @return associates {@link Settings}
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Parses the given XML DOM {@link Node} to a {@link ConfigItem}.
     *
     * @param direction direction the item is intended to operate in
     * @param node      XML DOM {@link Node} to parse
     * @return parsed {@link ConfigItem}
     */
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
