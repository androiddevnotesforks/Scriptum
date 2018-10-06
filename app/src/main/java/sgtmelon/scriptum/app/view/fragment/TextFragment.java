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
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.TextViewModel;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.element.ColorDialog;
import sgtmelon.scriptum.element.common.MessageDialog;
import sgtmelon.scriptum.element.common.MultiplyDialog;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.conv.ListConv;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.NoteSt;

public final class TextFragment extends Fragment implements View.OnClickListener, MenuIntf.NoteClick {

    private static final String TAG = TextFragment.class.getSimpleName();

    public MenuControl menuNote;

    @Inject
    public TextViewModel vm;

    @Inject
    FragmentManager fm;
    @Inject
    FragmentTextBinding binding;

    @Inject
    @Named(DialogDef.CONVERT)
    MessageDialog dlgConvert;
    @Inject
    ColorDialog colorDialog;
    @Inject
    @Named(DialogDef.RANK)
    MultiplyDialog dlgRank;

    private NoteActivity activity;
    private Context context;

    private RoomDb db;

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

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this, inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        if (vm.isEmpty()) vm.setNoteModel(activity.vm.getNoteModel());

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupToolbar();
        setupDialog();
        setupEnter();

        onMenuEditClick(activity.vm.getNoteSt().isEdit());

        NoteSt noteSt = activity.vm.getNoteSt();
        noteSt.setFirst(false);
        activity.vm.setNoteSt(noteSt);
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind: keyEdit=" + keyEdit);

        binding.setNoteItem(vm.getNoteModel().getNoteItem());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_note);

        View indicator = frgView.findViewById(R.id.color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuNote = new MenuControl(context, activity.getWindow());
        } else {
            menuNote = new MenuControlAnim(context, activity.getWindow());
        }

        menuNote.setToolbar(toolbar);
        menuNote.setIndicator(indicator);
        menuNote.setType(noteItem.getType());
        menuNote.setColor(noteItem.getColor());

        menuNote.setNoteClick(this);
        menuNote.setDeleteClick(activity);

        NoteSt noteSt = activity.vm.getNoteSt();

        menuNote.setupDrawable();
        menuNote.setDrawable(noteSt.isEdit() && !noteSt.isCreate(), false);

        menuNote.setupMenu(noteItem.isStatus());

        toolbar.setOnMenuItemClickListener(menuNote);
        toolbar.setNavigationOnClickListener(this);
    }

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

        dlgConvert.setTitle(getString(R.string.dialog_title_convert));
        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();
            String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка

            db = RoomDb.provideDb(context);
            List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(context);
            noteItem.setType(TypeDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteModel.setNoteItem(noteItem);
            noteModel.setListRoll(listRoll);

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);
            activity.setupFrg(false);
        });

        colorDialog.setTitle(getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            int check = colorDialog.getCheck();

            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();
            noteItem.setColor(check);
            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);

            menuNote.startTint(check);
        });

        db = RoomDb.provideDb(context);
        String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setTitle(getString(R.string.dialog_title_rank));
        dlgRank.setName(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            boolean[] check = dlgRank.getCheck();

            db = RoomDb.provideDb(context);
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

            NoteModel noteModel = vm.getNoteModel();

            NoteItem noteItem = noteModel.getNoteItem();
            noteItem.setRankId(ListConv.fromList(rankId));
            noteItem.setRankPs(ListConv.fromList(rankPs));
            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);
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

        NoteSt noteSt = activity.vm.getNoteSt();
        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        if (!noteSt.isCreate() && noteSt.isEdit() && !noteItem.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteModel = db.daoNote().get(context, noteItem.getId());
            noteItem = noteModel.getNoteItem();
            db.close();

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);

            onMenuEditClick(false);

            menuNote.startTint(noteItem.getColor());
        } else {
            activity.saveControl.setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();
        if (!noteItem.getText().equals("")) {
            noteItem.setChange(context);

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = RoomDb.provideDb(context);
            NoteSt noteSt = activity.vm.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);
                activity.vm.setNoteSt(noteSt);

                long ntId = db.daoNote().insert(noteItem);
                noteItem.setId(ntId);
            } else {
                db.daoNote().update(noteItem);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());
            db.close();

            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);
            activity.vm.setNoteModel(noteModel);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        db = RoomDb.provideDb(context);
        boolean[] check = db.daoRank().getCheck(noteItem.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DialogDef.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        NoteItem noteItem = vm.getNoteModel().getNoteItem();

        colorDialog.setArguments(noteItem.getColor());
        colorDialog.show(fm, DialogDef.COLOR);

        menuNote.setStartColor(noteItem.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        NoteSt noteSt = activity.vm.getNoteSt();
        noteSt.setEdit(editMode);

        menuNote.setDrawable(editMode && !noteSt.isCreate(), !noteSt.isCreate());
        menuNote.setMenuGroupVisible(noteSt.isBin(), editMode, !noteSt.isBin() && !editMode);

        bind(editMode);

        activity.vm.setNoteSt(noteSt);
        activity.saveControl.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        if (!noteItem.isStatus()) {
            noteItem.setStatus(true);
            noteModel.updateItemStatus(true);
        } else {
            noteItem.setStatus(false);
            noteModel.updateItemStatus(false);
        }

        menuNote.setStatusTitle(noteItem.isStatus());

        db = RoomDb.provideDb(context);
        db.daoNote().update(noteItem.getId(), noteItem.isStatus());
        db.close();

        noteModel.setNoteItem(noteItem);

        vm.setNoteModel(noteModel);
        activity.vm.setNoteModel(noteModel);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DialogDef.CONVERT);
    }

}