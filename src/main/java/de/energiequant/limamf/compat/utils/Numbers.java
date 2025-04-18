package de.energiequant.limamf.compat.utils;

public class Numbers {
    private static final int MIN_INT16 = -32768;
    private static final int MAX_INT16 = 32767;

    private Numbers() {
        // utility class; hide constructor
    }

    public static int requireUint8(String s) {
        return requireUint8(Integer.parseInt(s));
    }

    public static int requireUint8(int value) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("expected unsigned byte, got " + value);
        }

        return value;
    }

    public static int requireInt16(int value) {
        if (value < MIN_INT16 || value > MAX_INT16) {
            throw new IllegalArgumentException("expected signed 16-bit integer, got " + value);
        }

        return value;
    }

    public static int limit(long value, int minIncl, int maxIncl) {
        if (value < minIncl) {
            return minIncl;
        }

        if (value > maxIncl) {
            return maxIncl;
        }

        return (int) value;
    }

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
