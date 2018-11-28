package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

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
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.databinding.FragmentTextBinding;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.conv.StringConv;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.HelpUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class TextFragment extends NoteFragmentParent {

    private static final String TAG = TextFragment.class.getSimpleName();
    @Inject FragmentTextBinding binding;
    private EditText textEnter;

    public static TextFragment getInstance(boolean rankEmpty) {
        Log.i(TAG, "getInstance: rankEmpty=" + rankEmpty);

        final TextFragment textFragment = new TextFragment();
        final Bundle bundle = new Bundle();

        bundle.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
        textFragment.setArguments(bundle);

        return textFragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        final FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
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

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        if (vm.isEmpty()) {
            vm.setNoteRepo(viewModel.getNoteRepo());
        }

        setupBinding();
        setupToolbar();
        setupDialog();
        setupEnter();

        final NoteSt noteSt = viewModel.getNoteSt();

        onMenuEditClick(noteSt.isEdit());

        inputControl.setEnable(true);

        noteSt.setFirst(false);
        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    public void setupBinding() {
        Log.i(TAG, "setupBinding");

        binding.setNoteClick(this);
        binding.setDeleteClick(deleteMenuClick);
        binding.setRankEmpty(rankEmpty);
    }

    @Override
    public void bindEdit(boolean editMode) {
        Log.i(TAG, "bindEdit: keyEdit=" + editMode);

        binding.setNoteItem(vm.getNoteRepo().getNoteItem());
        binding.setKeyEdit(editMode);

        binding.executePendingBindings();
    }

    @Override
    public void bindInput() {
        Log.i(TAG, "bindInput");

        binding.setUndoAccess(inputControl.isUndoAccess());
        binding.setRedoAccess(inputControl.isRedoAccess());

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка

            db = RoomDb.provideDb(context);
            final List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(TimeUtils.getTime(context));
            noteItem.setType(TypeNoteDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteRepo.setNoteItem(noteItem);
            noteRepo.setListRoll(listRoll);

            vm.setNoteRepo(noteRepo);

            final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            noteCallback.setupFragment(false);
        });
    }

    @Override
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");
        super.setupEnter();

        textEnter = frgView.findViewById(R.id.text_enter);
        textEnter.addTextChangedListener(new TextWatcher() {
            private String textBefore;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textBefore = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String textChanged = charSequence.toString();
                if (!TextUtils.isEmpty(textBefore) && !textChanged.equals(textBefore)) {
                    inputControl.onTextChange(textBefore);
                    bindInput();
                    textBefore = textChanged;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        HelpUtils.hideKeyboard(context, activity.getCurrentFocus());

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();

        //Если редактирование и текст в хранилище не пустой
        if (!noteSt.isCreate() && noteSt.isEdit() && !TextUtils.isEmpty(noteItem.getText())) {
            menuControl.setStartColor(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteRepo = db.daoNote().get(context, noteItem.getId());
            noteItem = noteRepo.getNoteItem();
            db.close();

            vm.setNoteRepo(noteRepo);

            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            onMenuEditClick(false);

            menuControl.startTint(noteItem.getColor());
        } else {
            final SaveControl saveControl = noteCallback.getSaveControl();
            saveControl.setNeedSave(false);
            noteCallback.setSaveControl(saveControl);

            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange, boolean showToast) {
        Log.i(TAG, "onMenuSaveClick");

        inputControl.listAll(); // FIXME: 24.11.2018 remove

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        if (!TextUtils.isEmpty(noteItem.getText())) {
            noteItem.setChange(TimeUtils.getTime(context));

            if (editModeChange) {
                HelpUtils.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);
            }

            db = RoomDb.provideDb(context);

            final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            final NoteSt noteSt = viewModel.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);
                viewModel.setNoteSt(noteSt);

                if (!editModeChange) {
                    menuControl.setDrawable(true, true);
                }

                final long id = db.daoNote().insert(noteItem);
                noteItem.setId(id);
            } else {
                db.daoNote().update(noteItem);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());
            db.close();

            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            inputControl.clear();
            bindInput();

            return true;
        } else {
            if (showToast) {
                Toast.makeText(context, R.string.toast_note_save_warning, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();
        noteSt.setEdit(editMode);

        menuControl.setDrawable(
                editMode && !noteSt.isCreate(),
                !noteSt.isCreate() && !noteSt.isFirst()
        );

        bindEdit(editMode);

        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);

        final SaveControl saveControl = noteCallback.getSaveControl();
        saveControl.setSaveHandlerEvent(editMode);
        noteCallback.setSaveControl(saveControl);
    }

    @Override
    public void onInputClick(boolean undo) {
        Log.i(TAG, "onInputClick: undo=" + undo);

        inputControl.setEnable(false);
        final InputItem inputItem = undo
                ? inputControl.undo()
                : inputControl.redo();

        if (inputItem != null) {
            NoteRepo noteRepo;
            NoteItem noteItem;

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConv stringConv = new StringConv();
                    final List<Long> rankId = stringConv.fromString(inputItem.getValueSecond());

                    noteRepo = vm.getNoteRepo();
                    noteItem = noteRepo.getNoteItem();

                    noteItem.setRankId(rankId);
                    noteRepo.setNoteItem(noteItem);
                    vm.setNoteRepo(noteRepo);
                    break;
                case InputDef.color:
                    final int color = Integer.parseInt(inputItem.getValueSecond());

                    noteRepo = vm.getNoteRepo();
                    noteItem = noteRepo.getNoteItem();

                    menuControl.setStartColor(noteItem.getColor());

                    noteItem.setColor(color);
                    noteRepo.setNoteItem(noteItem);
                    vm.setNoteRepo(noteRepo);

                    menuControl.startTint(color);
                    break;
                case InputDef.name:
                    nameEnter.setText(inputItem.getValueSecond());
                    break;
                case InputDef.text:
                    textEnter.setText(inputItem.getValueSecond());
                    break;
            }
        }

        inputControl.setEnable(true);
        bindInput();
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");
    }

}