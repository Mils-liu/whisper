package com.mils.whisper.util;

import android.widget.Toast;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2017/11/20 0020.
 */

public class ToastUtil {
    public static void ToastShort(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public static void ToastLong(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
