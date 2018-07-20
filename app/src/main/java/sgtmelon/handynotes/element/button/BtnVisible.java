package sgtmelon.handynotes.element.button;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class BtnVisible extends AppCompatImageButton {

    private final Context context;

    public BtnVisible(Context context) {
        super(context);

        this.context = context;

        setupDrawable();
    }

    public BtnVisible(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        setupDrawable();
    }

    public BtnVisible(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        setupDrawable();
    }

    private Drawable visibleOn, visibleOff;
    private AnimatedVectorDrawable visibleOnAnim, visibleOffAnim;

    private Handler animHandler;
    private Runnable animRunnable;
    private boolean animRunnableVisible;

    private void setupDrawable() {
        visibleOn = Help.Icon.getDrawable(context, R.drawable.ic_visible_on);
        visibleOff = Help.Icon.getDrawable(context, R.drawable.ic_visible_off, R.color.iconSecond);

        visibleOnAnim = (AnimatedVectorDrawable) Help.Icon.getDrawable(context, R.drawable.ic_visible_on_anim);
        visibleOffAnim = (AnimatedVectorDrawable) Help.Icon.getDrawable(context, R.drawable.ic_visible_off_anim, R.color.iconSecond);

        animHandler = new Handler();
        animRunnable = () -> {
            if (visibleOnAnim.isRunning() || visibleOffAnim.isRunning()) {
                waitAnimationEnd();
            } else {
                if (animRunnableVisible) setImageDrawable(visibleOn);
                else setImageDrawable(visibleOff);
            }
        };
    }

    private void waitAnimationEnd() {
        animHandler.postDelayed(animRunnable, context.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    public void setVisible(boolean visible, boolean needAnim) {
        if (!needAnim) {
            if (visible) setImageDrawable(visibleOn);
            else setImageDrawable(visibleOff);
        } else {
            this.animRunnableVisible = visible;
            if (visible) {
                setImageDrawable(visibleOnAnim);
                visibleOnAnim.start();
            } else {
                setImageDrawable(visibleOffAnim);
                visibleOffAnim.start();
            }
            waitAnimationEnd();
        }
    }
}
