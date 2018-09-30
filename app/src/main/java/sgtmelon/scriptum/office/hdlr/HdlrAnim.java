package sgtmelon.scriptum.office.hdlr;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import sgtmelon.scriptum.office.intf.IntfItem;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class HdlrAnim {

    private final Context context;

    private final AnimatedVectorDrawable animOn, animOff;

    private final Handler animHandler;
    private final Runnable animRunnable;

    private boolean animState;

    private IntfItem.Animation animation;

    public HdlrAnim(Context context, AnimatedVectorDrawable animOn, AnimatedVectorDrawable animOff) {
        this.context = context;

        this.animOn = animOn;
        this.animOff = animOff;

        animHandler = new Handler();
        animRunnable = () -> {
            if (animOn.isRunning() || animOff.isRunning()) {
                waitAnimationEnd();
            } else {
                animation.setDrawable(animState, false);
            }
        };
    }

    public void waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, context.getResources().getInteger(android.R.integer.config_shortAnimTime));
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

    public void setAnimation(IntfItem.Animation animation) {
        this.animation = animation;
    }

}
