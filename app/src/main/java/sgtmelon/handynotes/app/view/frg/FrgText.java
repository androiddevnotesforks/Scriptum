package sgtmelon.handynotes.app.view.frg;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.control.MenuNote;
import sgtmelon.handynotes.app.control.MenuNotePreL;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.app.viewModel.VmFrgTextRoll;
import sgtmelon.handynotes.databinding.FrgTextBinding;
import sgtmelon.handynotes.element.dialog.DlgColor;
import sgtmelon.handynotes.element.dialog.common.DlgMessage;
import sgtmelon.handynotes.element.dialog.common.DlgMultiply;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefDlg;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.office.st.StNote;

public class FrgText extends Fragment implements View.OnClickListener, IntfMenu.NoteClick {

    //region Variable
    private static final String TAG = "FrgText";

    private DbRoom db;

    private Context context;
    private ActNote activity;
    private FragmentManager fm;

    private FrgTextBinding binding;
    private View frgView;

    public VmFrgTextRoll vm;
    //endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");

        this.context = context;
        activity = (ActNote) getActivity();
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_text, container, false);
        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        vm = ViewModelProviders.of(this).get(VmFrgTextRoll.class);
        if (vm.isEmpty()) vm.setRepoNote(activity.vm.getRepoNote());

        setupToolbar();
        setupDialog();
        setupEnter();

        onMenuEditClick(activity.vm.getStNote().isEdit());
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind");

        binding.setItemNote(vm.getRepoNote().getItemNote());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    public MenuNotePreL menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_note);

        View indicator = frgView.findViewById(R.id.incToolbarNote_iv_color);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuNote = new MenuNotePreL(context, activity.getWindow(), toolbar, indicator, itemNote.getType());
        } else {
            menuNote = new MenuNote(context, activity.getWindow(), toolbar, indicator, itemNote.getType());
        }

        menuNote.setColor(itemNote.getColor());

        menuNote.setNoteClick(this);
        menuNote.setDeleteClick(activity);

        StNote stNote = activity.vm.getStNote();

        menuNote.setupDrawable();
        menuNote.setDrawable(stNote.isEdit() && !stNote.isCreate(), false);

        menuNote.setupMenu(itemNote.isStatus());

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

        StNote stNote = activity.vm.getStNote();
        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();

        if (!stNote.isCreate() && stNote.isEdit() && !itemNote.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
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
            activity.saveNote.setNeedSave(false);
            activity.finish();
        }
    }

    private DlgMessage dlgConvert;
    private DlgColor dlgColor;
    private DlgMultiply dlgRank;

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

        dlgConvert = (DlgMessage) fm.findFragmentByTag(DefDlg.CONVERT);
        if (dlgConvert == null) dlgConvert = new DlgMessage();

        dlgConvert.setTitle(getString(R.string.dialog_title_convert));
        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
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
        });

        dlgColor = (DlgColor) fm.findFragmentByTag(DefDlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();

        dlgColor.setTitle(getString(R.string.dialog_title_color));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            int check = dlgColor.getCheck();

            RepoNote repoNote = vm.getRepoNote();
            ItemNote itemNote = repoNote.getItemNote();
            itemNote.setColor(check);
            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);

            menuNote.startTint(check);
        });

        dlgRank = (DlgMultiply) fm.findFragmentByTag(DefDlg.RANK);
        if (dlgRank == null) dlgRank = new DlgMultiply();

        db = DbRoom.provideDb(context);
        String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setTitle(getString(R.string.dialog_title_rank));
        dlgRank.setName(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            boolean[] check = dlgRank.getCheck();

            db = DbRoom.provideDb(context);
            Long[] id = db.daoRank().getId();
            db.close();

            List<Long> rankId = new ArrayList<>();
            List<Long> rankPs = new ArrayList<>();

            for (int j = 0; j < id.length; j++) {
                if (check[j]) {
                    rankId.add(id[j]);
                    rankPs.add((long) j);
                }
            }

            RepoNote repoNote = vm.getRepoNote();

            ItemNote itemNote = repoNote.getItemNote();
            itemNote.setRankId(ConvList.fromList(rankId));
            itemNote.setRankPs(ConvList.fromList(rankPs));
            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);
        });
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

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        db = DbRoom.provideDb(context);
        boolean[] check = db.daoRank().getCheck(itemNote.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DefDlg.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        dlgColor.setArguments(itemNote.getColor());
        dlgColor.show(fm, DefDlg.COLOR);

        menuNote.setStartColor(itemNote.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        StNote stNote = activity.vm.getStNote();
        stNote.setEdit(editMode);

        menuNote.setDrawable(editMode && !stNote.isCreate(), !stNote.isCreate());
        menuNote.setMenuGroupVisible(stNote.isBin(), editMode, !stNote.isBin() && !editMode);

        bind(editMode);

        activity.vm.setStNote(stNote);
        activity.saveNote.setSaveHandlerEvent(editMode);
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

        dlgConvert.show(fm, DefDlg.CONVERT);
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        EditText nameEnter = frgView.findViewById(R.id.incToolbarNote_et_name);
        final EditText textEnter = frgView.findViewById(R.id.frgText_et_enter);

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                textEnter.requestFocus();
                return true;
            }
            return false;
        });
    }

}