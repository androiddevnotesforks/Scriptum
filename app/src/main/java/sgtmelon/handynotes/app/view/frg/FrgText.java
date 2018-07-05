package sgtmelon.handynotes.app.view.frg;

import android.arch.lifecycle.ViewModelProviders;
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
import sgtmelon.handynotes.app.control.menu.MenuNote;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.app.viewModel.VmFrgText;
import sgtmelon.handynotes.databinding.FrgTextBinding;
import sgtmelon.handynotes.element.alert.AlertColor;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annotation.def.db.DefType;
import sgtmelon.handynotes.office.converter.ConvList;
import sgtmelon.handynotes.office.interfaces.IntfMenu;
import sgtmelon.handynotes.office.state.StNote;

public class FrgText extends Fragment implements View.OnClickListener, IntfMenu.NoteClick {

    //region Variable
    private static final String TAG = "FrgText";

    private DbRoom db;

    private FrgTextBinding binding;
    private View frgView;

    private Context context;
    private ActNote activity;

    public VmFrgText vm;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
        frgView = binding.getRoot();

        context = getContext();
        activity = (ActNote) getActivity();

        vm = ViewModelProviders.of(this).get(VmFrgText.class);
        if (vm.isEmpty()) vm.setRepoNote(activity.vm.getRepoNote());

        setupToolbar();
        setupEnter();

        onMenuEditClick(activity.vm.getStNote().isEdit());

        return frgView;
    }

    private void bind(boolean keyEdit, boolean keyCreate) {
        binding.setItemNote(vm.getRepoNote().getItemNote());
        binding.setKeyEdit(keyEdit);
        binding.setKeyCreate(keyCreate);

        binding.executePendingBindings();
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getRepoNote().getItemNote();

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

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        if (activity.vm.getStNote().isEdit() && !itemNote.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DbRoom.provideDb(context);
            repoNote = db.daoNote().get(context, itemNote.getId());
            itemNote = repoNote.getItemNote();
            db.close();

            vm.setRepoNote(repoNote);
            activity.vm.setRepoNote(repoNote);

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.ctrlSave.setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        if (!itemNote.getText().equals("")) {
            itemNote.setChange(context);

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = DbRoom.provideDb(context);
            StNote stNote = activity.vm.getStNote();
            if (stNote.isCreate()) {
                stNote.setCreate(false);
                activity.vm.setStNote(stNote);

                long ntId = db.daoNote().insert(itemNote);
                itemNote.setId(ntId);
            } else {
                db.daoNote().update(itemNote);
            }
            db.daoRank().update(itemNote.getId(), itemNote.getRankId());
            db.close();

            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);
            activity.vm.setRepoNote(repoNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        db = DbRoom.provideDb(context);
        final String[] checkName = db.daoRank().getName();
        final Long[] checkId = db.daoRank().getId();
        final boolean[] checkItem = db.daoRank().getCheck(vm.getRepoNote().getItemNote().getRankId());
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

                        RepoNote repoNote = vm.getRepoNote();

                        ItemNote itemNote = repoNote.getItemNote();
                        itemNote.setRankId(ConvList.fromList(rankId));
                        itemNote.setRankPs(ConvList.fromList(rankPs));
                        repoNote.setItemNote(itemNote);

                        vm.setRepoNote(repoNote);

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

        ItemNote itemNote = vm.getRepoNote().getItemNote();
        final AlertColor alert = new AlertColor(context, itemNote.getColor(), R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_color))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int color = alert.getCheck();

                        RepoNote repoNote = vm.getRepoNote();

                        ItemNote itemNote = repoNote.getItemNote();
                        itemNote.setColor(color);
                        repoNote.setItemNote(itemNote);

                        vm.setRepoNote(repoNote);

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

        StNote stNote = activity.vm.getStNote();
        stNote.setEdit(editMode);

        menuNote.setMenuGroupVisible(stNote.isBin(), editMode, !stNote.isBin() && !editMode);
        bind(editMode, stNote.isCreate());

        activity.vm.setStNote(stNote);
        activity.ctrlSave.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            repoNote.updateItemStatus(true);
        } else {
            itemNote.setStatus(false);
            repoNote.updateItemStatus(false);
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        repoNote.setItemNote(itemNote);

        vm.setRepoNote(repoNote);
        activity.vm.setRepoNote(repoNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        String[] textToRoll = itemNote.getText().split("\n");   //Получаем пункты списка

        db = DbRoom.provideDb(context);
        List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);

        itemNote.setChange(context);
        itemNote.setType(DefType.roll);
        itemNote.setText(0, listRoll.size());

        db.daoNote().update(itemNote);
        db.close();

        repoNote.setItemNote(itemNote);
        repoNote.setListRoll(listRoll);

        vm.setRepoNote(repoNote);
        activity.vm.setRepoNote(repoNote);
        activity.setupFrg(false);
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
