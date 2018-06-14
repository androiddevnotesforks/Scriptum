package sgtmelon.handynotes.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import android.os.Handler;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.Help;

public class ButtonVisible extends android.support.v7.widget.AppCompatImageButton {

    private Context context;

    public ButtonVisible(Context context) {
        super(context);

        this.context = context;

        setupDrawable();
    }

    public ButtonVisible(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setupDrawable();
    }

    public ButtonVisible(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        setupDrawable();
    }

    private Drawable visibleOn, visibleOff;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private AnimatedVectorDrawable visibleOnAnim, visibleOffAnim;

    private Handler animHandler;
    private Runnable animRunnable;
    private boolean animRunnableVisible;

    private void setupDrawable() {
        visibleOn = Help.Icon.getDrawable(context, R.drawable.ic_button_visible_on);
        visibleOff = Help.Icon.getDrawable(context, R.drawable.ic_button_visible_off, R.color.colorDarkSecond);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            visibleOnAnim = (AnimatedVectorDrawable) Help.Icon.getDrawable(context, R.drawable.ic_button_visible_on_anim);
            visibleOffAnim = (AnimatedVectorDrawable) Help.Icon.getDrawable(context, R.drawable.ic_button_visible_off_anim, R.color.colorDarkSecond);

            animHandler = new Handler();
            animRunnable = new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    if (visibleOnAnim.isRunning() || visibleOffAnim.isRunning()) waitAnimationEnd();
                    else {
                        if (animRunnableVisible) setImageDrawable(visibleOn);
                        else setImageDrawable(visibleOff);
                    }
                }
            };
        }
    }

    private void waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, context.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    //TODO: из-за этих методов не работает на низком API
    public void setVisible(boolean visible, boolean needAnim) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || !needAnim) {
            if (visible) setImageDrawable(visibleOn);
            else setImageDrawable(visibleOff);
        } else {
            this.animRunnableVisible = visible;
            if (visible) {
//                setImageDrawable(visibleOnAnim);
                visibleOnAnim.start();
            } else {
//                setImageDrawable(visibleOffAnim);
                visibleOffAnim.start();
            }
            waitAnimationEnd();
        }
    }
}
