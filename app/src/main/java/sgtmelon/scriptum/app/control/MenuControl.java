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

    private final boolean statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M;

    protected Toolbar toolbar;
    Drawable cancelOn, cancelOff;

    private Window window;
    private View indicator;

    private int valTheme;
    private int statusColorFrom, statusColorTo;
    private int toolbarColorFrom, toolbarColorTo;

    public MenuControl(Context context) {
        this.context = context;

        valTheme = PrefUtils.getInstance(context).getTheme();

        final ValueAnimator.AnimatorUpdateListener updateListener = animator -> {
            final float position = animator.getAnimatedFraction();

            int blended = ColorUtils.blend(statusColorFrom, statusColorTo, position);
            if (valTheme != ThemeDef.dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(blended);
            }

            ColorDrawable background = new ColorDrawable(blended);
            indicator.setBackground(background);

            blended = ColorUtils.blend(toolbarColorFrom, toolbarColorTo, position);
            background = new ColorDrawable(blended);

            if (valTheme != ThemeDef.dark) {
                toolbar.setBackground(background);
            }
        };

        anim.addUpdateListener(updateListener);
        anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    public final void setWindow(Window window) {
        this.window = window;
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
                window.setStatusBarColor(ColorUtils.get(context, color, statusOnDark));
            }
            toolbar.setBackgroundColor(ColorUtils.get(context, color, false));
        }
        indicator.setBackgroundColor(ColorUtils.get(context, color, true));

        setColorFrom(color);
    }

    /**
     * Установка начального цвета
     *
     * @param color - Начальный цвет
     */
    public final void setColorFrom(@ColorDef int color) {
        statusColorFrom = ColorUtils.get(context, color, statusOnDark);
        toolbarColorFrom = ColorUtils.get(context, color, false);
    }

    /**
     * Покраска UI элементов с анимацией
     *
     * @param color - Конечный цвет
     */
    public final void startTint(@ColorDef int color) {
        statusColorTo = ColorUtils.get(context, color, statusOnDark);
        toolbarColorTo = ColorUtils.get(context, color, false);

        if (statusColorFrom != statusColorTo) {
            anim.start();
        }
    }

    @CallSuper
    public void setupDrawable() {
        cancelOn = ColorUtils.getDrawable(context, R.drawable.ic_cancel_enter, R.attr.clContent);
        cancelOff = ColorUtils.getDrawable(context, R.drawable.ic_cancel_exit, R.attr.clContent);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        toolbar.setNavigationIcon(drawableOn
                ? cancelOn
                : cancelOff);
    }

}