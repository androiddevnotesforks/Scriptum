package sgtmelon.handynotes.ui.frg;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
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
import sgtmelon.handynotes.databinding.FrgNTextBinding;
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

    private FrgNTextBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgText", "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_n_text, container, false);
        frgView = binding.getRoot();

        context = getContext();
        activity = (ActNote) getActivity();

        setupToolbar();
        setupEnter();

        onMenuEditClick(activity.stateNote.isEdit());

        return frgView;
    }

    private void bind(boolean keyEdit) {
        binding.setItemNote(itemNote);
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
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

            itemNote = db.daoNote().get(itemNote.getId());

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

        if (!itemNote.getText().equals("")) {
            itemNote.setChange(Help.Time.getCurrentTime(context));

            if (changeEditMode) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = Room.databaseBuilder(context, DbRoom.class, "HandyNotes")
                    .allowMainThreadQueries()
                    .build();

            if (activity.stateNote.isCreate()) {
                activity.stateNote.setCreate(false);

                int ntId = (int) db.daoNote().insert(itemNote);
                itemNote.setId(ntId);
            } else {
                db.daoNote().update(itemNote);
            }
            db.daoRank().update(itemNote.getCreate(), itemNote.getRankId());

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

        final String[] checkName = Help.Array.strListToArr(db.daoRank().getName());
        final String[] checkId = Help.Array.strListToArr(ConverterInt.fromInteger(db.daoRank().getId())); //TODO !!! эт жесть если честно
        final boolean[] checkItem = db.daoRank().getCheck(itemNote.getRankId());

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
                        String[] rankId = new String[0];
                        String[] rankPs = new String[0];
                        for (int i = 0; i < checkId.length; i++) {
                            if (checkItem[i]) {
                                rankId = Help.Array.addStrItem(rankId, checkId[i]);
                                rankPs = Help.Array.addStrItem(rankPs, Integer.toString(i));
                            }
                        }
                        itemNote.setRankId(rankId);
                        itemNote.setRankPs(rankPs);

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

    //TODO: чё там
    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i("FrgText", "onMenuEditClick: " + editMode);

        activity.stateNote.setEdit(editMode);

        menuNote.setNavigationIcon(activity.stateNote.isEdit(), activity.stateNote.isCreate());
        menuNote.setMenuGroupVisible(activity.stateNote.isBin(), activity.stateNote.isEdit(), !activity.stateNote.isBin() && !activity.stateNote.isEdit());

        bind(editMode);

        if (editMode) {
            activity.prefNoteSave.startSaveHandler();
//            nameEnter.setSelection(itemNote.getName().length());
        } else {
            activity.prefNoteSave.stopSaveHandler();
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
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());

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

        ItemRollView itemRollView = db.daoRoll().insert(itemNote.getCreate(), textToRoll);

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(DbDesc.typeRoll);
        itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

        db.daoNote().update(itemNote);

        db.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    private void setupEnter() {
        Log.i("FrgText", "setupEnter");

        EditText nameEnter = frgView.findViewById(R.id.incToolbarNote_et_name);
        final EditText textEnter = frgView.findViewById(R.id.frgText_et_enter);

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
