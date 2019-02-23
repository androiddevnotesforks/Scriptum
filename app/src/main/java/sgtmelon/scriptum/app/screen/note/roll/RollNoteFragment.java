package sgtmelon.scriptum.app.screen.note.roll;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.safedialog.MessageDialog;
import sgtmelon.safedialog.MultiplyDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RollAdapter;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.control.MenuControl;
import sgtmelon.scriptum.app.control.MenuControlAnim;
import sgtmelon.scriptum.app.control.touch.RollTouchControl;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.screen.note.NoteCallback;
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.key.NoteType;
import sgtmelon.scriptum.office.converter.StringConverter;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputTextWatcher;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.state.CheckState;
import sgtmelon.scriptum.office.utils.AppUtils;
import sgtmelon.scriptum.office.utils.HelpUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;
import sgtmelon.scriptum.widget.color.ColorDialog;

public final class RollNoteFragment extends Fragment implements
        ItemIntf.ClickListener,
        ItemIntf.RollWatcher,
        View.OnClickListener,
        BindIntf,
        MenuIntf.Note.NoteMenuClick {

    // TODO: 11.02.2019 Если Id не существует то завершать активити

    private final CheckState checkState = new CheckState();
    private final InputControl inputControl = new InputControl();
    private FragmentRollNoteBinding binding;
    private RollAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private EditText rollEnter;

    // TODO: 17.12.2018 сделать долгое нажатие undo/redo
    private Context context;
    private Activity activity;
    private NoteCallback noteCallback;

    private MessageDialog convertDialog;

    private EditText nameEnter;

    private RollNoteViewModel vm;
    private FragmentManager fm;
    private RoomDb db;

    private boolean rankEmpty;

    private MenuControl menuControl;
    private MenuIntf.Note.DeleteMenuClick deleteMenuClick;

    private ColorDialog colorDialog;
    private MultiplyDialog rankDialog;


    @NonNull
    public static RollNoteFragment getInstance(@NonNull Long id) {
        final RollNoteFragment rollNoteFragment = new RollNoteFragment();
        final Bundle bundle = new Bundle();

        bundle.putLong(IntentDef.NOTE_ID, id);
        rollNoteFragment.setArguments(bundle);

        return rollNoteFragment;
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


    @Override
    public void onResume() {
        super.onResume();

        bindEnter(rollEnter.getText().toString());
        updateAdapter();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        fm = getFragmentManager();

        binding = AppUtils.INSTANCE.inflateBinding(
                inflater, R.layout.fragment_roll_note, container, false
        );

        vm = ViewModelProviders.of(this).get(RollNoteViewModel.class);
//        vm.setNoteRepo(noteCallback.getViewModel().getNoteRepo());
        vm.setNoteCallback(noteCallback);
        vm.setInputControl(inputControl);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        convertDialog = DialogFactory.INSTANCE.getConvertDialog(context, fm, NoteType.ROLL);
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
        setupRecycler(view);
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

        bindEnter(rollEnter.getText().toString());
    }

    private void bindEnter(String enterText) {
        binding.setEnterEmpty(TextUtils.isEmpty(enterText));
        binding.executePendingBindings();
    }

    @Override
    public void bindInput() {
        binding.setUndoAccess(inputControl.isUndoAccess());
        binding.setRedoAccess(inputControl.isRedoAccess());
        binding.setSaveEnabled(vm.getNoteRepo().getListRoll().size() != 0);
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

//        menuControl.setColor(vm.getNoteColor());
        menuControl.setColor(ColorDef.red);

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

    private void setupRecycler(@NonNull View view) {
        recyclerView = view.findViewById(R.id.roll_note_recycler);

        final SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        final RollTouchControl touchCallback = new RollTouchControl(
                vm, noteCallback, inputControl, this
        );

        adapter = new RollAdapter(
                context, this, touchCallback, this, inputControl, this
        );

        touchCallback.setAdapter(adapter);

//        adapter.setNoteState(noteCallback.getViewModel().getNoteState());

        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupEnter(@NonNull View view) {
        nameEnter = view.findViewById(R.id.toolbar_note_enter);
        nameEnter.addTextChangedListener(
                new InputTextWatcher(nameEnter, InputDef.name, this, inputControl)
        );
        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i != EditorInfo.IME_ACTION_NEXT) return false;

            rollEnter.requestFocus();
            return true;
        });

        rollEnter = view.findViewById(R.id.roll_note_enter);
        final ImageButton rollAdd = view.findViewById(R.id.roll_note_add_button);

        rollEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bindEnter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rollAdd.setOnClickListener(v -> scrollToInsert(true));
        rollAdd.setOnLongClickListener(v -> {
            scrollToInsert(false);
            return true;
        });
    }

    public void startTintToolbar(@ColorDef int colorFrom, @ColorDef int colorTo) {
        menuControl.setColorFrom(colorFrom);
        menuControl.startTint(colorTo);
    }

    public RollNoteViewModel getViewModel() {
        return vm;
    }

    public void updateAdapter() {
//        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

//        checkState.setAll(listRoll);

//        adapter.setList(listRoll);
//        adapter.notifyItemRangeChanged(0, listRoll.size());
//
//        adapter.setCheckToggle(false);
    }

    private void scrollToInsert(boolean scrollDown) {
        final String text = rollEnter.getText().toString();

        if (TextUtils.isEmpty(text)) return;

        rollEnter.setText("");

        //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
        final int p = scrollDown
                ? adapter.getItemCount()
                : 0;

        final RollItem rollItem = new RollItem();
        rollItem.setNoteId(vm.getNoteRepo().getNoteItem().getId());
        rollItem.setText(text);

        inputControl.onRollAdd(p, rollItem.toString());
        bindInput();

        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();
        listRoll.add(p, rollItem);

        final int visiblePs = scrollDown
                ? layoutManager.findLastVisibleItemPosition() + 1
                : layoutManager.findFirstVisibleItemPosition();

        if (visiblePs == p) {                          //Если видимая позиция равна позиции куда добавили пункт
            recyclerView.scrollToPosition(p);          //Прокручиваем до края, незаметно
            adapter.notifyItemInserted(p, listRoll);   //Добавляем элемент с анимацией
        } else {
            recyclerView.smoothScrollToPosition(p);    //Медленно прокручиваем, через весь список
            adapter.notifyDataSetChanged(listRoll);    //Добавляем элемент без анимации
        }
    }

    /**
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(@NonNull View view, int p) {
        final NoteRepo noteRepo = vm.getNoteRepo();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        final RollItem rollItem = listRoll.get(p);
        rollItem.setCheck(!rollItem.isCheck());

        adapter.setListItem(p, rollItem);

        final NoteItem noteItem = noteRepo.getNoteItem();
        final int check = HelpUtils.Note.INSTANCE.getCheck(listRoll);
        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
        noteItem.setText(check, listRoll.size());

        if (checkState.setAll(check, listRoll.size())) {
            binding.setNoteItem(noteItem);
            binding.executePendingBindings();
        }

//        noteCallback.getViewModel().setNoteRepo(noteRepo);

        assert rollItem.getId() != null : "Roll from database with @NonNull id";

        db = RoomDb.provideDb(context);
        db.daoRoll().update(rollItem.getId(), rollItem.isCheck());
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void afterRollChanged(int p, @NonNull String text) {
        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

        if (TextUtils.isEmpty(text)) {
            listRoll.remove(p);

            adapter.notifyItemRemoved(p, listRoll);
        } else {
            final RollItem rollItem = listRoll.get(p);
            rollItem.setText(text);

            adapter.setListItem(p, rollItem);
        }
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
//            adapter.setList(noteRepo.getListRoll());
//
//            onMenuEditClick(false);
//            menuControl.startTint(noteItem.getColor());
//
//            inputControl.clear();
//            bindInput();
//        } else {
//            noteCallback.getSaveControl().setNeedSave(false);
//            activity.finish(); //Иначе завершаем активность
//        }
    }

    @Override
    public boolean onMenuSaveClick(boolean modeChange) {
//        final NoteRepo noteRepo = vm.getNoteRepo();
//        final NoteItem noteItem = noteRepo.getNoteItem();
//        final List<RollItem> listRoll = noteRepo.getListRoll();
//
//        if (listRoll.size() == 0) return false;
//
//        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
//        noteItem.setText(HelpUtils.Note.INSTANCE.getCheck(listRoll), listRoll.size());
//
//        //Переход в режим просмотра
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
//            final long id = db.daoNote().insert(noteItem);
//            noteItem.setId(id);
//
//            //Запись в пунктов в БД
//            for (int i = 0; i < listRoll.size(); i++) {
//                final RollItem rollItem = listRoll.get(i);
//                rollItem.setNoteId(id);
//                rollItem.setPosition(i);
//                rollItem.setId(db.daoRoll().insert(rollItem));
//            }
//
//            adapter.setList(listRoll);
//        } else {
//            db.daoNote().update(noteItem);
//
//            for (int i = 0; i < listRoll.size(); i++) {
//                final RollItem rollItem = listRoll.get(i);
//
//                rollItem.setPosition(i);
//                if (rollItem.getId() == null) {
//                    rollItem.setId(db.daoRoll().insert(rollItem));
//                } else {
//                    db.daoRoll().update(rollItem.getId(), i, rollItem.getText());
//                }
//            }
//
//            adapter.setList(listRoll);
//
//            final List<Long> listRollId = new ArrayList<>();
//            for (RollItem rollItem : listRoll) {
//                listRollId.add(rollItem.getId());
//            }
//
//            db.daoRoll().delete(noteItem.getId(), listRollId);
//        }
//
//        db.daoRank().update(noteItem.getId(), noteItem.getRankId());
//        db.close();
//
//        viewModel.setNoteRepo(noteRepo);
//
//        inputControl.clear();
//        bindInput();

        return true;
    }

    @Override
    public void onUndoClick() {
        final InputItem inputItem = inputControl.undo();

        if (inputItem != null) {
            inputControl.setEnabled(false);

            final CursorItem cursorItem = inputItem.getCursorItem();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            RollItem rollItem;

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConverter stringConverter = new StringConverter();
                    final List<Long> rankId = stringConverter.fromString(inputItem.getValueFrom());

                    noteItem.setRankId(rankId);
                    break;
                case InputDef.color:
                    menuControl.setColorFrom(noteItem.getColor());

                    final int color = Integer.parseInt(inputItem.getValueFrom());
                    noteItem.setColor(color);

                    menuControl.startTint(color);
                    break;
                case InputDef.name:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    nameEnter.requestFocus();
                    nameEnter.setText(inputItem.getValueFrom());
                    nameEnter.setSelection(cursorItem.getValueFrom());
                    break;
                case InputDef.roll:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    final int position = inputItem.getPosition();
                    listRoll.get(position).setText(inputItem.getValueFrom());

                    adapter.setCursorPosition(cursorItem.getValueFrom());
                    adapter.notifyItemChanged(position, listRoll);
                    break;
                case InputDef.rollAdd:
                    listRoll.remove(inputItem.getPosition());

                    adapter.notifyItemRemoved(inputItem.getPosition(), listRoll);
                    break;
                case InputDef.rollRemove:
                    rollItem = new RollItem(inputItem.getValueFrom());
                    listRoll.add(inputItem.getPosition(), rollItem);

                    adapter.setCursorPosition(rollItem.getText().length());
                    adapter.notifyItemInserted(inputItem.getPosition(), listRoll);
                    break;
                case InputDef.rollMove:
                    final int positionStart = Integer.parseInt(inputItem.getValueTo());
                    final int positionEnd = Integer.parseInt(inputItem.getValueFrom());

                    rollItem = listRoll.get(positionStart);
                    listRoll.remove(positionStart);
                    listRoll.add(positionEnd, rollItem);

                    adapter.setList(listRoll);
                    adapter.notifyItemMoved(positionStart, positionEnd);
                    break;
            }

            if (inputItem.getTag() != InputDef.roll) {
                inputControl.setEnabled(true);
            }
        }

        bindInput();
    }

    @Override
    public void onRedoClick() {
        final InputItem inputItem = inputControl.redo();

        if (inputItem != null) {
            inputControl.setEnabled(false);

            final CursorItem cursorItem = inputItem.getCursorItem();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            RollItem rollItem;

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConverter stringConverter = new StringConverter();
                    final List<Long> rankId = stringConverter.fromString(inputItem.getValueTo());

                    noteItem.setRankId(rankId);
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
                case InputDef.roll:
                    assert cursorItem != null : "Cursor @NonNull for this tag";

                    final int position = inputItem.getPosition();
                    listRoll.get(position).setText(inputItem.getValueTo());

                    adapter.setCursorPosition(cursorItem.getValueTo());
                    adapter.notifyItemChanged(position, listRoll);
                    break;
                case InputDef.rollAdd:
                    rollItem = new RollItem(inputItem.getValueTo());
                    listRoll.add(inputItem.getPosition(), rollItem);

                    adapter.setCursorPosition(rollItem.getText().length());
                    adapter.notifyItemInserted(inputItem.getPosition(), listRoll);
                    break;
                case InputDef.rollRemove:
                    listRoll.remove(inputItem.getPosition());
                    adapter.notifyItemRemoved(inputItem.getPosition(), listRoll);
                    break;
                case InputDef.rollMove:
                    final int positionStart = Integer.parseInt(inputItem.getValueFrom());
                    final int positionEnd = Integer.parseInt(inputItem.getValueTo());

                    rollItem = listRoll.get(positionStart);
                    listRoll.remove(positionStart);
                    listRoll.add(positionEnd, rollItem);

                    adapter.setList(listRoll);
                    adapter.notifyItemMoved(positionStart, positionEnd);
                    break;
            }

            if (inputItem.getTag() != InputDef.roll) {
                inputControl.setEnabled(true);
            }
        }

        bindInput();
    }

    @Override
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
//        adapter.setNoteState(noteState);
//        adapter.notifyDataSetChanged();
//
//        noteCallback.getSaveControl().setSaveHandlerEvent(editMode);
//
//        inputControl.setEnabled(true);
//        inputControl.setChangeEnabled(true);
    }

    @Override
    public void onMenuRankClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        rankDialog.setArguments(vm.onMenuRank());
        rankDialog.show(fm, DialogDef.RANK);
    }

    @Override
    public void onMenuColorClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);

        final int color = vm.getNoteColor();

        colorDialog.setArguments(color);
        colorDialog.show(fm, DialogDef.COLOR);

        menuControl.setColorFrom(color);
    }

    @Override
    public void onMenuBindClick() {
        vm.onMenuBind();
        bindEdit(false); // TODO save
    }

    @Override
    public void onMenuConvertClick() {
        AppUtils.INSTANCE.hideKeyboard(activity);
        convertDialog.show(fm, DialogDef.CONVERT);
    }

    @Override
    public void onMenuCheckClick() {
        final NoteItem noteItem = vm.onMenuCheck(checkState.isAll());
        binding.setNoteItem(noteItem);
        binding.executePendingBindings();

        adapter.setCheckToggle(true);
        updateAdapter();
    }

}