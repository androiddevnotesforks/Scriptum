package sgtmelon.scriptum.app.view.fragment;

import android.graphics.Canvas;
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
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RollAdapter;
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
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.office.annot.def.CheckDef;
import sgtmelon.scriptum.office.annot.def.InputDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.conv.StringConv;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.CheckSt;
import sgtmelon.scriptum.office.st.DragListenerSt;
import sgtmelon.scriptum.office.st.NoteSt;
import sgtmelon.scriptum.office.utils.HelpUtils;
import sgtmelon.scriptum.office.utils.TimeUtils;

public final class RollFragment extends NoteFragmentParent implements ItemIntf.ClickListener,
        ItemIntf.RollWatcher {

    private static final String TAG = RollFragment.class.getSimpleName();

    private final DragListenerSt dragSt = new DragListenerSt();
    private final CheckSt checkSt = new CheckSt();

    @Inject FragmentRollBinding binding;

    private LinearLayoutManager layoutManager;
    private RollAdapter adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStart, dragEnd;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            final boolean isEdit = noteCallback.getViewModel().getNoteSt().isEdit();

            final int flagsDrag = isEdit && dragSt.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            final int flagsSwipe = isEdit
                    ? ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    : 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            dragStart = actionState == ItemTouchHelper.ACTION_STATE_DRAG
                    ? viewHolder.getAdapterPosition()
                    : -1;
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragEnd = viewHolder.getAdapterPosition();
            if (dragStart != -1 && dragStart != dragEnd) {
                inputControl.onRollMove(dragStart, dragEnd);
                bindInput();
            }
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            final int p = viewHolder.getAdapterPosition();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            inputControl.onRollRemove(p, listRoll.get(p).toString());
            bindInput();

            listRoll.remove(p);

            noteRepo.setListRoll(listRoll);
            vm.setNoteRepo(noteRepo);

            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int oldPs = viewHolder.getAdapterPosition();
            final int newPs = target.getAdapterPosition();

            final NoteRepo noteRepo = vm.getNoteRepo();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            final RollItem rollItem = listRoll.get(oldPs);
            listRoll.remove(oldPs);
            listRoll.add(newPs, rollItem);

            noteRepo.setListRoll(listRoll);
            vm.setNoteRepo(noteRepo);

            adapter.setList(listRoll);
            adapter.notifyItemMoved(oldPs, newPs);

            return true;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                final float itemWidth = viewHolder.itemView.getWidth();

                //Конечная точка, где альфа = 0
                final float targetX = itemWidth / 2;

                //Сдвиг, между начальной точкой и конечной
                final float translationX = dX > 0
                        ? Math.abs(Math.min(dX, targetX))
                        : Math.abs(Math.max(dX, -targetX));

                final float alpha = 1.0f - translationX / targetX;

                viewHolder.itemView.setAlpha((float) Math.max(alpha, 0.2));
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };

    private RecyclerView recyclerView;
    private EditText rollEnter;

    public static RollFragment getInstance(boolean rankEmpty) {
        Log.i(TAG, "getInstance: rankEmpty=" + rankEmpty);

        final RollFragment rollFragment = new RollFragment();
        final Bundle bundle = new Bundle();

        bundle.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
        rollFragment.setArguments(bundle);

        return rollFragment;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        bindEnter(rollEnter.getText().toString());
        updateAdapter();
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
        setupRecycler();
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
        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);
    }

    @Override
    public void bindEdit(boolean editMode) {
        Log.i(TAG, "bindEdit: keyEdit=" + editMode);

        binding.setKeyEdit(editMode);
        binding.setNoteItem(vm.getNoteRepo().getNoteItem());

        bindEnter(rollEnter.getText().toString());
    }

    private void bindEnter(String enterText) {
        Log.i(TAG, "bindEnter");

        binding.setEnterEmpty(TextUtils.isEmpty(enterText));
        binding.executePendingBindings();
    }

    @Override
    public void bindInput() {
        Log.i(TAG, "bindInput");

        binding.setUndoAccess(inputControl.isUndoAccess());
        binding.setRedoAccess(inputControl.isRedoAccess());
        binding.setSaveEnabled(vm.getNoteRepo().getListRoll().size() != 0);
        binding.setRankSelect(vm.getNoteRepo().getNoteItem().getRankId().size() != 0);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_roll_convert_to_text));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();

            db = RoomDb.provideDb(context);

            final String text = db.daoRoll().getText(noteItem.getId());
            noteItem.setChange(TimeUtils.getTime(context));
            noteItem.setType(TypeNoteDef.text);
            noteItem.setText(text);

            db.daoNote().update(noteItem);
            db.daoRoll().delete(noteItem.getId());

            db.close();

            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
            viewModel.setNoteRepo(noteRepo);
            noteCallback.setViewModel(viewModel);

            noteCallback.setupFragment(false);
        });
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        recyclerView = frgView.findViewById(R.id.roll_recycler);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RollAdapter(context);
        adapter.setNoteSt(noteCallback.getViewModel().getNoteSt());
        adapter.setInputIntf(inputControl);
        adapter.setBindIntf(this);

        adapter.setClickListener(this);
        adapter.setDragListener(dragSt);
        adapter.setRollWatcher(this);

        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");
        super.setupEnter();

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                rollEnter.requestFocus();
                return true;
            }
            return false;
        });

        rollEnter = frgView.findViewById(R.id.roll_enter);
        final ImageButton rollAdd = frgView.findViewById(R.id.add_button);

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

        rollAdd.setOnClickListener(view -> scrollToInsert(true));
        rollAdd.setOnLongClickListener(view -> {
            scrollToInsert(false);
            return true;
        });
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

        checkSt.setAll(listRoll);

        adapter.setList(listRoll);
        adapter.notifyDataSetChanged();
    }

    private void scrollToInsert(boolean scrollDown) {
        Log.i(TAG, "scrollToInsert");

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

        final NoteRepo noteRepo = vm.getNoteRepo();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        listRoll.add(p, rollItem);

        noteRepo.setListRoll(listRoll);
        vm.setNoteRepo(noteRepo);

        adapter.setList(listRoll);

        final int visiblePs = scrollDown
                ? layoutManager.findLastVisibleItemPosition() + 1
                : layoutManager.findFirstVisibleItemPosition();

        if (visiblePs == p) {                          //Если видимая позиция равна позиции куда добавили пункт
            recyclerView.scrollToPosition(p);          //Прокручиваем до края, незаметно
            adapter.notifyItemInserted(p);             //Добавляем элемент с анимацией
        } else {
            recyclerView.smoothScrollToPosition(p);    //Медленно прокручиваем, через весь список
            adapter.notifyDataSetChanged();             //Добавляем элемент без анимации
        }
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

            adapter.setList(noteRepo.getListRoll());

            onMenuEditClick(false);

            menuControl.startTint(noteItem.getColor());

            inputControl.clear();
            bindInput();
        } else {
            final SaveControl saveControl = noteCallback.getSaveControl();
            saveControl.setNeedSave(false);
            noteCallback.setSaveControl(saveControl);

            activity.finish(); //Иначе завершаем активность
        }
    }

    /**
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        final NoteRepo noteRepo = vm.getNoteRepo();

        final List<RollItem> listRoll = noteRepo.getListRoll();
        final RollItem rollItem = listRoll.get(p);
        rollItem.setCheck(!rollItem.isCheck());

        listRoll.set(p, rollItem);
        noteRepo.setListRoll(listRoll);

        adapter.setListItem(p, rollItem);

        final int check = HelpUtils.Note.getRollCheck(listRoll);

        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.getTime(context));
        noteItem.setText(check, listRoll.size());

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        if (checkSt.setAll(check, listRoll.size())) {
            binding.setNoteItem(noteItem);
            binding.executePendingBindings();
        }

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteRepo(noteRepo);
        noteCallback.setViewModel(viewModel);

        if (rollItem.getId() == null) return;

        db = RoomDb.provideDb(context);
        db.daoRoll().update(rollItem.getId(), rollItem.isCheck());
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void afterRollChanged(int p, String text) {
        Log.i(TAG, "afterRollChanged");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        if (TextUtils.isEmpty(text)) {
            listRoll.remove(p);
            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            final RollItem rollItem = listRoll.get(p);
            rollItem.setText(text);

            adapter.setListItem(p, rollItem);
        }

        noteRepo.setListRoll(listRoll);
        vm.setNoteRepo(noteRepo);
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange, boolean showToast) {
        Log.i(TAG, "onMenuSaveClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        if (listRoll.size() == 0) return false;

        noteItem.setChange(TimeUtils.getTime(context));
        noteItem.setText(HelpUtils.Note.getRollCheck(listRoll), listRoll.size());

        //Переход в режим просмотра
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

            final long ntId = db.daoNote().insert(noteItem);
            noteItem.setId(ntId);

            //Запись в пунктов в БД
            for (int i = 0; i < listRoll.size(); i++) {
                final RollItem rollItem = listRoll.get(i);
                rollItem.setNoteId(ntId);
                rollItem.setPosition(i);
                rollItem.setId(db.daoRoll().insert(rollItem));

                listRoll.set(i, rollItem);
            }
            noteRepo.setListRoll(listRoll);
            adapter.setList(listRoll);
        } else {
            db.daoNote().update(noteItem);

            for (int i = 0; i < listRoll.size(); i++) {
                final RollItem rollItem = listRoll.get(i);

                rollItem.setPosition(i);
                if (rollItem.getId() == null) {
                    rollItem.setId(db.daoRoll().insert(rollItem));
                } else {
                    db.daoRoll().update(rollItem.getId(), i, rollItem.getText());
                }

                listRoll.set(i, rollItem);
            }

            noteRepo.setListRoll(listRoll);
            adapter.setList(listRoll);

            final List<Long> listRollId = new ArrayList<>();
            for (RollItem rollItem : listRoll) {
                listRollId.add(rollItem.getId());
            }

            db.daoRoll().delete(noteItem.getId(), listRollId);
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
    }

    // TODO: 16.12.2018 Приложение падает при свайпе пункта и потом undo/redo

    @Override
    public void onUndoClick() {
        Log.i(TAG, "onUndoClick");

        inputControl.setEnable(false);
        final InputItem inputItem = inputControl.undo();

        if (inputItem != null) {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConv stringConv = new StringConv();
                    final List<Long> rankId = stringConv.fromString(inputItem.getValueFrom());

                    noteItem.setRankId(rankId);
                    break;
                case InputDef.color:
                    final int color = Integer.parseInt(inputItem.getValueFrom());

                    menuControl.setStartColor(noteItem.getColor());
                    noteItem.setColor(color);
                    menuControl.startTint(color);
                    break;
                case InputDef.name:
                    nameEnter.setText(inputItem.getValueFrom());
                    break;
                case InputDef.roll:
                    final int position = inputItem.getPosition();
                    listRoll.get(position).setText(inputItem.getValueFrom());

                    adapter.setList(listRoll);
                    adapter.notifyItemChanged(position);
                    break;
                case InputDef.rollAdd:
                    listRoll.remove(inputItem.getPosition());

                    adapter.setList(listRoll);
                    adapter.notifyItemRemoved(inputItem.getPosition());
                    break;
                case InputDef.rollRemove:
                    listRoll.add(inputItem.getPosition(), new RollItem(inputItem.getValueFrom()));

                    adapter.setList(listRoll);
                    adapter.notifyItemInserted(inputItem.getPosition());
                    break;
                case InputDef.rollMove:
                    final int startPosition = Integer.parseInt(inputItem.getValueTo());
                    final int endPosition = Integer.parseInt(inputItem.getValueFrom());

                    final RollItem rollItem = listRoll.get(startPosition);
                    listRoll.remove(startPosition);
                    listRoll.add(endPosition, rollItem);

                    adapter.setList(listRoll);
                    adapter.notifyItemMoved(startPosition, endPosition);
                    break;
            }
        }

        inputControl.setEnable(true);
        bindInput();
    }

    @Override
    public void onRedoClick() {
        Log.i(TAG, "onRedoClick");

        inputControl.setEnable(false);
        final InputItem inputItem = inputControl.redo();

        if (inputItem != null) {
            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConv stringConv = new StringConv();
                    final List<Long> rankId = stringConv.fromString(inputItem.getValueTo());

                    noteItem.setRankId(rankId);
                    break;
                case InputDef.color:
                    final int colorTo = Integer.parseInt(inputItem.getValueTo());

                    menuControl.setStartColor(noteItem.getColor());
                    noteItem.setColor(colorTo);
                    menuControl.startTint(colorTo);
                    break;
                case InputDef.name:
                    nameEnter.setText(inputItem.getValueTo());
                    break;
                case InputDef.roll:
                    final int position = inputItem.getPosition();
                    listRoll.get(position).setText(inputItem.getValueTo());

                    adapter.setList(listRoll);
                    adapter.notifyItemChanged(position);
                    break;
                case InputDef.rollAdd:
                    listRoll.add(inputItem.getPosition(), new RollItem(inputItem.getValueTo()));

                    adapter.setList(listRoll);
                    adapter.notifyItemInserted(inputItem.getPosition());
                    break;
                case InputDef.rollRemove:
                    listRoll.remove(inputItem.getPosition());

                    adapter.setList(listRoll);
                    adapter.notifyItemRemoved(inputItem.getPosition());
                    break;
                case InputDef.rollMove:
                    final int startPosition = Integer.parseInt(inputItem.getValueFrom());
                    final int endPosition = Integer.parseInt(inputItem.getValueTo());

                    final RollItem rollItem = listRoll.get(startPosition);
                    listRoll.remove(startPosition);
                    listRoll.add(endPosition, rollItem);

                    adapter.setList(listRoll);
                    adapter.notifyItemMoved(startPosition, endPosition);
                    break;
            }
        }

        inputControl.setEnable(true);
        bindInput();
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

        adapter.setNoteSt(noteSt);
        adapter.notifyDataSetChanged();

        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);

        final SaveControl saveControl = noteCallback.getSaveControl();
        saveControl.setSaveHandlerEvent(editMode);
        noteCallback.setSaveControl(saveControl);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.getTime(context));

        final int size = noteRepo.getListRoll().size();

        db = RoomDb.provideDb(context);
        if (checkSt.isAll()) {
            noteRepo.update(CheckDef.notDone);
            noteItem.setText(0, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.notDone);
            db.daoNote().update(noteItem);
        } else {
            noteRepo.update(CheckDef.done);
            noteItem.setText(size, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.done);
            db.daoNote().update(noteItem);
        }
        db.close();

        updateAdapter();

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        final ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteRepo(noteRepo);
        noteCallback.setViewModel(viewModel);
    }

}