package sgtmelon.iconanim.office.hdlr;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import sgtmelon.iconanim.office.intf.AnimIntf;

/**
 * Handler для регистрации начала и конца анимации
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class AnimHandler {

    private final AnimatedVectorDrawable animOn, animOff;
    private final int animTime;

    private final Handler animHandler = new Handler();;
    private final Runnable animRunnable;

    private boolean animState;

    private AnimIntf animation;

    public AnimHandler(@NonNull Context context, @NonNull AnimatedVectorDrawable animOn,
                       @NonNull AnimatedVectorDrawable animOff) {
        this.animOn = animOn;
        this.animOff = animOff;

        animTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        animRunnable = () -> {
            if (animOn.isRunning() || animOff.isRunning()) {
                waitAnimationEnd();
            } else {
                animation.setDrawable(animState, false);
            }
        };
    }

    public void waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, animTime);
    }

    public AnimatedVectorDrawable getAnimOn() {
        return animOn;
    }

    public void startAnimOn() {
        animOn.start();
    }

    public AnimatedVectorDrawable getAnimOff() {
        return animOff;
    }

    public void startAnimOff() {
        animOff.start();
    }

    public void setAnimState(boolean animState) {
        this.animState = animState;
    }

    public void setAnimation(AnimIntf animation) {
        this.animation = animation;
    }

}