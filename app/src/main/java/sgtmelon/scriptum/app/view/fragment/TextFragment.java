package sgtmelon.scriptum.app.view.fragment;

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

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.ControlMenu;
import sgtmelon.scriptum.app.control.ControlMenuPreL;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.TextViewModel;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.element.DlgColor;
import sgtmelon.scriptum.element.common.DlgMessage;
import sgtmelon.scriptum.element.common.DlgMultiply;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.conv.ConvList;
import sgtmelon.scriptum.office.intf.IntfMenu;
import sgtmelon.scriptum.office.st.StNote;

public final class TextFragment extends Fragment implements View.OnClickListener, IntfMenu.NoteClick {

    private static final String TAG = TextFragment.class.getSimpleName();

    public ControlMenuPreL menuNote;

    @Inject
    public TextViewModel vm;

    @Inject
    FragmentManager fm;
    @Inject
    FragmentTextBinding binding;

    @Inject
    @Named(DefDlg.CONVERT)
    DlgMessage dlgConvert;
    @Inject
    DlgColor dlgColor;
    @Inject
    @Named(DefDlg.RANK)
    DlgMultiply dlgRank;

    private NoteActivity activity;
    private Context context;

    private DbRoom db;

    private View frgView;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
        activity = (NoteActivity) getActivity(); // TODO: 02.10.2018 установи интерфейс общения
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComponentFragment componentFragment = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        componentFragment.inject(this);

        frgView = binding.getRoot();

        if (vm.isEmpty()) vm.setModelNote(activity.vm.getModelNote());

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupToolbar();
        setupDialog();
        setupEnter();

        onMenuEditClick(activity.vm.getStNote().isEdit());

        StNote stNote = activity.vm.getStNote();
        stNote.setFirst(false);
        activity.vm.setStNote(stNote);
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind: keyEdit=" + keyEdit);

        binding.setItemNote(vm.getModelNote().getItemNote());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getModelNote().getItemNote();

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_activity_note);

        View indicator = frgView.findViewById(R.id.color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuNote = new ControlMenuPreL(context, activity.getWindow());
        } else {
            menuNote = new ControlMenu(context, activity.getWindow());
        }

        menuNote.setToolbar(toolbar);
        menuNote.setIndicator(indicator);
        menuNote.setType(itemNote.getType());
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

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

        dlgConvert.setTitle(getString(R.string.dialog_title_convert));
        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            ModelNote modelNote = vm.getModelNote();
            ItemNote itemNote = modelNote.getItemNote();
            String[] textToRoll = itemNote.getText().split("\n");   //Получаем пункты списка

            db = DbRoom.provideDb(context);
            List<ItemRoll> listRoll = db.daoRoll().insert(itemNote.getId(), textToRoll);

            itemNote.setChange(context);
            itemNote.setType(DefType.roll);
            itemNote.setText(0, listRoll.size());

            db.daoNote().update(itemNote);
            db.close();

            modelNote.setItemNote(itemNote);
            modelNote.setListRoll(listRoll);

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);
            activity.setupFrg(false);
        });

        dlgColor.setTitle(getString(R.string.dialog_title_color));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            int check = dlgColor.getCheck();

            ModelNote modelNote = vm.getModelNote();
            ItemNote itemNote = modelNote.getItemNote();
            itemNote.setColor(check);
            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);

            menuNote.startTint(check);
        });

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

            ModelNote modelNote = vm.getModelNote();

            ItemNote itemNote = modelNote.getItemNote();
            itemNote.setRankId(ConvList.fromList(rankId));
            itemNote.setRankPs(ConvList.fromList(rankPs));
            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);
        });
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        EditText nameEnter = frgView.findViewById(R.id.name_enter);
        final EditText textEnter = frgView.findViewById(R.id.text_enter);

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                textEnter.requestFocus();
                return true;
            }
            return false;
        });
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        StNote stNote = activity.vm.getStNote();
        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();

        if (!stNote.isCreate() && stNote.isEdit() && !itemNote.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DbRoom.provideDb(context);
            modelNote = db.daoNote().get(context, itemNote.getId());
            itemNote = modelNote.getItemNote();
            db.close();

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);

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

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();
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

            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getModelNote().getItemNote();

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

        ItemNote itemNote = vm.getModelNote().getItemNote();

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
        activity.controlSave.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            modelNote.updateItemStatus(true);
        } else {
            itemNote.setStatus(false);
            modelNote.updateItemStatus(false);
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        modelNote.setItemNote(itemNote);

        vm.setModelNote(modelNote);
        activity.vm.setModelNote(modelNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DefDlg.CONVERT);
    }

}