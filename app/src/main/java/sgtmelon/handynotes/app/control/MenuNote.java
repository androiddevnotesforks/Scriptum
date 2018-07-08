package sgtmelon.handynotes.app.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class MenuNote implements Toolbar.OnMenuItemClickListener {

    //region Variables
    private final Context context;
    private final Window window;
    private final Toolbar toolbar;

    private final int type;
    //endregion

    public MenuNote(Context context, Window window, Toolbar toolbar, @DefType int type) {
        this.context = context;
        this.window = window;
        this.toolbar = toolbar;

        this.type = type;
    }

    //Установка цвета
    public void setColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Help.Icon.getColor(context, true, color));
        }
        toolbar.setBackgroundColor(Help.Icon.getColor(context, false, color));
    }

    //Цвета до и после смены цвета
    private int statusStartColor, statusEndColor, toolbarStartColor, toolbarEndColor;

    //Меняем начальный цвет
    public void setStartColor(int color) {
        statusStartColor = Help.Icon.getColor(context, true, color);
        toolbarStartColor = Help.Icon.getColor(context, false, color);
    }

    //Покраска с анимацией
    public void startTint(int color) {
        statusEndColor = Help.Icon.getColor(context, true, color);
        toolbarEndColor = Help.Icon.getColor(context, false, color);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

            anim.addUpdateListener(animation -> {
                float position = animation.getAnimatedFraction();

                int blended = Help.Icon.blendColors(statusStartColor, statusEndColor, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(blended);
                }

                blended = Help.Icon.blendColors(toolbarStartColor, toolbarEndColor, position);
                ColorDrawable background = new ColorDrawable(blended);
                toolbar.setBackground(background);
            });

            anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime)).start();
        }
    }

    private Menu menu;
    private MenuItem mItemStatus, mItemCheck;

    public void setupMenu(Menu menu, boolean status) {
        this.menu = menu;

        MenuItem mItemUndo = menu.findItem(R.id.menu_actNote_restore);
        MenuItem mItemDeleteF = menu.findItem(R.id.menu_actNote_clear);

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

        mItemStatus.setIcon(Help.Icon.getDrawable(context, isRoll
                ? R.drawable.ic_bind_roll
                : R.drawable.ic_bind_text));

        mItemConvert.setTitle(isRoll ? R.string.menu_note_convert_to_text : R.string.menu_note_convert_to_roll);
        mItemCheck.setVisible(isRoll);

        MenuItem[] mItems = new MenuItem[]{mItemUndo, mItemDeleteF,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) Help.Icon.tintMenuIcon(context, mItem);

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
            case R.id.menu_actNote_clear:
                deleteClick.onMenuDeleteForeverClick();
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
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);
                alert.setTitle(context.getString(R.string.dialog_title_convert));

                if (type == DefType.text) {
                    alert.setMessage(context.getString(R.string.dialog_text_convert_to_roll));
                } else {
                    alert.setMessage(context.getString(R.string.dialog_roll_convert_to_text));
                }

                alert.setCancelable(true)
                        .setPositiveButton(context.getString(R.string.dialog_btn_yes), (dialog, id) -> {
                            noteClick.onMenuConvertClick();
                            dialog.cancel();
                        })
                        .setNegativeButton(context.getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel());

                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            case R.id.menu_actNote_delete:
                deleteClick.onMenuDeleteClick();
                return true;
        }
        return false;
    }

}
