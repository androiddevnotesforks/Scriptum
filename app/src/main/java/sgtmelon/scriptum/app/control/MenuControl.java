package sgtmelon.scriptum.app.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.iconanim.office.intf.AnimIntf;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.utils.HelpUtils;

/**
 * Класс для контроля меню
 * Для версий API < 21
 */
// TODO: 31.10.2018 подумай над builder pattern
public class MenuControl implements Toolbar.OnMenuItemClickListener, AnimIntf {

    protected final Context context;
    protected final ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

    protected Toolbar toolbar;

    Drawable cancelOn, cancelOff;

    private Window window;
    private Menu menu;
    private View indicator;

    private int valTheme;
    private int statusStartColor, statusEndColor;
    private int toolbarStartColor, toolbarEndColor;

    private MenuIntf.Note.MainMenuClick mainMenuClick;

    public MenuControl(Context context, Window window) {
        this.context = context;
        this.window = window;

        valTheme = HelpUtils.Pref.getTheme(context);

        final ValueAnimator.AnimatorUpdateListener updateListener = animator -> {
            final float position = animator.getAnimatedFraction();

            int blended = HelpUtils.Clr.blend(statusStartColor, statusEndColor, position);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && valTheme != ThemeDef.dark) {
                window.setStatusBarColor(blended);
            }

            ColorDrawable background = new ColorDrawable(blended);
            indicator.setBackground(background);

            blended = HelpUtils.Clr.blend(toolbarStartColor, toolbarEndColor, position);
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
        menu = toolbar.getMenu();
    }

    public final void setIndicator(View indicator) {
        this.indicator = indicator;
    }

    public void setMainMenuClick(MenuIntf.Note.MainMenuClick mainMenuClick) {
        this.mainMenuClick = mainMenuClick;
    }

    /**
     * Установка цвета для UI
     *
     * @param color - Начальный цвет
     */
    public final void setColor(int color) {
        if (valTheme != ThemeDef.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(HelpUtils.Clr.get(context, color, true));
            }
            toolbar.setBackgroundColor(HelpUtils.Clr.get(context, color, false));
        }
        indicator.setBackgroundColor(HelpUtils.Clr.get(context, color, true));

        setStartColor(color);
    }

    /**
     * Установка начального цвета
     *
     * @param color - Начальный цвет
     */
    public final void setStartColor(int color) {
        statusStartColor = HelpUtils.Clr.get(context, color, true);
        toolbarStartColor = HelpUtils.Clr.get(context, color, false);
    }

    /**
     * Покраска UI элементов с анимацией
     *
     * @param color - конечный цвет
     */
    public final void startTint(int color) {
        statusEndColor = HelpUtils.Clr.get(context, color, true);
        toolbarEndColor = HelpUtils.Clr.get(context, color, false);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            anim.start();
        }
    }

    @CallSuper
    public void setupDrawable() {
        cancelOn = HelpUtils.Draw.get(context, R.drawable.ic_cancel_on, R.attr.clIcon);
        cancelOff = HelpUtils.Draw.get(context, R.drawable.ic_cancel_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        toolbar.setNavigationIcon(drawableOn
                ? cancelOn
                : cancelOff);
    }

    public final void setMenuVisible(boolean edit, boolean read) {
        menu.setGroupVisible(R.id.edit_group, edit);
        menu.setGroupVisible(R.id.read_group, read);
    }

    @Override
    public final boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                if (!mainMenuClick.onMenuSaveClick(true)) {
                    Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.edit_item:
                mainMenuClick.onMenuEditClick(true);
                return true;
        }
        return false;
    }

}
