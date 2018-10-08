package sgtmelon.iconanim.library;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import sgtmelon.iconanim.office.hdlr.AnimHdlr;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class SwitchButtonAnim extends SwitchButton {

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

        AnimatedVectorDrawable srcDisableAnim = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, this.srcDisableAnim);
        if (srcDisableAnim != null) {
            srcDisableAnim.setColorFilter(srcDisableColor, PorterDuff.Mode.SRC_ATOP);
        }

        AnimatedVectorDrawable srcSelectAnim = (AnimatedVectorDrawable) ContextCompat.getDrawable(context, this.srcSelectAnim);
        if (srcSelectAnim != null) {
            srcSelectAnim.setColorFilter(srcSelectColor, PorterDuff.Mode.SRC_ATOP);
        }

        animHdlr = new AnimHdlr(context, srcSelectAnim, srcDisableAnim);
        animHdlr.setAnimation(this);
    }

    @Override
    public void setDrawable(boolean select, boolean needAnim) {
        if (!needAnim) {
            if (select) setImageDrawable(drawableSelect);
            else setImageDrawable(drawableDisable);
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
