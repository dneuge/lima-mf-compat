package de.energiequant.limamf.compat.config.connector;

import java.util.Collection;

/**
 * Base interface to access information related to a {@link ConnectorConfiguration}.
 */
public interface ConfigNode {
    /**
     * Returns all direct children related to the surrounding {@link ConnectorConfiguration}.
     *
     * @return direct children
     */
    Collection<Object> getChildren();
}
