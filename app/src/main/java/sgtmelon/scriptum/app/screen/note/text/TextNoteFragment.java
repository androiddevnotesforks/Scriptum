package sgtmelon.scriptum.app.screen.note.text;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import sgtmelon.safedialog.MessageDialog;
import sgtmelon.safedialog.MultiplyDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.room.RoomDb;
import sgtmelon.scriptum.app.screen.note.NoteCallback;
import sgtmelon.scriptum.databinding.FragmentTextNoteBinding;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.key.NoteType;
import sgtmelon.scriptum.office.converter.StringConverter;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.utils.AppUtils;
import sgtmelon.scriptum.widget.color.ColorDialog;

public final class TextNoteFragment extends Fragment implements
        View.OnClickListener, BindIntf {

    // TODO: 17.12.2018 сделать долгое нажатие undo/redo
    // TODO: 11.02.2019 Если Id не существует то завершать активити

    private final InputControl inputControl = new InputControl();

    private FragmentTextNoteBinding binding;

    private EditText textEnter;

    private Context context;
    private Activity activity;
    private NoteCallback noteCallback;

    private EditText nameEnter;

    private TextNoteViewModel vm;
    private FragmentManager fm;
    private RoomDb db;

    private boolean rankEmpty;

    private MenuControl menuControl;

    private ColorDialog colorDialog;
    private MultiplyDialog rankDialog;
    private MessageDialog convertDialog;


    public static TextNoteFragment getInstance(@NonNull Long id) {
        final TextNoteFragment textNoteFragment = new TextNoteFragment();
        final Bundle bundle = new Bundle();

        bundle.putLong(IntentDef.NOTE_ID, id);
        textNoteFragment.setArguments(bundle);

        return textNoteFragment;
    }

    @Override
    public void onAttach(Context context) { // TODO as ?:
        super.onAttach(context);

        this.context = context;
        activity = getActivity();

        if (context instanceof NoteCallback) {
            noteCallback = (NoteCallback) context;
        } else {
            throw new ClassCastException(NoteCallback.class.getSimpleName() +
                    " interface not installed");
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fm = getFragmentManager();

        binding = AppUtils.INSTANCE.inflateBinding(
                inflater, R.layout.fragment_text_note, container, false
        );

        vm = ViewModelProviders.of(this).get(TextNoteViewModel.class);
//        vm.setNoteRepo(noteCallback.getViewModel().getNoteRepo());
        vm.setNoteCallback(noteCallback);
        vm.setInputControl(inputControl);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        convertDialog = DialogFactory.INSTANCE.getConvertDialog(context, fm, NoteType.TEXT);
        colorDialog = DialogFactory.INSTANCE.getColorDialog(fm);
        rankDialog = DialogFactory.INSTANCE.getRankDialog(context, fm);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            rankEmpty = bundle.getBoolean(IntentDef.RANK_EMPTY);
        } else if (savedInstanceState != null) {
            rankEmpty = savedInstanceState.getBoolean(IntentDef.RANK_EMPTY);
        }

        setupBinding();
        setupToolbar(view);
        setupDialog();
        setupEnter(view);
//        final NoteState noteState = noteCallback.getViewModel().getNoteState();
//        onMenuEditClick(noteState.isEdit());
//        noteState.setFirst(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
    }

    private void setupBinding() {
        binding.setMenuClick(vm);
        binding.setRankEmpty(rankEmpty);
        binding.setRankSelect(true);
//        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);
    }

    private void bindEdit(boolean editMode) {
        binding.setKeyEdit(editMode);
        binding.setNoteItem(vm.getNoteRepo().getNoteItem());

        binding.executePendingBindings();
    }

    @Override
    public void bindInput() {
        binding.setUndoAccess(inputControl.isUndoAccess());
        binding.setRedoAccess(inputControl.isRedoAccess());
        binding.setSaveEnabled(!TextUtils.isEmpty(textEnter.getText().toString()));
        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);

        binding.executePendingBindings();
    }

    private void setupToolbar(@NonNull View view) {
        final Toolbar toolbar = view.findViewById(R.id.toolbar_note_container);
        final View indicator = view.findViewById(R.id.toolbar_note_color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuControl = new MenuControl(context, activity.getWindow(), toolbar, indicator);
        } else {
            menuControl = new MenuControlAnim(context, activity.getWindow(), toolbar, indicator);
        }

        menuControl.setColor(ColorDef.red);
//        menuControl.setColor(vm.getNoteColor());

//        final NoteState noteState = noteCallback.getViewModel().getNoteState();
//        menuControl.setDrawable(noteState.isEdit() && !noteState.isCreate(), false);

        toolbar.setNavigationOnClickListener(this);
    }

    private void setupDialog() {
        colorDialog.setTitle(activity.getString(R.string.dialog_title_color));
        colorDialog.setPositiveListener((dialogInterface, i) -> {
            final int check = colorDialog.getCheck();

            vm.onColorDialog(check);
            bindInput();

            menuControl.startTint(check);
        });

        rankDialog.setName(vm.getRankDialogName());
        rankDialog.setPositiveListener((dialogInterface, i) -> {
            vm.onRankDialog(rankDialog.getCheck());
            bindInput();
        });

        convertDialog.setPositiveListener((dialogInterface, i) -> vm.onConvertDialog());
    }

    private void setupEnter(@NonNull View view) {
        nameEnter = view.findViewById(R.id.toolbar_note_enter);
        nameEnter.addTextChangedListener(
                new InputTextWatcher(nameEnter, InputDef.name, this, inputControl)
        );

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

    public void startTintToolbar(@ColorDef int colorFrom, @ColorDef int colorTo) {
        menuControl.setColorFrom(colorFrom);
        menuControl.startTint(colorTo);
    }

    public TextNoteViewModel getViewModel() {
        return vm;
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View v) {
//        AppUtils.INSTANCE.hideKeyboard(activity);
//
//        final NoteViewModel viewModel = noteCallback.getViewModel();
//        final NoteState noteState = viewModel.getNoteState();
//
//        NoteRepo noteRepo = vm.getNoteRepo();
//        NoteItem noteItem = noteRepo.getNoteItem();
//
//        //Если редактирование и текст в хранилище не пустой
//        if (!noteState.isCreate() && noteState.isEdit() && !TextUtils.isEmpty(noteItem.getText())) {
//            menuControl.setColorFrom(noteItem.getColor());
//
//            db = RoomDb.provideDb(context);
//            noteRepo = db.daoNote().get(context, noteItem.getId());
//            noteItem = noteRepo.getNoteItem();
//            db.close();
//
//            vm.setNoteRepo(noteRepo);
//            viewModel.setNoteRepo(noteRepo);
//
//            onMenuEditClick(false);
//            menuControl.startTint(noteItem.getColor());
//
//            inputControl.clear();
//            bindInput();
//        } else {
//            noteCallback.getSaveControl().setNeedSave(false);
//            activity.finish();
//        }
    }

    public boolean onMenuSaveClick(boolean modeChange) {
//        final NoteItem noteItem = vm.getNoteRepo().getNoteItem();
//
//        if (TextUtils.isEmpty(noteItem.getText())) return false;
//
//        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
//
//        if (modeChange) {
//            AppUtils.INSTANCE.hideKeyboard(activity);
//            onMenuEditClick(false);
//        }
//
//        db = RoomDb.provideDb(context);
//
//        final NoteViewModel viewModel = noteCallback.getViewModel();
//        final NoteState noteState = viewModel.getNoteState();
//        if (noteState.isCreate()) {
//            noteState.setCreate(false);
//
//            if (!modeChange) {
//                menuControl.setDrawable(true, true);
//            }
//
//            noteItem.setId(db.daoNote().insert(noteItem));
//        } else {
//            db.daoNote().update(noteItem);
//        }
//
//        db.daoRank().update(noteItem.getId(), noteItem.getRankId());
//        db.close();
//
//        viewModel.setNoteRepo(vm.getNoteRepo());
//
//        inputControl.clear();
//        bindInput();

        return true;
    }

    public void onUndoClick() {
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

    public void onRedoClick() {
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

    public void onMenuEditClick(boolean editMode) {
//        inputControl.setEnabled(false);
//        inputControl.setChangeEnabled(false);
//
//        final NoteState noteState = noteCallback.getViewModel().getNoteState();
//        noteState.setEdit(editMode);
//
//        menuControl.setDrawable(
//                editMode && !noteState.isCreate(),
//                !noteState.isCreate() && !noteState.isFirst()
//        );
//
//        bindEdit(editMode);
//        bindInput();
//
//        noteCallback.getSaveControl().setSaveHandlerEvent(editMode);
//
//        inputControl.setEnabled(true);
//        inputControl.setChangeEnabled(true);
    }

    public void onMenuRankClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        rankDialog.setArguments(vm.onMenuRank());
        rankDialog.show(fm, DialogDef.RANK);
    }

    public void onMenuColorClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        final int color = vm.getNoteColor();

        colorDialog.setArguments(color);
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setColorFrom(color);
    }

    public void onMenuBindClick() {
        vm.onMenuBind();
        bindEdit(false); // TODO save
    }

    public void onMenuConvertClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);
        convertDialog.show(fm, DialogDef.CONVERT);
    }

    // TODO: 10.12.2018 вынести onMenuCheckClick в отдельный интерфейс только для RollNoteFragment

    public void onMenuCheckClick() {

    }

}