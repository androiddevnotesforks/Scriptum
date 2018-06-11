package sgtmelon.handynotes.ui.frg;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.db.converter.ConverterInt;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.control.menu.MenuNote;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.ui.act.ActNote;
import sgtmelon.handynotes.view.alert.AlertColor;

public class FrgText extends Fragment implements View.OnClickListener, MenuNoteClick.NoteClick {

    private ItemNote itemNote;

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    private DbRoom db;

    private View frgView;
    private Context context;
    private ActNote activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgText", "onCreateView");

        frgView = inflater.inflate(R.layout.frg_n_text, container, false);

        context = getContext();
        activity = (ActNote) getActivity();

        setupToolbar();
        setupEnter();

        onMenuEditClick(activity.stateNote.isEdit());

        return frgView;
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i("FrgText", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_note);

        menuNote = new MenuNote(context, activity.getWindow(), toolbar, itemNote.getType());
        menuNote.setColor(itemNote.getColor());

        menuNote.setNoteClick(this);
        menuNote.setDeleteClick(activity);
        menuNote.setupMenu(toolbar.getMenu(), itemNote.isStatus());

        toolbar.setOnMenuItemClickListener(menuNote);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("FrgText", "onClick");

        if (activity.stateNote.isEdit() && !itemNote.getText().equals("")) { //Если это редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                    .allowMainThreadQueries()
                    .build();

            itemNote = db.daoNote().getNote(itemNote.getId());

            db.close();

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.prefNoteSave.setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean changeEditMode) {
        Log.i("FrgText", "onMenuSaveClick");

        if (!textEnter.getText().toString().equals("")) {
            itemNote.setChange(Help.Time.getCurrentTime(context));
            itemNote.setName(nameEnter.getText().toString());
            itemNote.setText(textEnter.getText().toString());

            if (changeEditMode) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                    .allowMainThreadQueries()
                    .build();

            if (activity.stateNote.isCreate()) {
                activity.stateNote.setCreate(false);    //Теперь у нас заметка уже будет создана

                int ntId = (int) db.daoNote().insertNote(itemNote);
                itemNote.setId(ntId);
            } else {
                db.daoNote().updateNote(itemNote);
            }
            db.daoRank().updateRank(itemNote.getCreate(), itemNote.getRankId());

            db.close();

            activity.setItemNote(itemNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i("FrgText", "onMenuRankClick");

        db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        final String[] checkName = Help.Array.strListToArr(db.daoRank().getRankName());
        final String[] checkId = Help.Array.strListToArr(ConverterInt.fromInteger(db.daoRank().getRankId())); //TODO !!! эт жесть если честно
        final boolean[] checkItem = db.daoRank().getRankCheck(itemNote.getRankId());

        db.close();

        Help.hideKeyboard(context, activity.getCurrentFocus());

        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_rank))
                .setMultiChoiceItems(checkName, checkItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkItem[which] = isChecked;
                    }
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String[] addRank = new String[0];
                        for (int i = 0; i < checkId.length; i++) {
                            if (checkItem[i])
                                addRank = Help.Array.addStrItem(addRank, checkId[i]);
                        }
                        itemNote.setRankId(addRank);
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onMenuColorClick() {
        Log.i("FrgText", "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        final AlertColor alert = new AlertColor(context, itemNote.getColor(), R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_color))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int color = alert.getCheckPosition();

                        itemNote.setColor(color);
                        menuNote.startTint(color);

                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();

        menuNote.setStartColor(itemNote.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i("FrgText", "onMenuEditClick: " + editMode);

        activity.stateNote.setEdit(editMode);

        menuNote.setNavigationIcon(activity.stateNote.isEdit(), activity.stateNote.isCreate());
        menuNote.setMenuGroupVisible(activity.stateNote.isBin(), activity.stateNote.isEdit(), !activity.stateNote.isBin() && !activity.stateNote.isEdit());

        nameEnter.setText(itemNote.getName());      //Установка имени
        nameView.setText(itemNote.getName());

        textEnter.setText(itemNote.getText());      //Устанока текста
        textView.setText(itemNote.getText());

        if (editMode) {
            activity.prefNoteSave.startSaveHandler();

            nameEnter.setVisibility(View.VISIBLE);      //Делаем видимыми редакторы
            nameScrollView.setVisibility(View.GONE);    //Убираем просмотр текста
            textEnter.setVisibility(View.VISIBLE);      //Делаем видимыми редакторы
            textView.setVisibility(View.GONE);          //Убираем просмотр текста
        } else {
            activity.prefNoteSave.stopSaveHandler();

            nameEnter.setVisibility(View.GONE);         //Убираем редакторы
            nameScrollView.setVisibility(View.VISIBLE); //Делаем видимыми просмотр текста
            textEnter.setVisibility(View.GONE);         //Убираем редакторы
            textView.setVisibility(View.VISIBLE);       //Делаем видимыми просмотр текста

            nameEnter.setSelection(itemNote.getName().length());
        }
    }

    @Override
    public void onMenuBindClick() {
        Log.i("FrgText", "onMenuBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.itemStatus.notifyNote();
        } else {
            itemNote.setStatus(false);
            activity.itemStatus.cancelNote();
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();
        db.daoNote().updateNote(itemNote.getId(), itemNote.isStatus());

        db.close();

        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i("FrgText", "onMenuConvertClick");

        String[] textToRoll = itemNote.getText().split("\n");   //Получаем пункты списка

        db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        ItemRollView itemRollView = db.daoRoll().insertRoll(itemNote.getCreate(), textToRoll);

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(DbDesc.typeRoll);
        itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

        db.daoNote().updateNote(itemNote);

        db.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    private EditText nameEnter, textEnter;
    private TextView nameView, textView;
    private HorizontalScrollView nameScrollView;

    private void setupEnter() {
        Log.i("FrgText", "setupEnter");

        nameEnter = frgView.findViewById(R.id.incToolbarNote_et_name);
        nameView = frgView.findViewById(R.id.incToolbarNote_tv_name);
        nameScrollView = frgView.findViewById(R.id.incToolbarNote_hsv_name);

        textEnter = frgView.findViewById(R.id.frgText_et_enter);
        textView = frgView.findViewById(R.id.frgText_tv_text);

        nameEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    textEnter.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }
}
