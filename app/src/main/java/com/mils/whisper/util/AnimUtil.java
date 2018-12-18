package com.mils.whisper.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class AnimUtil {

    private static AlphaAnimation scaleAnim;
    private static AlphaAnimation scaleAnim1;


    public static void startAnim(View imv) {

        scaleAnim = new AlphaAnimation(1f, 0.2f);
        scaleAnim.setRepeatCount(1000);
        scaleAnim.setDuration(400);

        scaleAnim1 = new AlphaAnimation(0.2f, 1f);
        scaleAnim1.setRepeatCount(1000);
        scaleAnim1.setDuration(400);

        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnim);
        animationSet.addAnimation(scaleAnim1);

        imv.startAnimation(animationSet);

    }

    public static void stopAnim(ImageView imv) {
        imv.clearAnimation();

    }


}
