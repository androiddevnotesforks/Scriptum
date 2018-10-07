package sgtmelon.scriptum.app.view.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RollAdapter;
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
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.element.ColorDialog;
import sgtmelon.scriptum.element.common.MessageDialog;
import sgtmelon.scriptum.element.common.MultiplyDialog;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.conv.ListConv;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.CheckSt;
import sgtmelon.scriptum.office.st.DragSt;
import sgtmelon.scriptum.office.st.NoteSt;

public final class RollFragment extends Fragment implements View.OnClickListener,
        ItemIntf.Click, ItemIntf.Watcher, MenuIntf.NoteClick, MenuIntf.RollClick {

    private static final String TAG = RollFragment.class.getSimpleName();

    private final DragSt dragSt = new DragSt();
    private final CheckSt checkSt = new CheckSt();

    public MenuControl menuNote;

    @Inject
    public TextViewModel vm;

    @Inject
    FragmentManager fm;
    @Inject
    FragmentRollBinding binding;

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

    private LinearLayoutManager layoutManager;
    private RollAdapter adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = activity.vm.getNoteSt().isEdit() && dragSt.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            int flagsSwipe = activity.vm.getNoteSt().isEdit()
                    ? ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    : 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int p = viewHolder.getAdapterPosition();

            NoteModel noteModel = vm.getNoteModel();
            List<RollItem> listRoll = noteModel.getListRoll();

            listRoll.remove(p);                     //Убираем элемент из массива данных

            noteModel.setListRoll(listRoll);
            vm.setNoteModel(noteModel);

            adapter.setListRoll(listRoll);    //Обновление массива данных в адаптере
            adapter.notifyItemRemoved(p);       //Обновление удаления элемента
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();    //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();        //Новая позиция (куда отпустили)

            NoteModel noteModel = vm.getNoteModel();
            List<RollItem> listRoll = noteModel.getListRoll();

            RollItem rollItem = listRoll.get(oldPs);
            listRoll.remove(oldPs);                        //Удаляем
            listRoll.add(newPs, rollItem);             //И устанавливаем на новое место

            noteModel.setListRoll(listRoll);
            vm.setNoteModel(noteModel);

            adapter.setListRoll(listRoll);            //Обновление массива данных в адаптере
            adapter.notifyItemMoved(oldPs, newPs);    //Обновление передвижения
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

    private RoomDb db;
    private View frgView;

    private RecyclerView recyclerView;

    private LinearLayout rollContainer;
    private Animation translateIn, translateOut;

    private final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            rollContainer.setEnabled(false);

            if (animation == translateOut) {
                rollContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            rollContainer.setEnabled(true);

            if (animation == translateIn) {
                rollContainer.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private EditText rollEnter;
    private ImageButton rollAdd;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
        activity = (NoteActivity) getActivity(); // TODO: 02.10.2018 установи интерфейс общения
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        String rollText = rollEnter.getText().toString();
        Help.Tint.button(context, rollAdd, R.drawable.ic_add, R.attr.clAccent, rollText);

        updateAdapter();
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
        setupRecycler();
        setupEnter();

        onMenuEditClick(activity.vm.getNoteSt().isEdit());

        NoteSt noteSt = activity.vm.getNoteSt();
        noteSt.setFirst(false);
        activity.vm.setNoteSt(noteSt);
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind");

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
        menuNote.setRollClick(this);
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

        dlgConvert.setMessage(getString(R.string.dialog_roll_convert_to_text));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            NoteModel noteModel = vm.getNoteModel();
            NoteItem noteItem = noteModel.getNoteItem();

            db = RoomDb.provideDb(context);

            String text = db.daoRoll().getText(noteItem.getId());
            noteItem.setChange(context);
            noteItem.setType(TypeDef.text);
            noteItem.setText(text);

            db.daoNote().update(noteItem);
            db.daoRoll().delete(noteItem.getId());

            db.close();

            noteModel.setNoteItem(noteItem);

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

        dlgRank.setRows(name);
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

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        recyclerView = frgView.findViewById(R.id.roll_recycler);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RollAdapter(context);
        adapter.setNoteSt(activity.vm.getNoteSt());
        adapter.setCallback(this, dragSt, this);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<RollItem> listRoll = vm.getNoteModel().getListRoll();

        checkSt.setAll(listRoll);
        menuNote.setCheckTitle(checkSt.isAll());

        adapter.setListRoll(listRoll);
        adapter.notifyDataSetChanged();
    }

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        EditText nameEnter = frgView.findViewById(R.id.name_enter);
        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                rollEnter.requestFocus();
                return true;
            }
            return false;
        });

        rollContainer = frgView.findViewById(R.id.container);

        translateIn = AnimationUtils.loadAnimation(context, R.anim.translate_in);
        translateOut = AnimationUtils.loadAnimation(context, R.anim.translate_out);

        translateIn.setAnimationListener(animationListener);
        translateOut.setAnimationListener(animationListener);

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

    private void scrollToInsert(boolean scrollDown) {
        Log.i(TAG, "scrollToInsert");

        String text = rollEnter.getText().toString();   //Берём текст из поля
        if (!text.equals("")) {                         //Если он != пустому месту, то добавляем
            rollEnter.setText("");                      //Сразу же убираем текст с поля

            int ps;                                     //Позиция, куда будем добавлять новый пункт
            if (scrollDown) {
                ps = adapter.getItemCount();            //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
            } else ps = 0;                              //Добавить в самое начало

            RollItem rollItem = new RollItem();
            rollItem.setIdNote(vm.getNoteModel().getNoteItem().getId());
            rollItem.setText(text);
            rollItem.setExist(false);

            NoteModel noteModel = vm.getNoteModel();
            List<RollItem> listRoll = noteModel.getListRoll();

            listRoll.add(ps, rollItem);

            noteModel.setListRoll(listRoll);
            vm.setNoteModel(noteModel);

            adapter.setListRoll(listRoll);

            int visiblePs;
            if (scrollDown) {
                visiblePs = layoutManager.findLastVisibleItemPosition() + 1;   //Видимая последняя позиция +1 (добавленный элемент)
            } else {
                visiblePs = layoutManager.findFirstVisibleItemPosition();      //Видимая первая позиция
            }

            if (visiblePs == ps) {                          //Если видимая позиция равна позиции куда добавили пункт
                recyclerView.scrollToPosition(ps);          //Прокручиваем до края, незаметно
                adapter.notifyItemInserted(ps);             //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(ps);    //Медленно прокручиваем, через весь список
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

            adapter.setListRoll(noteModel.getListRoll());

            onMenuEditClick(false);

            menuNote.startTint(noteItem.getColor());
        } else {
            activity.saveControl.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    /**
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        NoteModel noteModel = vm.getNoteModel();

        List<RollItem> listRoll = noteModel.getListRoll();
        RollItem rollItem = listRoll.get(p);
        rollItem.setCheck(!rollItem.isCheck());

        listRoll.set(p, rollItem);
        noteModel.setListRoll(listRoll);

        adapter.setListRoll(p, rollItem);

        int rollCheck = Help.Note.getRollCheck(listRoll);

        if (checkSt.setAll(rollCheck, listRoll.size())) {
            menuNote.setCheckTitle(checkSt.isAll());
        }

        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setChange(context);
        noteItem.setText(rollCheck, listRoll.size());

        noteModel.setNoteItem(noteItem);

        vm.setNoteModel(noteModel);
        activity.vm.setNoteModel(noteModel);

        db = RoomDb.provideDb(context);
        db.daoRoll().update(rollItem.getId(), rollItem.isCheck());
        db.daoNote().update(noteItem);
        db.close();
    }

    @Override
    public void onChanged(int p, String text) {
        Log.i(TAG, "onChanged");

        NoteModel noteModel = vm.getNoteModel();

        List<RollItem> listRoll = noteModel.getListRoll();
        if (text.equals("")) {
            listRoll.remove(p);
            adapter.setListRoll(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            RollItem rollItem = listRoll.get(p);
            rollItem.setText(text);

            listRoll.set(p, rollItem);
            adapter.setListRoll(p, rollItem);
        }
        noteModel.setListRoll(listRoll);

        vm.setNoteModel(noteModel);
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();
        List<RollItem> listRoll = noteModel.getListRoll();

        if (listRoll.size() != 0) {
            noteItem.setChange(context);      //Новое время редактирования
            noteItem.setText(Help.Note.getRollCheck(listRoll), listRoll.size());          //Новый текст

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);                                            //Переход в режим просмотра
            }

            db = RoomDb.provideDb(context);

            NoteSt noteSt = activity.vm.getNoteSt();
            if (noteSt.isCreate()) {
                noteSt.setCreate(false);    //Теперь у нас заметка уже будет создана
                activity.vm.setNoteSt(noteSt);

                long ntId = db.daoNote().insert(noteItem);
                noteItem.setId(ntId);

                for (int i = 0; i < listRoll.size(); i++) {           //Запись в пунктов в БД
                    RollItem rollItem = listRoll.get(i);

                    rollItem.setIdNote(ntId);
                    rollItem.setPosition(i);
                    rollItem.setId(db.daoRoll().insert(rollItem));             //Обновление некоторых значений
                    rollItem.setExist(true);

                    listRoll.set(i, rollItem);
                }
                noteModel.setListRoll(listRoll);
                adapter.setListRoll(listRoll);
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
                noteModel.setListRoll(listRoll);
                adapter.setListRoll(listRoll);

                List<Long> rollId = new ArrayList<>();
                for (RollItem rollItem : listRoll) {
                    rollId.add(rollItem.getId());
                }
                db.daoRoll().delete(noteItem.getId(), rollId);
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

        if (noteSt.isCreate() && editMode) rollContainer.setVisibility(View.VISIBLE);
        else if (noteSt.isFirst()) rollContainer.setVisibility(View.GONE);
        else rollContainer.startAnimation(editMode ? translateIn : translateOut);

        bind(editMode);

        adapter.setNoteSt(noteSt);
        adapter.notifyDataSetChanged();

        activity.vm.setNoteSt(noteSt);
        activity.saveControl.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        NoteModel noteModel = vm.getNoteModel();
        NoteItem noteItem = noteModel.getNoteItem();
        noteItem.setChange(context);

        int size = noteModel.getListRoll().size();

        db = RoomDb.provideDb(context);
        if (checkSt.isAll()) {
            noteModel.updateListRoll(CheckDef.notDone);
            noteItem.setText(0, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.notDone);
            db.daoNote().update(noteItem);
        } else {
            noteModel.updateListRoll(CheckDef.done);
            noteItem.setText(size, size);

            db.daoRoll().update(noteItem.getId(), CheckDef.done);
            db.daoNote().update(noteItem);
        }
        db.close();

        updateAdapter();

        noteModel.setNoteItem(noteItem);

        vm.setNoteModel(noteModel);
        activity.vm.setNoteModel(noteModel);
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