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
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.office.HelpUtils;
import sgtmelon.scriptum.office.annot.def.ThemeDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.intf.MenuIntf;

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

    private int type;
    private int valTheme;
    private int statusStartColor, statusEndColor;
    private int toolbarStartColor, toolbarEndColor;

    private MenuItem mItemStatus, mItemCheck;
    private MenuIntf.Note.DeleteMenuClick deleteMenuClick;
    private MenuIntf.Note.NoteMenuClick noteMenuClick;

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

    public final void setType(int type) {
        this.type = type;
    }

    public final void setDeleteMenuClick(MenuIntf.Note.DeleteMenuClick deleteMenuClick) {
        this.deleteMenuClick = deleteMenuClick;
    }

    public final void setNoteMenuClick(MenuIntf.Note.NoteMenuClick noteMenuClick) {
        this.noteMenuClick = noteMenuClick;
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

    public final void setupMenu(boolean status) {
        final MenuItem mItemRestore = menu.findItem(R.id.restore_item);
        final MenuItem mItemRestoreOpen = menu.findItem(R.id.restore_open_item);
        final MenuItem mItemClear = menu.findItem(R.id.clear_item);

        final MenuItem mItemMoreR = menu.findItem(R.id.read_more_item);
        mItemStatus = menu.findItem(R.id.bind_item);
        final MenuItem mItemConvert = menu.findItem(R.id.convert_item);
        mItemCheck = menu.findItem(R.id.check_item);
        final MenuItem mItemDelete = menu.findItem(R.id.delete_item);

        final MenuItem mItemMoreE = menu.findItem(R.id.edit_more_item);
        final MenuItem mItemRank = menu.findItem(R.id.rank_item);
        final MenuItem mItemColor = menu.findItem(R.id.color_item);

        setStatusTitle(status);

        final boolean isRoll = type == TypeNoteDef.roll;

        mItemStatus.setIcon(HelpUtils.Draw.get(context, isRoll
                        ? R.drawable.ic_bind_roll
                        : R.drawable.ic_bind_text,
                R.attr.clIcon));

        mItemConvert.setTitle(isRoll
                ? R.string.menu_note_convert_to_text
                : R.string.menu_note_convert_to_roll);
        mItemCheck.setVisible(isRoll);

        final MenuItem[] mItems = new MenuItem[]{mItemRestore, mItemRestoreOpen, mItemClear,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) HelpUtils.Tint.menuIcon(context, mItem);

        final RoomDb db = RoomDb.provideDb(context);
        mItemRank.setVisible(db.daoRank().getCount() != 0);
        db.close();
    }

    public final void setMenuGroupVisible(boolean grDelete, boolean grEdit, boolean grRead) {
        menu.setGroupVisible(R.id.delete_group, grDelete);
        menu.setGroupVisible(R.id.edit_group, grEdit);
        menu.setGroupVisible(R.id.read_group, grRead);
    }

    public final void setCheckTitle(boolean isAllCheck) {
        mItemCheck.setTitle(isAllCheck
                ? context.getString(R.string.menu_note_check_zero)
                : context.getString(R.string.menu_note_check_all)
        );
    }

    public final void setStatusTitle(boolean status) {
        mItemStatus.setTitle(status
                ? context.getString(R.string.menu_note_status_unbind)
                : context.getString(R.string.menu_note_status_bind)
        );
    }

    @Override
    public final boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restore_item:
                deleteMenuClick.onMenuRestoreClick();
                return true;
            case R.id.restore_open_item:
                deleteMenuClick.onMenuRestoreOpenClick();
                return true;
            case R.id.clear_item:
                deleteMenuClick.onMenuClearClick();
                return true;
            case R.id.save_item:
                if (!noteMenuClick.onMenuSaveClick(true)) {
                    Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.rank_item:
                noteMenuClick.onMenuRankClick();
                return true;
            case R.id.color_item:
                noteMenuClick.onMenuColorClick();
                return true;
            case R.id.edit_item:
                noteMenuClick.onMenuEditClick(true);
                return true;
            case R.id.check_item:
                noteMenuClick.onMenuCheckClick();
                return true;
            case R.id.bind_item:
                noteMenuClick.onMenuBindClick();
                break;
            case R.id.convert_item:
                noteMenuClick.onMenuConvertClick();
                return true;
            case R.id.delete_item:
                deleteMenuClick.onMenuDeleteClick();
                return true;
        }
        return false;
    }

}
