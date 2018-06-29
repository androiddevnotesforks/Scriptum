package sgtmelon.handynotes.app.ui.frg;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.databinding.FrgTextBinding;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.def.data.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.app.control.menu.MenuNote;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.app.model.item.ItemRollView;
import sgtmelon.handynotes.app.ui.act.ActNote;
import sgtmelon.handynotes.view.alert.AlertColor;

public class FrgText extends Fragment implements View.OnClickListener, IntfMenu.NoteClick {

    //region Variable
    final String TAG = "FrgText";

    private DataRoom db;

    private View frgView;
    private Context context;
    private ActNote activity;

    private FrgTextBinding binding;

    private ItemNote itemNote;
    //endregion

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
        frgView = binding.getRoot();

        context = getContext();
        activity = (ActNote) getActivity();

        setupToolbar();
        setupEnter();

        onMenuEditClick(activity.stateNote.isEdit());

        return frgView;
    }

    private void bind(boolean keyEdit, boolean keyCreate) {
        binding.setItemNote(itemNote);
        binding.setKeyEdit(keyEdit);
        binding.setKeyCreate(keyCreate);

        binding.executePendingBindings();
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

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
        Log.i(TAG, "onClick");

        if (activity.stateNote.isEdit() && !itemNote.getText().equals("")) { //Если это редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DataRoom.provideDb(context);
            itemNote = db.daoNote().get(itemNote.getId());
            db.close();

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.controlSave.setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        if (!itemNote.getText().equals("")) {
            itemNote.setChange(Help.Time.getCurrentTime(context));

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = DataRoom.provideDb(context);
            if (activity.stateNote.isCreate()) {
                activity.stateNote.setCreate(false);

                long ntId = db.daoNote().insert(itemNote);
                itemNote.setId(ntId);
            } else {
                db.daoNote().update(itemNote);
            }
            db.daoRank().update(itemNote.getId(), itemNote.getRankId());
            db.close();

            activity.setItemNote(itemNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        db = DataRoom.provideDb(context);
        final String[] checkName = db.daoRank().getName();
        final Long[] checkId = db.daoRank().getId();
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
                        List<Long> rankId = new ArrayList<>();
                        List<Long> rankPs = new ArrayList<>();

                        for (int i = 0; i < checkId.length; i++) {
                            if (checkItem[i]) {
                                rankId.add(checkId[i]);
                                rankPs.add((long) i);
                            }
                        }

                        itemNote.setRankId(ConvList.fromList(rankId));
                        itemNote.setRankPs(ConvList.fromList(rankPs));

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
        Log.i(TAG, "onMenuColorClick");

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
        Log.i(TAG, "onMenuEditClick: " + editMode);

        activity.stateNote.setEdit(editMode);

        menuNote.setMenuGroupVisible(activity.stateNote.isBin(), editMode, !activity.stateNote.isBin() && !editMode);
        bind(editMode, activity.stateNote.isCreate());

        activity.controlSave.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.itemStatus.notifyNote();
        } else {
            itemNote.setStatus(false);
            activity.itemStatus.cancelNote();
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DataRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        String[] textToRoll = itemNote.getText().split("\n");   //Получаем пункты списка

        db = DataRoom.provideDb(context);

        ItemRollView itemRollView = db.daoRoll().insert(itemNote.getId(), textToRoll);

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(DefType.roll);
        itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

        db.daoNote().update(itemNote);

        db.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

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
