package sgtmelon.scriptum.app.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.CallSuper;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.iconanim.office.intf.AnimIntf;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.utils.ColorUtils;
import sgtmelon.scriptum.office.utils.PrefUtils;

/**
 * Класс для контроля меню
 * Для версий API < 21
 */
public class MenuControl implements AnimIntf {

    protected final Context context;
    protected final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

    protected Toolbar toolbar;

    Drawable cancelOn, cancelOff;

    private Window window;

    private View indicator;

    private int valTheme;
    private int statusStartColor, statusEndColor;
    private int toolbarStartColor, toolbarEndColor;

    public MenuControl(Context context, Window window) {
        this.context = context;
        this.window = window;

        valTheme = PrefUtils.getTheme(context);

        final ValueAnimator.AnimatorUpdateListener updateListener = animator -> {
            final float position = animator.getAnimatedFraction();

            int blended = ColorUtils.blend(statusStartColor, statusEndColor, position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && valTheme != ThemeDef.dark) {
                window.setStatusBarColor(blended);
            }

            // TODO: 05.12.2018 не красится в тёмной теме
            ColorDrawable background = new ColorDrawable(blended);
            indicator.setBackground(background);

            blended = ColorUtils.blend(toolbarStartColor, toolbarEndColor, position);
            background = new ColorDrawable(blended);

            if (valTheme != ThemeDef.dark) {
                toolbar.setBackground(background);
            }
        };

        anim.addUpdateListener(updateListener);
        anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    public final void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public final void setIndicator(View indicator) {
        this.indicator = indicator;
    }

    /**
     * Установка цвета для UI
     *
     * @param color - Начальный цвет
     */
    public final void setColor(@ColorDef int color) {
        if (valTheme != ThemeDef.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ColorUtils.get(context, color, true));
            }
            toolbar.setBackgroundColor(ColorUtils.get(context, color, false));
        }
        indicator.setBackgroundColor(ColorUtils.get(context, color, true));

        setStartColor(color);
    }

    /**
     * Установка начального цвета
     *
     * @param color - Начальный цвет
     */
    public final void setStartColor(@ColorDef int color) {
        statusStartColor = ColorUtils.get(context, color, true);
        toolbarStartColor = ColorUtils.get(context, color, false);
    }

    /**
     * Покраска UI элементов с анимацией
     *
     * @param color - конечный цвет
     */
    public final void startTint(@ColorDef int color) {
        statusEndColor = ColorUtils.get(context, color, true);
        toolbarEndColor = ColorUtils.get(context, color, false);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            anim.start();
        }
    }

    @CallSuper
    public void setupDrawable() {
        cancelOn = ColorUtils.getDrawable(context, R.drawable.ic_cancel_on, R.attr.clIcon);
        cancelOff = ColorUtils.getDrawable(context, R.drawable.ic_cancel_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        toolbar.setNavigationIcon(drawableOn
                ? cancelOn
                : cancelOff);
    }

}
