package com.mils.whisper.util;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.mils.whisper.R;

import butterknife.BindView;

import static com.mils.whisper.app.MyApplicant.getContext;

/**
 * Created by Administrator on 2018/12/20.
 */

public class ResourceUtil {

    public static String getResString(int ResId){
        return getContext().getResources().getString(ResId);
    }
    public static Drawable getResDrawble(int ResId){
        return getContext().getResources().getDrawable(ResId);
    }
    public static int getResColor(int ResId){
        return getContext().getResources().getColor(ResId);
    }

}
