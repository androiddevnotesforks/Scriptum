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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RollAdapter;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.InputItem;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.NoteActivityViewModel;
import sgtmelon.scriptum.databinding.FragmentRollNoteBinding;
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

public final class RollNoteFragment extends NoteFragmentParent implements ItemIntf.ClickListener,
        ItemIntf.RollWatcher {

    private static final String TAG = RollNoteFragment.class.getSimpleName();

    private final DragListenerSt dragSt = new DragListenerSt();
    private final CheckSt checkSt = new CheckSt();

    private FragmentRollNoteBinding binding;

    private LinearLayoutManager layoutManager;
    private RollAdapter adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragFrom = RecyclerView.NO_POSITION;
        private int dragTo;

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

            if (dragFrom != RecyclerView.NO_POSITION) return;

            dragFrom = actionState == ItemTouchHelper.ACTION_STATE_DRAG
                    ? viewHolder.getAdapterPosition()
                    : RecyclerView.NO_POSITION;
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragTo = viewHolder.getAdapterPosition();

            if (dragFrom == RecyclerView.NO_POSITION
                    || dragTo == RecyclerView.NO_POSITION
                    || dragFrom == dragTo) return;

            inputControl.onRollMove(dragFrom, dragTo);
            bindInput();

            dragFrom = RecyclerView.NO_POSITION;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            final int p = viewHolder.getAdapterPosition();
            final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

            inputControl.onRollRemove(p, listRoll.get(p).toString());
            bindInput();

            listRoll.remove(p);

            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int positionFrom = viewHolder.getAdapterPosition();
            final int positionTo = target.getAdapterPosition();

            final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();
            final RollItem rollItem = listRoll.get(positionFrom);

            listRoll.remove(positionFrom);
            listRoll.add(positionTo, rollItem);

            adapter.setList(listRoll);
            adapter.notifyItemMoved(positionFrom, positionTo);

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

    @NonNull
    public static RollNoteFragment getInstance(boolean rankEmpty) {
        Log.i(TAG, "getInstance: rankEmpty=" + rankEmpty);

        final RollNoteFragment rollNoteFragment = new RollNoteFragment();
        final Bundle bundle = new Bundle();

        bundle.putBoolean(IntentDef.RANK_EMPTY, rankEmpty);
        rollNoteFragment.setArguments(bundle);

        return rollNoteFragment;
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_roll_note, container, false);
        return frgView = binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        final NoteActivityViewModel viewModel = noteCallback.getViewModel();
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

        convertDialog.setMessage(getString(R.string.dialog_roll_convert_to_text));
        convertDialog.setPositiveListener((dialogInterface, i) -> {
            final NoteItem noteItem = vm.getNoteRepo().getNoteItem();

            db = RoomDb.provideDb(context);

            final String text = db.daoRoll().getText(noteItem.getId());
            noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
            noteItem.setType(TypeNoteDef.text);
            noteItem.setText(text);

            db.daoNote().update(noteItem);
            db.daoRoll().delete(noteItem.getId());
            db.close();

            noteCallback.getViewModel().setNoteRepo(vm.getNoteRepo());
            noteCallback.setupFragment(false);
        });
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        recyclerView = frgView.findViewById(R.id.roll_note_recycler);

        final SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RollAdapter(
                context, this, dragSt, this, inputControl, this
        );

        adapter.setNoteSt(noteCallback.getViewModel().getNoteSt());

        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void setupEnter() {
        Log.i(TAG, "setupEnter");
        super.setupEnter();

        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i != EditorInfo.IME_ACTION_NEXT) return false;

            rollEnter.requestFocus();
            return true;
        });

        rollEnter = frgView.findViewById(R.id.roll_note_enter);
        final ImageButton rollAdd = frgView.findViewById(R.id.roll_note_add_button);

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
        adapter.notifyItemRangeChanged(0, listRoll.size());

        adapter.setCheckToggle(false);
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

        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();
        listRoll.add(p, rollItem);

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
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemClick");

        if (p == RecyclerView.NO_POSITION) return;

        final NoteRepo noteRepo = vm.getNoteRepo();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        final RollItem rollItem = listRoll.get(p);
        rollItem.setCheck(!rollItem.isCheck());

        adapter.setListItem(p, rollItem);

        final NoteItem noteItem = noteRepo.getNoteItem();
        final int check = HelpUtils.Note.INSTANCE.getCheckCount(listRoll);
        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
        noteItem.setText(check, listRoll.size());

        if (checkSt.setAll(check, listRoll.size())) {
            binding.setNoteItem(noteItem);
            binding.executePendingBindings();
        }

        noteCallback.getViewModel().setNoteRepo(noteRepo);

        assert rollItem.getId() != null : "Roll from database with @NonNull id";

        db = RoomDb.provideDb(context);
        db.daoRoll().update(rollItem.getId(), rollItem.isCheck());
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void afterRollChanged(int p, @NonNull String text) {
        Log.i(TAG, "afterRollChanged");

        final List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

        if (TextUtils.isEmpty(text)) {
            listRoll.remove(p);

            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            final RollItem rollItem = listRoll.get(p);
            rollItem.setText(text);

            adapter.setListItem(p, rollItem);
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange, boolean showToast) {
        Log.i(TAG, "onMenuSaveClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        final List<RollItem> listRoll = noteRepo.getListRoll();

        if (listRoll.size() == 0) return false;

        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));
        noteItem.setText(HelpUtils.Note.INSTANCE.getCheckCount(listRoll), listRoll.size());

        //Переход в режим просмотра
        if (editModeChange) {
            HelpUtils.INSTANCE.hideKeyboard(context, activity.getCurrentFocus());
            onMenuEditClick(false);
        }

        db = RoomDb.provideDb(context);

        final NoteActivityViewModel viewModel = noteCallback.getViewModel();
        final NoteSt noteSt = viewModel.getNoteSt();
        if (noteSt.isCreate()) {
            noteSt.setCreate(false);

            if (!editModeChange) {
                menuControl.setDrawable(true, true);
            }

            final long id = db.daoNote().insert(noteItem);
            noteItem.setId(id);

            //Запись в пунктов в БД
            for (int i = 0; i < listRoll.size(); i++) {
                final RollItem rollItem = listRoll.get(i);
                rollItem.setNoteId(id);
                rollItem.setPosition(i);
                rollItem.setId(db.daoRoll().insert(rollItem));
            }

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
            }

            adapter.setList(listRoll);

            final List<Long> listRollId = new ArrayList<>();
            for (RollItem rollItem : listRoll) {
                listRollId.add(rollItem.getId());
            }

            db.daoRoll().delete(noteItem.getId(), listRollId);
        }

        db.daoRank().update(noteItem.getId(), noteItem.getRankId());
        db.close();

        viewModel.setNoteRepo(noteRepo);

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

            final NoteRepo noteRepo = vm.getNoteRepo();
            final NoteItem noteItem = noteRepo.getNoteItem();
            final List<RollItem> listRoll = noteRepo.getListRoll();

            RollItem rollItem;

            switch (inputItem.getTag()) {
                case InputDef.rank:
                    final StringConv stringConv = new StringConv();
                    final List<Long> rankId = stringConv.fromString(inputItem.getValueFrom());

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

                    adapter.setList(listRoll);
                    adapter.setCursorPosition(cursorItem.getValueFrom());
                    adapter.notifyItemChanged(position);
                    break;
                case InputDef.rollAdd:
                    listRoll.remove(inputItem.getPosition());

                    adapter.setList(listRoll);
                    adapter.notifyItemRemoved(inputItem.getPosition());
                    break;
                case InputDef.rollRemove:
                    rollItem = new RollItem(inputItem.getValueFrom());
                    listRoll.add(inputItem.getPosition(), rollItem);

                    adapter.setList(listRoll);
                    adapter.setCursorPosition(rollItem.getText().length());
                    adapter.notifyItemInserted(inputItem.getPosition());
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
        Log.i(TAG, "onRedoClick");

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
                    final StringConv stringConv = new StringConv();
                    final List<Long> rankId = stringConv.fromString(inputItem.getValueTo());

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

                    adapter.setList(listRoll);
                    adapter.setCursorPosition(cursorItem.getValueTo());
                    adapter.notifyItemChanged(position);
                    break;
                case InputDef.rollAdd:
                    rollItem = new RollItem(inputItem.getValueTo());
                    listRoll.add(inputItem.getPosition(), rollItem);

                    adapter.setList(listRoll);
                    adapter.setCursorPosition(rollItem.getText().length());
                    adapter.notifyItemInserted(inputItem.getPosition());
                    break;
                case InputDef.rollRemove:
                    listRoll.remove(inputItem.getPosition());

                    adapter.setList(listRoll);
                    adapter.notifyItemRemoved(inputItem.getPosition());
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

        adapter.setNoteSt(noteSt);
        adapter.notifyDataSetChanged();

        noteCallback.getSaveControl().setSaveHandlerEvent(editMode);

        inputControl.setEnabled(true);
        inputControl.setChangeEnabled(true);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        final NoteRepo noteRepo = vm.getNoteRepo();
        final NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(TimeUtils.INSTANCE.getTime(context));

        final int size = noteRepo.getListRoll().size();
        int check;

        db = RoomDb.provideDb(context);
        if (checkSt.isAll()) {
            check = 0;

            noteRepo.update(CheckDef.notDone);
            noteItem.setText(check, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.notDone);
            db.daoNote().update(noteItem);
        } else {
            check = size;

            noteRepo.update(CheckDef.done);
            noteItem.setText(check, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.done);
            db.daoNote().update(noteItem);
        }
        db.close();

        if (checkSt.setAll(check, size)) {
            binding.setNoteItem(noteItem);
            binding.executePendingBindings();
        }

        adapter.setCheckToggle(true);
        updateAdapter();

        noteCallback.getViewModel().setNoteRepo(noteRepo);
    }

}