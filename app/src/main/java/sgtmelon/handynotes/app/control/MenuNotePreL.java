package sgtmelon.handynotes.app.control;

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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefTheme;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class MenuNotePreL implements Toolbar.OnMenuItemClickListener, IntfItem.Animation {

    //region Variables
    protected final Context context;
    private final Window window;
    protected final Toolbar toolbar;
    private final View indicator;

    private final int type;
    private final int valTheme;
    //endregion

    public MenuNotePreL(Context context, Window window, Toolbar toolbar, View indicator, @DefType int type) {
        this.context = context;
        this.window = window;
        this.toolbar = toolbar;
        this.indicator = indicator;

        this.type = type;

        menu = toolbar.getMenu();
        valTheme = Help.Pref.getTheme(context);
    }

    //Установка цвета
    public void setColor(int color) {
        if (valTheme != DefTheme.dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Help.Clr.get(context, color, true));
            }
            toolbar.setBackgroundColor(Help.Clr.get(context, color, false));
        }
        indicator.setBackgroundColor(Help.Clr.get(context, color, true));

        setStartColor(color);
    }

    //Цвета до и после смены цвета
    private int statusStartColor, statusEndColor, toolbarStartColor, toolbarEndColor;

    //Меняем начальный цвет
    public void setStartColor(int color) {
        statusStartColor = Help.Clr.get(context, color, true);
        toolbarStartColor = Help.Clr.get(context, color, false);
    }

    //Покраска с анимацией
    public void startTint(int color) {
        statusEndColor = Help.Clr.get(context, color, true);
        toolbarEndColor = Help.Clr.get(context, color, false);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

            anim.addUpdateListener(animation -> {
                float position = animation.getAnimatedFraction();

                int blended = Help.Clr.blend(statusStartColor, statusEndColor, position);
                if (valTheme != DefTheme.dark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(blended);
                }

                ColorDrawable background = new ColorDrawable(blended);
                indicator.setBackground(background);

                blended = Help.Clr.blend(toolbarStartColor, toolbarEndColor, position);
                background = new ColorDrawable(blended);
                if (valTheme != DefTheme.dark) toolbar.setBackground(background);

            });

            anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime)).start();
        }
    }

    Drawable cancelOn, cancelOff;

    public void setupDrawable() {
        cancelOn = Help.Draw.get(context, R.drawable.ic_cancel_on, R.attr.clIcon);
        cancelOff = Help.Draw.get(context, R.drawable.ic_cancel_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, @Nullable boolean needAnim) {
        if (drawableOn) toolbar.setNavigationIcon(cancelOn);
        else toolbar.setNavigationIcon(cancelOff);
    }

    private Menu menu;
    private MenuItem mItemStatus, mItemCheck;

    public void setupMenu(boolean status) {
        MenuItem mItemRestore = menu.findItem(R.id.menu_actNote_restore);
        MenuItem mItemRestoreOpen = menu.findItem(R.id.menu_actNote_restore_open);
        MenuItem mItemClear = menu.findItem(R.id.menu_actNote_clear);

        MenuItem mItemMoreR = menu.findItem(R.id.menu_actNote_readMore);
        mItemStatus = menu.findItem(R.id.menu_actNote_bind);
        MenuItem mItemConvert = menu.findItem(R.id.menu_actNote_convert);
        mItemCheck = menu.findItem(R.id.menu_actNote_check);
        MenuItem mItemDelete = menu.findItem(R.id.menu_actNote_delete);

        MenuItem mItemMoreE = menu.findItem(R.id.menu_actNote_editMore);
        MenuItem mItemRank = menu.findItem(R.id.menu_actNote_rank);
        MenuItem mItemColor = menu.findItem(R.id.menu_actNote_color);

        setStatusTitle(status);

        boolean isRoll = type == DefType.roll;

        mItemStatus.setIcon(Help.Draw.get(context, isRoll
                        ? R.drawable.ic_bind_roll
                        : R.drawable.ic_bind_text,
                R.attr.clIcon));

        mItemConvert.setTitle(isRoll ? R.string.menu_note_convert_to_text : R.string.menu_note_convert_to_roll);
        mItemCheck.setVisible(isRoll);

        MenuItem[] mItems = new MenuItem[]{mItemRestore, mItemRestoreOpen, mItemClear,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) Help.Tint.menuIcon(context, mItem);

        DbRoom db = DbRoom.provideDb(context);
        mItemRank.setVisible(db.daoRank().getCount() != 0);
        db.close();
    }

    public void setCheckTitle(boolean isAllCheck) {
        if (isAllCheck) mItemCheck.setTitle(context.getString(R.string.menu_note_check_zero));
        else mItemCheck.setTitle(context.getString(R.string.menu_note_check_all));
    }

    public void setStatusTitle(boolean status) {
        if (status) mItemStatus.setTitle(context.getString(R.string.menu_note_status_unbind));
        else mItemStatus.setTitle(context.getString(R.string.menu_note_status_bind));
    }

    public void setMenuGroupVisible(boolean grDelete, boolean grEdit, boolean grRead) {
        menu.setGroupVisible(R.id.menu_actNote_grDelete, grDelete);
        menu.setGroupVisible(R.id.menu_actNote_grEdit, grEdit);
        menu.setGroupVisible(R.id.menu_actNote_grRead, grRead);
    }

    private IntfMenu.NoteClick noteClick;
    private IntfMenu.RollClick rollClick;
    private IntfMenu.DeleteClick deleteClick;

    public void setNoteClick(IntfMenu.NoteClick noteClick) {
        this.noteClick = noteClick;
    }

    public void setRollClick(IntfMenu.RollClick rollClick) {
        this.rollClick = rollClick;
    }

    public void setDeleteClick(IntfMenu.DeleteClick deleteClick) {
        this.deleteClick = deleteClick;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_actNote_restore:
                deleteClick.onMenuRestoreClick();
                return true;
            case R.id.menu_actNote_restore_open:
                deleteClick.onMenuRestoreOpenClick();
                return true;
            case R.id.menu_actNote_clear:
                deleteClick.onMenuClearClick();
                return true;
            case R.id.menu_actNote_save:
                if (!noteClick.onMenuSaveClick(true)) {
                    Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.menu_actNote_rank:
                noteClick.onMenuRankClick();
                return true;
            case R.id.menu_actNote_color:
                noteClick.onMenuColorClick();
                return true;
            case R.id.menu_actNote_edit:
                noteClick.onMenuEditClick(true);
                return true;
            case R.id.menu_actNote_check:
                rollClick.onMenuCheckClick();
                return true;
            case R.id.menu_actNote_bind:
                noteClick.onMenuBindClick();
                break;
            case R.id.menu_actNote_convert:
                noteClick.onMenuConvertClick();
                return true;
            case R.id.menu_actNote_delete:
                deleteClick.onMenuDeleteClick();
                return true;
        }
        return false;
    }

}
