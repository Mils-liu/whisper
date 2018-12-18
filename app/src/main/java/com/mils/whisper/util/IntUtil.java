package com.mils.whisper.util;

/**
 * Created by Administrator on 2017/7/24 0024.
 *判断int数据是几位数
 */

public class IntUtil {
    final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
            99999999, 999999999, Integer.MAX_VALUE};

    public static int sizeOfInt(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }
}
