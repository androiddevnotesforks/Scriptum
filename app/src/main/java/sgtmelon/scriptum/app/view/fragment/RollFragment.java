package sgtmelon.scriptum.app.view.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;
import sgtmelon.scriptum.app.vm.activity.ActivityNoteViewModel;
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.CheckDef;
import sgtmelon.scriptum.office.annot.def.TypeDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.CheckSt;
import sgtmelon.scriptum.office.st.DragListenerSt;
import sgtmelon.scriptum.office.st.NoteSt;

public final class RollFragment extends NoteFragmentParent implements ItemIntf.ClickListener,
        ItemIntf.Watcher {

    private static final String TAG = RollFragment.class.getSimpleName();

    private final DragListenerSt dragSt = new DragListenerSt();
    private final CheckSt checkSt = new CheckSt();

    @Inject FragmentRollBinding binding;

    private LinearLayoutManager layoutManager;
    private RollAdapter adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            boolean isEdit = noteCallback.getViewModel().getNoteSt().isEdit();

            int flagsDrag = isEdit && dragSt.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            int flagsSwipe = isEdit
                    ? ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    : 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int p = viewHolder.getAdapterPosition();

            NoteRepo noteRepo = vm.getNoteRepo();
            List<RollItem> listRoll = noteRepo.getListRoll();

            listRoll.remove(p);

            noteRepo.setListRoll(listRoll);
            vm.setNoteRepo(noteRepo);

            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();    //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();        //Новая позиция (куда отпустили)

            NoteRepo noteRepo = vm.getNoteRepo();
            List<RollItem> listRoll = noteRepo.getListRoll();

            RollItem rollItem = listRoll.get(oldPs);
            listRoll.remove(oldPs);
            listRoll.add(newPs, rollItem);

            noteRepo.setListRoll(listRoll);
            vm.setNoteRepo(noteRepo);

            adapter.setList(listRoll);
            adapter.notifyItemMoved(oldPs, newPs);
            return true;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float itemWidth = viewHolder.itemView.getWidth();           //Ширина плитки
                float targetX = itemWidth / 2;                              //Конечная точка, где альфа = 0

                float translationX;                                         //Сдвиг, между начальной точкой и конечной
                if (dX > 0) {                                               //Сдвиг слева вправо
                    translationX = Math.abs(Math.min(dX, targetX));         //Выбираем минимальное (если dX превышает targetX, то выбираем второе)
                } else {                                                    //Сдвиг справа влево
                    translationX = Math.abs(Math.max(dX, -targetX));        //Выбираем максимальное (если dX принижает targetX, то выбираем второе)
                }

                float alpha = 1.0f - translationX / targetX;                //Значение прозрачности

                viewHolder.itemView.setAlpha((float) Math.max(alpha, 0.2)); //Установка прозрачности
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private RecyclerView recyclerView;
    private EditText rollEnter;
    private ImageButton rollAdd;

    private View panelContainer;
    private Animation translateIn, translateOut;

    private final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            panelContainer.setEnabled(false);

            if (animation == translateOut) {
                panelContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            panelContainer.setEnabled(true);

            if (animation == translateIn) {
                panelContainer.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        String rollText = rollEnter.getText().toString();
        Help.Tint.button(context, rollAdd, R.drawable.ic_add, R.attr.clAccent, rollText);

        updateAdapter();
    }

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
        if (vm.isEmpty()) vm.setNoteRepo(viewModel.getNoteRepo());

        setupToolbar();
        setupDialog();
        setupRecycler();
        setupPanel();
        setupEnter();

        NoteSt noteSt = viewModel.getNoteSt();

        onMenuEditClick(noteSt.isEdit());

        noteSt.setFirst(false);
        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);
    }

    @Override
    protected void bind(boolean keyEdit) {
        Log.i(TAG, "bind");

        binding.setNoteItem(vm.getNoteRepo().getNoteItem());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    @Override
    protected void setupDialog() {
        Log.i(TAG, "setupDialog");
        super.setupDialog();

        dlgConvert.setMessage(getString(R.string.dialog_roll_convert_to_text));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            NoteRepo noteRepo = vm.getNoteRepo();
            NoteItem noteItem = noteRepo.getNoteItem();

            db = RoomDb.provideDb(context);

            String text = db.daoRoll().getText(noteItem.getId());
            noteItem.setChange(Help.Time.getCurrentTime(context));
            noteItem.setType(TypeDef.Note.text);
            noteItem.setText(text);

            db.daoNote().update(noteItem);
            db.daoRoll().delete(noteItem.getId());

            db.close();

            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            ActivityNoteViewModel viewModel = noteCallback.getViewModel();
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

        adapter.setClickListener(this);
        adapter.setDragListener(dragSt);
        adapter.setWatcher(this);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupPanel() {
        Log.i(TAG, "setupPanel");

        panelContainer = frgView.findViewById(R.id.panel_container);

        translateIn = AnimationUtils.loadAnimation(context, R.anim.translate_in);
        translateOut = AnimationUtils.loadAnimation(context, R.anim.translate_out);

        translateIn.setAnimationListener(animationListener);
        translateOut.setAnimationListener(animationListener);
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        final EditText nameEnter = frgView.findViewById(R.id.name_enter);
        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                rollEnter.requestFocus();
                return true;
            }
            return false;
        });

        rollEnter = frgView.findViewById(R.id.roll_enter);
        rollAdd = frgView.findViewById(R.id.add_button);

        rollEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rollText = rollEnter.getText().toString();
                Help.Tint.button(context, rollAdd, R.drawable.ic_add, R.attr.clAccent, rollText);
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

        List<RollItem> listRoll = vm.getNoteRepo().getListRoll();

        checkSt.setAll(listRoll);
        menuControl.setCheckTitle(checkSt.isAll());

        adapter.setList(listRoll);
        adapter.notifyDataSetChanged();
    }

    private void scrollToInsert(boolean scrollDown) {
        Log.i(TAG, "scrollToInsert");

        String text = rollEnter.getText().toString();   //Берём текст из поля
        if (!text.equals("")) {                         //Если он != пустому месту, то добавляем
            rollEnter.setText("");                      //Сразу же убираем текст с поля

            int p;                                     //Позиция, куда будем добавлять новый пункт
            if (scrollDown) {
                p = adapter.getItemCount();            //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
            } else p = 0;                              //Добавить в самое начало

            RollItem rollItem = new RollItem();
            rollItem.setIdNote(vm.getNoteRepo().getNoteItem().getId());
            rollItem.setText(text);
            rollItem.setExist(false);

            NoteRepo noteRepo = vm.getNoteRepo();
            List<RollItem> listRoll = noteRepo.getListRoll();

            listRoll.add(p, rollItem);

            noteRepo.setListRoll(listRoll);
            vm.setNoteRepo(noteRepo);

            adapter.setList(listRoll);

            int visiblePs;
            if (scrollDown) {
                visiblePs = layoutManager.findLastVisibleItemPosition() + 1;   //Видимая последняя позиция +1 (добавленный элемент)
            } else {
                visiblePs = layoutManager.findFirstVisibleItemPosition();      //Видимая первая позиция
            }

            if (visiblePs == p) {                          //Если видимая позиция равна позиции куда добавили пункт
                recyclerView.scrollToPosition(p);          //Прокручиваем до края, незаметно
                adapter.notifyItemInserted(p);             //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(p);    //Медленно прокручиваем, через весь список
                adapter.notifyDataSetChanged();             //Добавляем элемент без анимации
            }
        }
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

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();

        if (!noteSt.isCreate() && noteSt.isEdit() && !noteItem.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
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
        } else {
            SaveControl saveControl = noteCallback.getSaveControl();
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

        NoteRepo noteRepo = vm.getNoteRepo();

        List<RollItem> listRoll = noteRepo.getListRoll();
        RollItem rollItem = listRoll.get(p);
        rollItem.setCheck(!rollItem.isCheck());

        listRoll.set(p, rollItem);
        noteRepo.setListRoll(listRoll);

        adapter.setListItem(p, rollItem);

        int rollCheck = Help.Note.getRollCheck(listRoll);

        if (checkSt.setAll(rollCheck, listRoll.size())) {
            menuControl.setCheckTitle(checkSt.isAll());
        }

        NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(Help.Time.getCurrentTime(context));
        noteItem.setText(rollCheck, listRoll.size());

        noteRepo.setNoteItem(noteItem);

        vm.setNoteRepo(noteRepo);

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteRepo(noteRepo);
        noteCallback.setViewModel(viewModel);

        db = RoomDb.provideDb(context);
        db.daoRoll().update(rollItem.getId(), rollItem.isCheck());
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void onChanged(int p, String text) {
        Log.i(TAG, "onChanged");

        NoteRepo noteRepo = vm.getNoteRepo();

        List<RollItem> listRoll = noteRepo.getListRoll();
        if (text.equals("")) {
            listRoll.remove(p);
            adapter.setList(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            RollItem rollItem = listRoll.get(p);
            rollItem.setText(text);

            listRoll.set(p, rollItem);
            adapter.setListItem(p, rollItem);
        }
        noteRepo.setListRoll(listRoll);

        vm.setNoteRepo(noteRepo);
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();
        List<RollItem> listRoll = noteRepo.getListRoll();

        if (listRoll.size() != 0) {
            noteItem.setChange(Help.Time.getCurrentTime(context));
            noteItem.setText(Help.Note.getRollCheck(listRoll), listRoll.size());

            //Переход в режим просмотра
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

                //Запись в пунктов в БД
                for (int i = 0; i < listRoll.size(); i++) {
                    RollItem rollItem = listRoll.get(i);

                    rollItem.setIdNote(ntId);
                    rollItem.setPosition(i);
                    rollItem.setId(db.daoRoll().insert(rollItem));
                    rollItem.setExist(true);

                    listRoll.set(i, rollItem);
                }
                noteRepo.setListRoll(listRoll);
                adapter.setList(listRoll);
            } else {
                db.daoNote().update(noteItem);

                for (int i = 0; i < listRoll.size(); i++) {
                    RollItem rollItem = listRoll.get(i);

                    rollItem.setPosition(i);
                    if (!rollItem.isExist()) {
                        rollItem.setId(db.daoRoll().insert(rollItem));
                        rollItem.setExist(true);
                    } else {
                        db.daoRoll().update(rollItem.getId(), i, rollItem.getText());
                    }

                    listRoll.set(i, rollItem);
                }
                noteRepo.setListRoll(listRoll);
                adapter.setList(listRoll);

                List<Long> rollId = new ArrayList<>();
                for (RollItem rollItem : listRoll) {
                    rollId.add(rollItem.getId());
                }
                db.daoRoll().delete(noteItem.getId(), rollId);
            }
            db.daoRank().update(noteItem.getId(), noteItem.getRankId());

            db.close();

            noteRepo.setNoteItem(noteItem);

            vm.setNoteRepo(noteRepo);

            viewModel.setNoteRepo(noteRepo);
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

        if (noteSt.isCreate() && editMode) panelContainer.setVisibility(View.VISIBLE);
        else if (noteSt.isFirst()) panelContainer.setVisibility(View.GONE);
        else panelContainer.startAnimation(editMode ? translateIn : translateOut);

        bind(editMode);

        adapter.setNoteSt(noteSt);
        adapter.notifyDataSetChanged();

        viewModel.setNoteSt(noteSt);
        noteCallback.setViewModel(viewModel);

        SaveControl saveControl = noteCallback.getSaveControl();
        saveControl.setSaveHandlerEvent(editMode);
        noteCallback.setSaveControl(saveControl);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        NoteRepo noteRepo = vm.getNoteRepo();
        NoteItem noteItem = noteRepo.getNoteItem();
        noteItem.setChange(Help.Time.getCurrentTime(context));

        int size = noteRepo.getListRoll().size();

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

        ActivityNoteViewModel viewModel = noteCallback.getViewModel();
        viewModel.setNoteRepo(noteRepo);
        noteCallback.setViewModel(viewModel);
    }

}