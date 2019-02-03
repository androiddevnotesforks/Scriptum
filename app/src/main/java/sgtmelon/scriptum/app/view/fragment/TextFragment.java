package sgtmelon.scriptum.app.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.CursorItem;
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
import sgtmelon.scriptum.office.intf.InputTextWatcher;
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

        final ActivityNoteViewModel vm = noteCallback.getViewModel();
        if (this.vm.isEmpty()) {
            this.vm.setNoteRepo(vm.getNoteRepo());
        }

        setupBinding();
        setupToolbar();
        setupDialog();
        setupEnter();

        final NoteSt noteSt = vm.getNoteSt();
        onMenuEditClick(noteSt.isEdit());
        noteSt.setFirst(false);
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

        dlgConvert.setMessage(getString(R.string.dialog_text_convert_to_roll));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            db = RoomDb.provideDb(context);

            final String[] textToRoll = noteItem.getText().split("\n");   //Получаем пункты списка
            final List<RollItem> listRoll = db.daoRoll().insert(noteItem.getId(), textToRoll);

            noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
            noteItem.setType(TypeNoteDef.roll);
            noteItem.setText(0, listRoll.size());

            db.daoNote().update(noteItem);
            db.close();

            noteRepo.setListRoll(listRoll);

            noteCallback.getViewModel().setNoteRepo(noteRepo);
            noteCallback.setupFragment(false);
        });
    }

    @Override
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");
        super.setupEnter();

        textEnter = frgView.findViewById(R.id.text_enter);
        textEnter.addTextChangedListener(
                new InputTextWatcher(textEnter, InputDef.text, this, inputControl)
        );

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i != EditorInfo.IME_ACTION_NEXT) return false;

            textEnter.requestFocus();
            return true;
        });
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange, boolean showToast) {
        Log.i(TAG, "onMenuSaveClick");

        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

        if (TextUtils.isEmpty(noteItem.getText())) return false;

        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));

        if (editModeChange) {
            HelpUtils.INSTANCE.hideKeyboard(context, activity.getCurrentFocus());
            onMenuEditClick(false);
        }

        db = RoomDb.provideDb(context);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();
        if (noteSt.isCreate()) {
            noteSt.setCreate(false);

            if (!editModeChange) {
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
                    final StringConv stringConv = new StringConv();
                    final List<Long> listRankId = stringConv.fromString(inputItem.getValueFrom());

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
                    final StringConv stringConv = new StringConv();
                    final List<Long> listRankId = stringConv.fromString(inputItem.getValueTo());

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

        final NoteSt noteSt = noteCallback.getViewModel().getNoteSt();
        noteSt.setEdit(editMode);

        menuControl.setDrawable(
                editMode && !noteSt.isCreate(),
                !noteSt.isCreate() && !noteSt.isFirst()
        );

        bindEdit(editMode);
        bindInput();

        noteCallback.getSaveControl().setSaveHandlerEvent(editMode);

        inputControl.setEnabled(true);
        inputControl.setChangeEnabled(true);
    }

    // TODO: 10.12.2018 вынести onMenuCheckClick в отдельный интерфейс только для RollFragment

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");
    }

}