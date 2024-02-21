package com.caychen.boot.common.utils.lang;

import com.caychen.boot.common.utils.convert.ArraysUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2024/2/20 17:02
 * @Description:
 */
public class BytesArray {

    private List<byte[]> _arrays;
    private int _arrays_length;

    public BytesArray() {
        if (this._arrays == null) {
            this._arrays = new ArrayList();
            this._arrays_length = 0;
        }

    }

    public BytesArray append(byte[] bytes) {
        this._arrays.add(bytes);
        this._arrays_length += bytes.length;
        return this;
    }

    public byte[] get() {
        byte[] bytes = new byte[this._arrays_length];
        int index = 0;

        byte[] b;
        for (Iterator var3 = this._arrays.iterator(); var3.hasNext(); index += b.length) {
            b = (byte[]) var3.next();
            ArraysUtil.arrayappend(bytes, index, b);
        }

        return bytes;
    }
}
