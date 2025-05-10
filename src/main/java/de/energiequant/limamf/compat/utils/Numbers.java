package de.energiequant.limamf.compat.utils;

/**
 * Helper methods to handle numeric information.
 */
public class Numbers {
    private static final int MIN_INT16 = -32768;
    private static final int MAX_INT16 = 32767;

    private Numbers() {
        // utility class; hide constructor
    }

    /**
     * Parses the given string and checks that it is in range for an unsigned 8-bit integer (0..255), throwing an
     * {@link IllegalArgumentException} if not.
     *
     * @param s string to parse as an 8-bit unsigned integer
     * @return parsed integer, in range
     * @throws IllegalArgumentException if out of range
     * @throws NumberFormatException    if unparseable
     */
    public static int requireUint8(String s) {
        return requireUint8(Integer.parseInt(s));
    }

    /**
     * Checks if the given value is in range for an unsigned 8-bit integer (0..255) and throws an
     * {@link IllegalArgumentException} if not. The same value is returned, if it is valid.
     *
     * @param value value to check for 8-bit unsigned integer range
     * @return same value, for method-chaining
     * @throws IllegalArgumentException if out of range
     */
    public static int requireUint8(int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("expected unsigned byte, got " + value);
        }

        return value;
    }

    /**
     * Checks if the given value is in range for a signed 16-bit integer ({@value #MIN_INT16}..{@value #MAX_INT16})
     * and throws an {@link IllegalArgumentException} if not. The same value is returned, if it is valid.
     *
     * @param value value to check for 16-bit signed integer range
     * @return same value, for method-chaining
     * @throws IllegalArgumentException if out of range
     */
    public static int requireInt16(int value) {
        if (value < MIN_INT16 || value > MAX_INT16) {
            throw new IllegalArgumentException("expected signed 16-bit integer, got " + value);
        }

        return value;
    }

    /**
     * Limits the given value to the specified range. Excessive values are clipped at min/max.
     *
     * @param value   value to limit
     * @param minIncl minimum value (inclusive)
     * @param maxIncl maximum value (inclusive)
     * @return value limited to specified range
     */
    public static int limit(long value, int minIncl, int maxIncl) {
        if (value < minIncl) {
            return minIncl;
        }

        if (value > maxIncl) {
            return maxIncl;
        }

        return (int) value;
    }

    /**
     * Limits the given value to the specified range. Excessive values are clipped at min/max.
     *
     * @param value   value to limit
     * @param minIncl minimum value (inclusive)
     * @param maxIncl maximum value (inclusive)
     * @return value limited to specified range
     */
    public static int limit(int value, int minIncl, int maxIncl) {
        if (value < minIncl) {
            return minIncl;
        }

        if (value > maxIncl) {
            return maxIncl;
        }

        return value;
    }
}
