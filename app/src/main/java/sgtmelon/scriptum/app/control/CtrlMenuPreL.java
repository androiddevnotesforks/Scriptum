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

import androidx.appcompat.widget.Toolbar;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefTheme;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.intf.IntfMenu;

public class CtrlMenuPreL implements Toolbar.OnMenuItemClickListener, IntfItem.Animation {

    protected final Context context;
    private final Window window;

    protected Toolbar toolbar;
    protected Drawable cancelOn, cancelOff;

    private Menu menu;
    private View indicator;

    private int type;
    private int valTheme;

    private int statusStartColor, statusEndColor;   //Цвета до и после смены цвета
    private int toolbarStartColor, toolbarEndColor;

    private MenuItem mItemStatus, mItemCheck;

    private IntfMenu.NoteClick noteClick;
    private IntfMenu.RollClick rollClick;
    private IntfMenu.DeleteClick deleteClick;

    public CtrlMenuPreL(Context context, Window window) {
        this.context = context;
        this.window = window;

        valTheme = Help.Pref.getTheme(context);
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        menu = toolbar.getMenu();
    }

    public void setIndicator(View indicator) {
        this.indicator = indicator;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNoteClick(IntfMenu.NoteClick noteClick) {
        this.noteClick = noteClick;
    }

    public void setRollClick(IntfMenu.RollClick rollClick) {
        this.rollClick = rollClick;
    }

    public void setDeleteClick(IntfMenu.DeleteClick deleteClick) {
        this.deleteClick = deleteClick;
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

    public void setupDrawable() {
        cancelOn = Help.Draw.get(context, R.drawable.ic_cancel_on, R.attr.clIcon);
        cancelOff = Help.Draw.get(context, R.drawable.ic_cancel_off, R.attr.clIcon);
    }

    @Override
    public void setDrawable(boolean drawableOn, boolean needAnim) {
        if (drawableOn) toolbar.setNavigationIcon(cancelOn);
        else toolbar.setNavigationIcon(cancelOff);
    }

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

    public void setMenuGroupVisible(boolean grDelete, boolean grEdit, boolean grRead) {
        menu.setGroupVisible(R.id.menu_actNote_grDelete, grDelete);
        menu.setGroupVisible(R.id.menu_actNote_grEdit, grEdit);
        menu.setGroupVisible(R.id.menu_actNote_grRead, grRead);
    }

    public void setCheckTitle(boolean isAllCheck) {
        mItemCheck.setTitle(isAllCheck ? context.getString(R.string.menu_note_check_zero) : context.getString(R.string.menu_note_check_all));
    }

    public void setStatusTitle(boolean status) {
        mItemStatus.setTitle(status ? context.getString(R.string.menu_note_status_unbind) : context.getString(R.string.menu_note_status_bind));
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
