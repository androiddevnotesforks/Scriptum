package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.SaveControl;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.st.NoteSt;

public final class TextFragment extends NoteFragmentParent {

    private static final String TAG = TextFragment.class.getSimpleName();

    @Inject FragmentTextBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        fragmentComponent.inject(this);

        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        if (vm.isEmpty()) vm.setNoteModel(viewModel.getNoteModel());

        setupToolbar();
        setupDialog();
        setupEnter();

        NoteSt noteSt = viewModel.getNoteSt();

        onMenuEditClick(noteSt.isEdit());

        noteSt.setFirst(false);
        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    protected void bind(boolean keyEdit) {
        Log.i(TAG, "bind: keyEdit=" + keyEdit);

        binding.setNoteItem(vm.getNoteModel().getNoteItem());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();
            String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка

            db = RoomDb.provideDb(context);
            List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(Help.Time.getCurrentTime(context));
            noteItem.setType(TypeDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteModel.setNoteItem(noteItem);
            noteModel.setListRoll(listRoll);

            vm.setNoteModel(noteModel);

            ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            viewModel.setNoteModel(noteModel);
            noteCallback.setViewModel(viewModel);

            noteCallback.setupFragment(false);
        });
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        final EditText nameEnter = frgView.findViewById(R.id.name_enter);
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

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        NoteSt noteSt = viewModel.getNoteSt();

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();

        //Если редактирование и текст в хранилище не пустой
        if (!noteSt.isCreate() && noteSt.isEdit() && !noteItem.getText().equals("")) {
            menuControl.setStartColor(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteModel = db.daoNote().get(context, noteItem.getId());
            noteItem = noteModel.getNoteItem();
            db.close();

            vm.setNoteModel(noteModel);

            viewModel.setNoteModel(noteModel);
            noteCallback.setViewModel(viewModel);

            onMenuEditClick(false);

            menuControl.startTint(noteItem.getColor());
        } else {
            SaveControl saveControl = noteCallback.getSaveControl();
            saveControl.setNeedSave(false);
            noteCallback.setSaveControl(saveControl);

            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();
        if (!noteItem.getText().equals("")) {
            noteItem.setChange(Help.Time.getCurrentTime(context));

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = RoomDb.provideDb(context);

            ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            NoteSt noteSt = viewModel.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);
                viewModel.setNoteSt(noteSt);

                if (!editModeChange) {
                    menuControl.setDrawable(true, true);
                }

                long ntId = db.daoNote().insert(noteItem);
                noteItem.setId(ntId);
            } else {
                db.daoNote().update(noteItem);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());
            db.close();

            noteModel.setNoteItem(noteItem);

            vm.setNoteModel(noteModel);

            viewModel.setNoteModel(noteModel);
            noteCallback.setViewModel(viewModel);
            return true;
        } else return false;
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        NoteSt noteSt = viewModel.getNoteSt();
        noteSt.setEdit(editMode);

        menuControl.setDrawable(editMode && !noteSt.isCreate(),
                !noteSt.isCreate() && !noteSt.isFirst());
        menuControl.setMenuGroupVisible(noteSt.isBin(), editMode, !noteSt.isBin() && !editMode);

        bind(editMode);

        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);

        SaveControl saveControl = noteCallback.getSaveControl();
        saveControl.setSaveHandlerEvent(editMode);
        noteCallback.setSaveControl(saveControl);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

    }

}