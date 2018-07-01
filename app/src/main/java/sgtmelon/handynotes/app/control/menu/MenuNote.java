package sgtmelon.handynotes.app.control.menu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.office.Help;

public class MenuNote implements Toolbar.OnMenuItemClickListener {

    //region Variables
    private final Context context;
    private final Window window;
    private final Toolbar toolbar;

    private final int noteType;
    //endregion

    public MenuNote(Context context, Window window, Toolbar toolbar, @DefType int noteType) {
        this.context = context;
        this.window = window;
        this.toolbar = toolbar;

        this.noteType = noteType;
    }

    //Установка цвета
    public void setColor(int ntColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Help.Icon.getColor(context, true, ntColor));
        }
        toolbar.setBackgroundColor(Help.Icon.getColor(context, false, ntColor));
    }

    //Цвета до и после смены цвета
    private int statusStartColor, statusEndColor, toolbarStartColor, toolbarEndColor;

    //Меняем начальный цвет
    public void setStartColor(int noteColor) {
        statusStartColor = Help.Icon.getColor(context, true, noteColor);
        toolbarStartColor = Help.Icon.getColor(context, false, noteColor);
    }

    //Покраска с анимацией
    public void startTint(int noteColor) {
        statusEndColor = Help.Icon.getColor(context, true, noteColor);
        toolbarEndColor = Help.Icon.getColor(context, false, noteColor);

        if (statusStartColor != statusEndColor && toolbarStartColor != toolbarEndColor) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();

                    int blended = Help.Icon.blendColors(statusStartColor, statusEndColor, position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window.setStatusBarColor(blended);
                    }

                    blended = Help.Icon.blendColors(toolbarStartColor, toolbarEndColor, position);
                    ColorDrawable background = new ColorDrawable(blended);
                    toolbar.setBackground(background);
                }
            });

            anim.setDuration(context.getResources().getInteger(android.R.integer.config_shortAnimTime)).start();
        }
    }

    private Menu menu;
    private MenuItem mItemStatus, mItemCheck;

    public void setupMenu(Menu menu, boolean noteStatus) {
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

        setStatusTitle(noteStatus);

        boolean isRoll = noteType == DefType.roll;

        mItemStatus.setIcon(Help.Icon.getDrawable(context, isRoll
                ? R.drawable.ic_menu_bind_roll
                : R.drawable.ic_menu_bind_text));

        mItemConvert.setTitle(isRoll ? R.string.menu_note_convert_to_text : R.string.menu_note_convert_to_roll);
        mItemCheck.setVisible(isRoll);

        MenuItem[] mItems = new MenuItem[]{mItemUndo, mItemDeleteF,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) Help.Icon.tintMenuIcon(context, mItem);

        DbRoom db = DbRoom.provideDb(context);
        if (db.daoRank().getCount() == 0) mItemRank.setVisible(false);
        db.close();
    }

    public void setCheckTitle(boolean allCheck) {
        if (allCheck) mItemCheck.setTitle(context.getString(R.string.menu_note_check_zero));
        else mItemCheck.setTitle(context.getString(R.string.menu_note_check_all));
    }

    public void setStatusTitle(boolean noteStatus) {
        if (noteStatus) mItemStatus.setTitle(context.getString(R.string.menu_note_status_unbind));
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

                if (noteType == DefType.text) {
                    alert.setMessage(context.getString(R.string.dialog_text_convert_to_roll));
                } else {
                    alert.setMessage(context.getString(R.string.dialog_roll_convert_to_text));
                }

                alert.setCancelable(true)
                        .setPositiveButton(context.getString(R.string.dialog_btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                noteClick.onMenuConvertClick();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.dialog_btn_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

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
