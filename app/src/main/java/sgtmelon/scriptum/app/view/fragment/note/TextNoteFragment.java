package sgtmelon.scriptum.app.view.fragment.note;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.NoteViewModel;
import sgtmelon.scriptum.app.vm.fragment.note.TextNoteViewModel;
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.converter.StringConverter;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.state.NoteState;
import sgtmelon.scriptum.office.utils.AppUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class TextNoteFragment extends NoteFragmentParent {

    private static final String TAG = TextNoteFragment.class.getSimpleName();

    private FragmentTextNoteBinding binding;

    private EditText textEnter;

    public static TextNoteFragment getInstance(boolean rankEmpty) {
        Log.i(TAG, "getInstance: rankEmpty=" + rankEmpty);

        final TextNoteFragment textNoteFragment = new TextNoteFragment();
        final Bundle bundle = new Bundle();

        bundle.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
        textNoteFragment.setArguments(bundle);

        return textNoteFragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_text_note, container, false);

        vm = ViewModelProviders.of(this).get(TextNoteViewModel.class);
        vm.setNoteRepo(noteCallback.getViewModel().getNoteRepo());
        vm.setNoteCallback(noteCallback);
        vm.setInputControl(inputControl);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupBinding();
        setupToolbar(view);
        setupDialog();
        setupEnter(view);

        final NoteState noteState = noteCallback.getViewModel().getNoteState();
        onMenuEditClick(noteState.isEdit());
        noteState.setFirst(false);
    }

    @Override
    public void setupBinding() {
        Log.i(TAG, "setupBinding");

        binding.setNoteClick(this);
        binding.setDeleteClick(deleteMenuClick);
        binding.setRankEmpty(rankEmpty);
        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);
    }

    @Override
    public void bindEdit(boolean editMode) {
        Log.i(TAG, "bindEdit: keyEdit=" + editMode);

        binding.setKeyEdit(editMode);
        binding.setNoteItem(vm.getNoteRepo().getNoteItem());

        binding.executePendingBindings();
    }

    @Override
    public void bindInput() {
        Log.i(TAG, "bindInput");

        binding.setUndoAccess(inputControl.isUndoAccess());
        binding.setRedoAccess(inputControl.isRedoAccess());
        binding.setSaveEnabled(!TextUtils.isEmpty(textEnter.getText().toString()));
        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        convertDialog.setMessage(getString(R.string.dialog_text_convert_to_roll));
        convertDialog.setPositiveListener((dialogInterface, i) -> vm.onConvertDialog());
    }

    @Override
    protected void setupEnter(@NonNull View view) {
        Log.i(TAG, "setupEnter");
        super.setupEnter(view);

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i != EditorInfo.IME_ACTION_NEXT) return false;

            textEnter.requestFocus();
            return true;
        });

        textEnter = view.findViewById(R.id.text_note_content_enter);
        textEnter.addTextChangedListener(
                new InputTextWatcher(textEnter, InputDef.text, this, inputControl)
        );
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");

        AppUtils.INSTANCE.hideKeyboard(activity);

        final NoteViewModel viewModel = noteCallback.getViewModel();
        final NoteState noteState = viewModel.getNoteState();

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();

        //Если редактирование и текст в хранилище не пустой
        if (!noteState.isCreate() && noteState.isEdit() && !TextUtils.isEmpty(noteItem.getText())) {
            menuControl.setColorFrom(noteItem.getColor());

            db = RoomDb.provideDb(context);
            noteRepo = db.daoNote().get(context, noteItem.getId());
            noteItem = noteRepo.getNoteItem();
            db.close();

            vm.setNoteRepo(noteRepo);
            viewModel.setNoteRepo(noteRepo);

            onMenuEditClick(false);
            menuControl.startTint(noteItem.getColor());

            inputControl.clear();
            bindInput();
        } else {
            noteCallback.getSaveControl().setNeedSave(false);
            activity.finish();
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean modeChange, boolean showToast) {
        Log.i(TAG, "onMenuSaveClick");

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        if (TextUtils.isEmpty(noteItem.getText())) return false;

        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));

        if (modeChange) {
            AppUtils.INSTANCE.hideKeyboard(activity);
            onMenuEditClick(false);
        }

        db = RoomDb.provideDb(context);

        final NoteViewModel viewModel = noteCallback.getViewModel();
        final NoteState noteState = viewModel.getNoteState();
        if (noteState.isCreate()) {
            noteState.setCreate(false);

            if (!modeChange) {
                menuControl.setDrawable(true, true);
            }

            noteItem.setId(db.daoNote().insert(noteItem));
        } else {
            db.daoNote().update(noteItem);
        }

        db.daoRank().update(noteItem.getId(), noteItem.getRankId());
        db.close();

        viewModel.setNoteRepo(vm.getNoteRepo());

        inputControl.clear();
        bindInput();

        return true;
    }

    @Override
    public void onUndoClick() {
        Log.i(TAG, "onUndoClick");

        final InputItem inputItem = inputControl.undo();

        if (inputItem != null) {
            inputControl.setEnabled(false);

            final CursorItem cursorItem = inputItem.getCursorItem();
            final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConverter stringConverter = new StringConverter();
                    final List<Long> listRankId = stringConverter.fromString(inputItem.getValueFrom());

                    noteItem.setRankId(listRankId);
                    break;
                case InputDef.color:
                    menuControl.setColorFrom(noteItem.getColor());

                    final int colorFrom = Integer.parseInt(inputItem.getValueFrom());
                    noteItem.setColor(colorFrom);

                    menuControl.startTint(colorFrom);
                    break;
                case InputDef.name:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    nameEnter.requestFocus();
                    nameEnter.setText(inputItem.getValueFrom());
                    nameEnter.setSelection(cursorItem.getValueFrom());
                    break;
                case InputDef.text:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    textEnter.requestFocus();
                    textEnter.setText(inputItem.getValueFrom());
                    textEnter.setSelection(cursorItem.getValueFrom());
                    break;
            }

            inputControl.setEnabled(true);
        }

        bindInput();
    }

    @Override
    public void onRedoClick() {
        Log.i(TAG, "onRedoClick");

        final InputItem inputItem = inputControl.redo();

        if (inputItem != null) {
            inputControl.setEnabled(false);

            final NoteItem noteItem = vm.getNoteRepo().getNoteItem();
            final CursorItem cursorItem = inputItem.getCursorItem();

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConverter stringConverter = new StringConverter();
                    final List<Long> listRankId = stringConverter.fromString(inputItem.getValueTo());

                    noteItem.setRankId(listRankId);
                    break;
                case InputDef.color:
                    menuControl.setColorFrom(noteItem.getColor());

                    final int colorTo = Integer.parseInt(inputItem.getValueTo());
                    noteItem.setColor(colorTo);

                    menuControl.startTint(colorTo);
                    break;
                case InputDef.name:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    nameEnter.requestFocus();
                    nameEnter.setText(inputItem.getValueTo());
                    nameEnter.setSelection(cursorItem.getValueTo());
                    break;
                case InputDef.text:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    textEnter.requestFocus();
                    textEnter.setText(inputItem.getValueTo());
                    textEnter.setSelection(cursorItem.getValueTo());
                    break;
            }

            inputControl.setEnabled(true);
        }

        bindInput();
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        inputControl.setEnabled(false);
        inputControl.setChangeEnabled(false);

        final NoteState noteState = noteCallback.getViewModel().getNoteState();
        noteState.setEdit(editMode);

        menuControl.setDrawable(
                editMode && !noteState.isCreate(),
                !noteState.isCreate() && !noteState.isFirst()
        );

        bindEdit(editMode);
        bindInput();

        noteCallback.getSaveControl().setSaveHandlerEvent(editMode);

        inputControl.setEnabled(true);
        inputControl.setChangeEnabled(true);
    }

    // TODO: 10.12.2018 вынести onMenuCheckClick в отдельный интерфейс только для RollNoteFragment

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");
    }

}