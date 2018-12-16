package sgtmelon.scriptum.app.control;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;

import androidx.annotation.RequiresApi;
import sgtmelon.iconanim.office.hdlr.AnimHdlr;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.utils.ColorUtils;

/**
 * Класс для контроля меню с использованием анимации
 * Для версий API >= 21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public final class MenuControlAnim extends MenuControl {

    private AnimHdlr animHdlr;

    public MenuControlAnim(Context context) {
        super(context);
    }

    @Override
    public void setupDrawable() {
        super.setupDrawable();

        final AnimatedVectorDrawable cancelOnAnim = (AnimatedVectorDrawable) ColorUtils.getDrawable(
                context, R.drawable.ic_cancel_on_anim, R.attr.clIcon
        );
        final AnimatedVectorDrawable cancelOffAnim = (AnimatedVectorDrawable) ColorUtils.getDrawable(
                context, R.drawable.ic_cancel_off_anim, R.attr.clIcon
        );

        animHdlr = new AnimHdlr(context, cancelOnAnim, cancelOffAnim);
        animHdlr.setAnimation(this);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (!needAnim) {
            if (drawableOn) toolbar.setNavigationIcon(cancelOn);
            else toolbar.setNavigationIcon(cancelOff);
        } else {
            animHdlr.setAnimState(drawableOn);
            if (drawableOn) {
                toolbar.setNavigationIcon(animHdlr.getAnimOn());
                animHdlr.startAnimOn();
            } else {
                toolbar.setNavigationIcon(animHdlr.getAnimOff());
                animHdlr.startAnimOff();
            }
            animHdlr.waitAnimationEnd();
        }
    }

}
