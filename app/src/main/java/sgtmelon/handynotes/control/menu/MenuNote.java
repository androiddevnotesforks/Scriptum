package sgtmelon.handynotes.control.menu;

import android.animation.ValueAnimator;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.Help;

public class MenuNote implements Toolbar.OnMenuItemClickListener {

    //region Variables
    private Context context;
    private Window window;
    private Toolbar toolbar;

    private int ntType;

    private Drawable navBackArrow, navCancel;
    //endregion

    public MenuNote(Context context, Window window, Toolbar toolbar, int ntType) {
        this.context = context;
        this.window = window;
        this.toolbar = toolbar;

        this.ntType = ntType;

        navBackArrow = Help.Icon.getDrawable(context, R.drawable.ic_menu_arrow_back);
        navCancel = Help.Icon.getDrawable(context, R.drawable.ic_button_cancel);
    }

    public void setNavigationIcon(boolean keyEdit, boolean keyCreate){
        if (keyEdit && !keyCreate) toolbar.setNavigationIcon(navCancel);
        else toolbar.setNavigationIcon(navBackArrow);
    }

    //Установка цвета
    public void setColor(int ntColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Help.Icon.getColor(context, true, ntColor));
        }

        toolbar.setBackgroundColor(Help.Icon.getColor(context, false, ntColor));
    }

    //Цвета до и после смены цвета
    private int statusBarStartColor, statusBarEndColor, toolbarStartColor, toolbarEndColor;

    //Меняем начальный цвет
    public void setStartColor(int ntColor) {
        statusBarStartColor = Help.Icon.getColor(context, true, ntColor);
        toolbarStartColor = Help.Icon.getColor(context, false, ntColor);
    }

    //Покраска с анимацией
    public void startTint(int ntColor) {
        statusBarEndColor = Help.Icon.getColor(context, true, ntColor);
        toolbarEndColor = Help.Icon.getColor(context, false, ntColor);

        if (statusBarStartColor != statusBarEndColor && toolbarStartColor != toolbarEndColor) {
            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float position = animation.getAnimatedFraction();

                    int blended = Help.Icon.blendColors(statusBarStartColor, statusBarEndColor, position);
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

    public void setupMenu(Menu menu, boolean ntStatus){
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

        setStatusTitle(ntStatus);

        switch (ntType) {
            case DbDesc.typeRoll:
                mItemStatus.setIcon(Help.Icon.getDrawable(context, R.drawable.ic_menu_bind_roll));

                mItemConvert.setTitle(R.string.menu_note_convert_to_text);
                mItemCheck.setVisible(true);
                break;
            case DbDesc.typeText:
                mItemStatus.setIcon(Help.Icon.getDrawable(context, R.drawable.ic_menu_bind_text));

                mItemConvert.setTitle(R.string.menu_note_convert_to_roll);
                mItemCheck.setVisible(false);
                break;
        }

        MenuItem[] mItems = new MenuItem[]{mItemUndo, mItemDeleteF,
                mItemMoreR, mItemStatus, mItemConvert, mItemCheck, mItemDelete,
                mItemMoreE, mItemRank, mItemColor};

        for (MenuItem mItem : mItems) Help.Icon.tintMenuIcon(context, mItem);

        DbRoom db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        if (db.daoRank().getCount() == 0) mItemRank.setVisible(false);

        db.close();
    }

    public void setCheckTitle(boolean allCheck) {
        if (allCheck) mItemCheck.setTitle(context.getString(R.string.menu_note_check_zero));
        else mItemCheck.setTitle(context.getString(R.string.menu_note_check_all));
    }

    public void setStatusTitle(boolean ntStatus){
        if (ntStatus) mItemStatus.setTitle(context.getString(R.string.menu_note_status_unbind));
        else mItemStatus.setTitle(context.getString(R.string.menu_note_status_bind));
    }

    public void setMenuGroupVisible(boolean grDelete, boolean grEdit, boolean grRead){
        menu.setGroupVisible(R.id.menu_actNote_grDelete, grDelete);
        menu.setGroupVisible(R.id.menu_actNote_grEdit, grEdit);
        menu.setGroupVisible(R.id.menu_actNote_grRead, grRead);
    }

    private MenuNoteClick.NoteClick noteClick;
    private MenuNoteClick.RollClick rollClick;
    private MenuNoteClick.DeleteClick deleteClick;

    public void setNoteClick(MenuNoteClick.NoteClick noteClick) {
        this.noteClick = noteClick;
    }

    public void setRollClick(MenuNoteClick.RollClick rollClick) {
        this.rollClick = rollClick;
    }

    public void setDeleteClick(MenuNoteClick.DeleteClick deleteClick) {
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
                if (!noteClick.onMenuSaveClick(true))
                    Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
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

                if (ntType == DbDesc.typeText) {
                    alert.setMessage(context.getString(R.string.dialog_text_convert_to_roll));
                } else alert.setMessage(context.getString(R.string.dialog_roll_convert_to_text));

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
