package com.caychen.boot.common.utils.convert;

import java.util.Arrays;

/**
 * @Author: Caychen
 * @Date: 2024/2/20 16:50
 * @Description:
 */
public class ArraysUtil {

    public static final byte[] arraycopy(byte[] begin, byte[] end) {
        if (begin == null) {
            return end;
        } else if (end == null) {
            return begin;
        } else {
            int length = begin.length + end.length;
            byte[] result = new byte[length];
            System.arraycopy(begin, 0, result, 0, begin.length);
            System.arraycopy(end, 0, result, begin.length, end.length);
            return result;
        }
    }

    public static final byte[] arrayappend(byte[] source, int sourceIndex, byte[] end) {
        if (end.length + sourceIndex > source.length) {
            throw new IndexOutOfBoundsException("length = " + source.length + " , total length = " + (sourceIndex + end.length));
        } else {
            System.arraycopy(end, 0, source, sourceIndex, end.length);
            return source;
        }
    }

    public static final byte[] subarrays(byte[] bytes, int startIndex, int length) {
        if (startIndex == 0 && bytes.length == length) {
            return bytes;
        } else {
            byte[] rbytes;
            if (length != -1 && bytes.length - startIndex >= length) {
                rbytes = new byte[length];
                System.arraycopy(bytes, startIndex, rbytes, 0, length);
                return rbytes;
            } else {
                rbytes = new byte[bytes.length - startIndex];
                System.arraycopy(bytes, startIndex, rbytes, 0, rbytes.length);
                return rbytes;
            }
        }
    }

    public static final byte[] subarrays(byte[] bytes, int startIndex) {
        if (startIndex > bytes.length) {
            throw new IndexOutOfBoundsException("statIndex = " + startIndex + " , bytes length = " + bytes.length + "");
        } else {
            byte[] rbytes = new byte[bytes.length - startIndex];
            System.arraycopy(bytes, startIndex, rbytes, 0, rbytes.length);
            return rbytes;
        }
    }

    public static final boolean equals(byte[] frist, byte[] second) {
        return Arrays.equals(frist, second);
    }

    public static final int indexOf(byte b, byte[] bytes) {
        int i = 0;

        for (int length = bytes.length; i < length; ++i) {
            if (b == bytes[i]) {
                return i;
            }
        }

        return -1;
    }
}
