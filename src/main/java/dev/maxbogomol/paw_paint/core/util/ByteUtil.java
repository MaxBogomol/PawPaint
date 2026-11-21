package dev.maxbogomol.paw_paint.core.util;

public class ByteUtil {
    public static void writeUInt16(byte[] buf, int offset, int value) {
        buf[offset] = (byte) (value & 0xFF);
        buf[offset + 1] = (byte) ((value >> 8) & 0xFF);
    }

    public static int readUInt16(byte[] buf, int offset) {
        return (buf[offset] & 0xFF) | ((buf[offset + 1] & 0xFF) << 8);
    }

    public static void writeUInt32(byte[] buf, int offset, long value) {
        buf[offset] = (byte) (value & 0xFF);
        buf[offset + 1] = (byte) ((value >> 8) & 0xFF);
        buf[offset + 2] = (byte) ((value >> 16) & 0xFF);
        buf[offset + 3] = (byte) ((value >> 24) & 0xFF);
    }

    public static long readUInt32(byte[] buf, int offset) {
        return (buf[offset] & 0xFFL) | ((buf[offset + 1] & 0xFFL) << 8) | ((buf[offset + 2] & 0xFFL) << 16) | ((buf[offset + 3] & 0xFFL) << 24);
    }

    public static void writeFloat(byte[] buf, int offset, float value) {
        int bits = Float.floatToRawIntBits(value);
        buf[offset] = (byte) (bits & 0xFF);
        buf[offset + 1] = (byte) ((bits >> 8) & 0xFF);
        buf[offset + 2] = (byte) ((bits >> 16) & 0xFF);
        buf[offset + 3] = (byte) ((bits >> 24) & 0xFF);
    }

    public static float readFloat(byte[] buf, int offset) {
        int bits = (buf[offset] & 0xFF) | ((buf[offset + 1] & 0xFF) << 8) | ((buf[offset + 2] & 0xFF) << 16) | ((buf[offset + 3] & 0xFF) << 24);
        return Float.intBitsToFloat(bits);
    }
}
