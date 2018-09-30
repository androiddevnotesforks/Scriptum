package sgtmelon.scriptum.element.button;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.RequiresApi;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.hdlr.HdlrAnim;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class BtnVisible extends BtnVisiblePreL {

    private HdlrAnim hdlrAnim;

    public BtnVisible(Context context) {
        super(context);
    }

    public BtnVisible(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BtnVisible(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setupDrawable() {
        super.setupDrawable();

        AnimatedVectorDrawable visibleOnAnim = (AnimatedVectorDrawable) Help.Draw.get(context, R.drawable.ic_visible_on_anim, R.attr.clAccent);
        AnimatedVectorDrawable visibleOffAnim = (AnimatedVectorDrawable) Help.Draw.get(context, R.drawable.ic_visible_off_anim, R.attr.clIcon);

        hdlrAnim = new HdlrAnim(context, visibleOnAnim, visibleOffAnim);
        hdlrAnim.setAnimation(this);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (!needAnim) {
            if (drawableOn) setImageDrawable(visibleOn);
            else setImageDrawable(visibleOff);
        } else {
            hdlrAnim.setAnimState(drawableOn);
            if (drawableOn) {
                setImageDrawable(hdlrAnim.getAnimOn());
                hdlrAnim.startAnimOn();
            } else {
                setImageDrawable(hdlrAnim.getAnimOff());
                hdlrAnim.startAnimOff();
            }
            hdlrAnim.waitAnimationEnd();
        }
    }

}
