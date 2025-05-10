package de.energiequant.limamf.compat.utils;

import java.util.Collection;
import java.util.Optional;

/**
 * Helper methods for {@link Collection}s.
 */
public class CollectionUtils {
    private CollectionUtils() {
        // utility class; hide constructor
    }

    /**
     * Requires the given {@link Collection} to hold exactly one item and returns it.
     * An {@link IllegalArgumentException} will be thrown if this is not the case.
     *
     * @param collection collection required to have exactly one item
     * @param <T>        item type
     * @return the collection's sole item
     * @throws IllegalArgumentException if the {@link Collection} is empty or holds more than one item
     * @see #atMostOne(Collection)
     */
    public static <T> T exactlyOne(Collection<T> collection) {
        int size = collection.size();
        if (size != 1) {
            throw new IllegalArgumentException("expected exactly one item but got " + size);
        }

        return collection.iterator().next();
    }

    /**
     * Requires the given {@link Collection} to hold at most one item. The returned {@link Optional} holds the
     * item, if present.
     * An {@link IllegalArgumentException} will be thrown if the collection holds more than one item.
     *
     * @param collection collection required to have at most one item
     * @param <T>        item type
     * @return the collection's sole item, if present
     * @throws IllegalArgumentException if the {@link Collection} holds more than one item
     * @see #exactlyOne(Collection)
     */
    public static <T> Optional<T> atMostOne(Collection<T> collection) {
        int size = collection.size();
        if (size == 0) {
            return Optional.empty();
        }
        if (size == 1) {
            return Optional.of(collection.iterator().next());
        }

        throw new IllegalArgumentException("Expected at most one item but got " + size);
    }
}
