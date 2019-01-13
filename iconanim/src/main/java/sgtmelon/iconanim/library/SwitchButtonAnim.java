package sgtmelon.iconanim.library;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import sgtmelon.iconanim.office.hdlr.AnimHdlr;

/**
 * Версия {@link SwitchButton} с анимацией иконок при их смене
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class SwitchButtonAnim extends SwitchButton {

    private AnimHdlr animHdlr;

    public SwitchButtonAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchButtonAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupDrawable() {
        super.setupDrawable();

        final AnimatedVectorDrawable srcDisableAnim = (AnimatedVectorDrawable)
                ContextCompat.getDrawable(context, this.srcDisableAnim);

        if (srcDisableAnim != null) {
            srcDisableAnim.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP);
        }

        final AnimatedVectorDrawable srcSelectAnim = (AnimatedVectorDrawable)
                ContextCompat.getDrawable(context, this.srcSelectAnim);

        if (srcSelectAnim != null) {
            srcSelectAnim.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP);
        }

        animHdlr = new AnimHdlr(context, srcSelectAnim, srcDisableAnim);
        animHdlr.setAnimation(this);
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        if (!needAnim) {
            setImageDrawable(select
                    ? drawableSelect
                    : drawableDisable);
        } else {
            animHdlr.setAnimState(select);
            if (select) {
                setImageDrawable(animHdlr.getAnimOn());
                animHdlr.startAnimOn();
            } else {
                setImageDrawable(animHdlr.getAnimOff());
                animHdlr.startAnimOff();
            }
            animHdlr.waitAnimationEnd();
        }
    }

}