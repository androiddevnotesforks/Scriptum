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
import sgtmelon.handynotes.database.DataBaseRoom;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.service.menu.MenuNote;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.ui.act.ActNote;
import sgtmelon.handynotes.view.alert.AlertColor;

public class FrgText extends Fragment implements View.OnClickListener, MenuNoteClick.NoteClick {

    private ItemNote itemNote;

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    private NoteDB noteDB;

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

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
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

            noteDB = new NoteDB(context);
            itemNote = noteDB.getNote(itemNote.getId());
            noteDB.close();

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

            DataBaseRoom db = Room.databaseBuilder(context, DataBaseRoom.class, "itemTest")
                    .allowMainThreadQueries()
                    .build();

            noteDB = new NoteDB(context);
            if (activity.stateNote.isCreate()) {
                activity.stateNote.setCreate(false);    //Теперь у нас заметка уже будет создана

                int ntId = noteDB.insertNote(itemNote);
                itemNote.setId(ntId);
            } else {
                noteDB.updateNote(itemNote);        //Обновляем
            }
            noteDB.updateRank(itemNote.getCreate(), itemNote.getRankId());
            noteDB.close();

            activity.setItemNote(itemNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i("FrgText", "onMenuRankClick");

        noteDB = new NoteDB(context);
        final String[] checkName = noteDB.getRankColumn(NoteDB.RK_NM);
        final String[] checkId = noteDB.getRankColumn(NoteDB.RK_ID);
        final boolean[] checkItem = noteDB.getRankCheck(itemNote.getRankId());
        noteDB.close();

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

        noteDB = new NoteDB(context);
        noteDB.updateNote(itemNote.getId(), itemNote.isStatus());
        noteDB.close();

        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i("FrgText", "onMenuConvertClick");

        String[] textToRoll = itemNote.getText().split("\n");   //Получаем пункты списка

        noteDB = new NoteDB(context);
        ItemRollView itemRollView = noteDB.insertRoll(itemNote.getCreate(), textToRoll);      //Записываем пункты

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(NoteDB.typeRoll);
        itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

        noteDB.updateNoteType(itemNote);
        noteDB.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    private EditText nameEnter, textEnter;
    private TextView nameView, textView;
    private HorizontalScrollView nameScrollView;

    private void setupEnter() {
        Log.i("FrgText", "setupEnter");

        nameEnter = frgView.findViewById(R.id.editText_toolbarNote_name);
        nameView = frgView.findViewById(R.id.tView_toolbarNote_name);
        nameScrollView = frgView.findViewById(R.id.hScrollView_toolbarNote_name);

        textEnter = frgView.findViewById(R.id.editText_frgText_enter);
        textView = frgView.findViewById(R.id.tView_frgText_text);

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
