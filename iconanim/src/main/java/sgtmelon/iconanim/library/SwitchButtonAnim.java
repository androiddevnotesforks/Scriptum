package sgtmelon.iconanim.library;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import sgtmelon.iconanim.office.hdlr.AnimHdlr;

/**
 * Версия {@link SwitchButton} с анимацией иконок при их смене
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class SwitchButtonAnim extends SwitchButton {

    @Nullable private AnimHdlr animHdlr;

    @Nullable private AnimatedVectorDrawable drawableDisableAnim;
    @Nullable private AnimatedVectorDrawable drawableSelectAnim;

    public SwitchButtonAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchButtonAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupDrawable() {
        super.setupDrawable();

        if (srcDisableAnim != SRC_NULL) {
            final Drawable srcDisableAnimCast = ContextCompat.getDrawable(context, srcDisableAnim);
            if (!(srcDisableAnimCast instanceof AnimatedVectorDrawable)) return;

            drawableDisableAnim = (AnimatedVectorDrawable) srcDisableAnimCast;
            drawableDisableAnim.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP);
        }

        if (srcSelectAnim != SRC_NULL) {
            final Drawable srcSelectAnimCast = ContextCompat.getDrawable(context, srcSelectAnim);
            if (!(srcSelectAnimCast instanceof AnimatedVectorDrawable)) return;

            drawableSelectAnim = (AnimatedVectorDrawable) srcSelectAnimCast;
            drawableSelectAnim.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP);
        }

        if (drawableSelectAnim != null && drawableDisableAnim != null) {
            animHdlr = new AnimHdlr(context, drawableSelectAnim, drawableDisableAnim);
            animHdlr.setAnimation(this);
        }
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        if (!needAnim) {
            setImageDrawable(select
                    ? drawableSelect
                    : drawableDisable);
        } else if (animHdlr != null){
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