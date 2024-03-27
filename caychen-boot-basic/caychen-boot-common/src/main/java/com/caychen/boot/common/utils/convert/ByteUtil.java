package com.caychen.boot.common.utils.convert;

/**
 * @Author: Caychen
 * @Date: 2024/2/20 16:44
 * @Description:
 */
public final class ByteUtil {
    private static final byte[] HEX = "0123456789ABCDEF".getBytes();

    public static String bytesToHexString(byte[] bytes) {
        byte[] buff = new byte[2 * bytes.length];
        int i = 0;

        for (int length = bytes.length; i < length; ++i) {
            buff[2 * i] = HEX[bytes[i] >> 4 & 0xF];
            buff[2 * i + 1] = HEX[bytes[i] & 0xF];
        }

        return new String(buff);
    }

    public static byte[] hexStringToBytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        int i = 0;

        for (int length = b.length; i < length; ++i) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) (parse(c0) << 4 | parse(c1));
        }

        return b;
    }

    private static int parse(char c) {
        if (c >= 'a') {
            return c - 97 + 0xA & 0xF;
        } else {
            return c >= 'A' ? c - 65 + 0xA & 0xF : c - 48 & 0xF;
        }
    }

    public static String decimalToHexadecimal(long number, int x) {
        String hex = Long.toHexString(number).toUpperCase();
        return fillZero(hex, x);
    }

    public static String fillZero(Object text, int length) {
        StringBuilder builder = new StringBuilder(length);
        int i;
        if (text == null) {
            for (i = 0; i < length; ++i) {
                builder.append("0");
            }
        } else {
            for (i = String.valueOf(text).length(); i < length; ++i) {
                builder.append("0");
            }

            builder.append(text);
        }

        return builder.toString();
    }

    public static byte[] longTobytes(long number, int size) {
        byte[] b = new byte[size];

        for (int i = 0; i < size; ++i) {
            b[i] = (byte) ((int) (number >> 8 * (size - i - 1) & 0xFF));
        }

        return b;
    }

    public static long byte2Long(byte[] bytes, int size) {
        long intValue = 0L;

        for (int i = 0; i < bytes.length; ++i) {
            intValue |= (long) (bytes[i] & 0xFF) << 8 * (size - i - 1);
        }

        return intValue;
    }

    public static long byte2SignedLong(byte[] bytes, int size) {
        long intValue = 0L;

        for (int i = 0; i < bytes.length; ++i) {
            intValue |= (long) (bytes[i] & 0xFF) << 8 * (size - i - 1);
        }

        long sign = intValue >> 8 * size - 1;
        return sign > 0L ? intValue - (long) (1L << 8 * size) : intValue;
    }

    public static int byte2Int(byte[] bytes, int size) {
        int intValue = 0;

        for (int i = 0; i < bytes.length; ++i) {
            intValue += (bytes[i] & 0xFF) << 8 * (size - i - 1);
        }

        return intValue;
    }

    public static int byte2SignedInt(byte[] bytes, int size) {
        int intValue = 0;

        int sign;
        for (sign = 0; sign < bytes.length; ++sign) {
            intValue += (bytes[sign] & 0xFF) << 8 * (size - sign - 1);
        }

        sign = intValue >> 8 * size - 1;
        return sign > 0 ? intValue - (1 << 8 * size) : intValue;
    }

    public static long byte2LongLittleEndian(byte[] bytes) {
        long intValue = 0L;

        for (int i = 0; i < bytes.length; ++i) {
            intValue |= (long) (bytes[i] & 0xFF) << 8 * i;
        }

        return intValue;
    }

    public static int byte2IntLittleEndian(byte[] bytes) {
        int intValue = 0;

        for (int i = 0; i < bytes.length; ++i) {
            intValue += (bytes[i] & 0xFF) << 8 * i;
        }

        return intValue;
    }

    public static byte[] intTobytes(int number, int size) {
        byte[] b = new byte[size];

        for (int i = 0; i < size; ++i) {
            b[i] = (byte) (number >> 8 * (size - i - 1) & 0xFF);
        }

        return b;
    }

    public static byte[] byteReverse(byte[] b) {
        byte[] reB = new byte[b.length];

        for (int i = b.length - 1; i >= 0; --i) {
            reB[b.length - i - 1] = b[i];
        }

        return reB;
    }

    public static byte[] float2byte(float f) {
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];

        int len;
        for (len = 0; len < 4; ++len) {
            b[len] = (byte) (fbit >> 24 - len * 8);
        }

        len = b.length;
        byte[] dest = new byte[len];
        System.arraycopy(b, 0, dest, 0, len);

        for (int i = 0; i < len / 2; ++i) {
            byte temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;
    }

    public static float byte2float(byte[] b, int index) {
        int l = b[index];
        l &= 0xFF;
        l = (int) ((long) l | (long) b[index + 1] << 8);
        l &= 0xFFFF;
        l = (int) ((long) l | (long) b[index + 2] << 16);
        l &= 0xFFFFFF;
        l = (int) ((long) l | (long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }
}

