package de.energiequant.limamf.compat.utils;

import java.util.Collection;
import java.util.Optional;

public class CollectionUtils {
    private CollectionUtils() {
        // utility class; hide constructor
    }

    public static <T> T exactlyOne(Collection<T> collection) {
        int size = collection.size();
        if (size != 1) {
            throw new IllegalArgumentException("expected exactly one item but got " + size);
        }

        return collection.iterator().next();
    }

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
