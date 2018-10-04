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
import sgtmelon.scriptum.app.adapter.AdapterRoll;
import sgtmelon.scriptum.app.control.ControlMenu;
import sgtmelon.scriptum.app.control.ControlMenuPreL;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.model.ModelNote;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.TextViewModel;
import sgtmelon.scriptum.databinding.FragmentRollBinding;
import sgtmelon.scriptum.element.DlgColor;
import sgtmelon.scriptum.element.common.DlgMessage;
import sgtmelon.scriptum.element.common.DlgMultiply;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.conv.ConvList;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.intf.IntfMenu;
import sgtmelon.scriptum.office.st.StCheck;
import sgtmelon.scriptum.office.st.StDrag;
import sgtmelon.scriptum.office.st.StNote;

public final class RollFragment extends Fragment implements View.OnClickListener,
        IntfItem.Click, IntfItem.Watcher, IntfMenu.NoteClick, IntfMenu.RollClick {

    private static final String TAG = RollFragment.class.getSimpleName();

    private final StDrag stDrag = new StDrag();
    private final StCheck stCheck = new StCheck();

    public ControlMenuPreL menuNote;

    @Inject
    public TextViewModel vm;

    @Inject
    FragmentManager fm;
    @Inject
    FragmentRollBinding binding;

    @Inject
    @Named(DefDlg.CONVERT)
    DlgMessage dlgConvert;
    @Inject
    DlgColor dlgColor;
    @Inject
    @Named(DefDlg.RANK)
    DlgMultiply dlgRank;

    private NoteActivity activity;
    private Context context;

    private LinearLayoutManager layoutManager;
    private AdapterRoll adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = activity.vm.getStNote().isEdit() && stDrag.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            int flagsSwipe = activity.vm.getStNote().isEdit()
                    ? ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    : 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int p = viewHolder.getAdapterPosition();

            ModelNote modelNote = vm.getModelNote();
            List<ItemRoll> listRoll = modelNote.getListRoll();

            listRoll.remove(p);                     //Убираем элемент из массива данных

            modelNote.setListRoll(listRoll);
            vm.setModelNote(modelNote);

            adapter.setListRoll(listRoll);    //Обновление массива данных в адаптере
            adapter.notifyItemRemoved(p);       //Обновление удаления элемента
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();    //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();        //Новая позиция (куда отпустили)

            ModelNote modelNote = vm.getModelNote();
            List<ItemRoll> listRoll = modelNote.getListRoll();

            ItemRoll itemRoll = listRoll.get(oldPs);
            listRoll.remove(oldPs);                        //Удаляем
            listRoll.add(newPs, itemRoll);             //И устанавливаем на новое место

            modelNote.setListRoll(listRoll);
            vm.setModelNote(modelNote);

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

    private DbRoom db;
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

        ComponentFragment componentFragment = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        componentFragment.inject(this);

        frgView = binding.getRoot();

        if (vm.isEmpty()) vm.setModelNote(activity.vm.getModelNote());

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

        onMenuEditClick(activity.vm.getStNote().isEdit());

        StNote stNote = activity.vm.getStNote();
        stNote.setFirst(false);
        activity.vm.setStNote(stNote);
    }

    private void bind(boolean keyEdit) {
        Log.i(TAG, "bind");

        binding.setItemNote(vm.getModelNote().getItemNote());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getModelNote().getItemNote();

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_activity_note);

        View indicator = frgView.findViewById(R.id.color_view);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuNote = new ControlMenuPreL(context, activity.getWindow());
        } else {
            menuNote = new ControlMenu(context, activity.getWindow());
        }

        menuNote.setToolbar(toolbar);
        menuNote.setIndicator(indicator);
        menuNote.setType(itemNote.getType());
        menuNote.setColor(itemNote.getColor());

        menuNote.setNoteClick(this);
        menuNote.setRollClick(this);
        menuNote.setDeleteClick(activity);

        StNote stNote = activity.vm.getStNote();

        menuNote.setupDrawable();
        menuNote.setDrawable(stNote.isEdit() && !stNote.isCreate(), false);

        menuNote.setupMenu(itemNote.isStatus());

        toolbar.setOnMenuItemClickListener(menuNote);
        toolbar.setNavigationOnClickListener(this);
    }

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

        dlgConvert.setTitle(getString(R.string.dialog_title_convert));
        dlgConvert.setMessage(getString(R.string.dialog_roll_convert_to_text));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            ModelNote modelNote = vm.getModelNote();
            ItemNote itemNote = modelNote.getItemNote();

            db = DbRoom.provideDb(context);

            String text = db.daoRoll().getText(itemNote.getId());
            itemNote.setChange(context);
            itemNote.setType(DefType.text);
            itemNote.setText(text);

            db.daoNote().update(itemNote);
            db.daoRoll().delete(itemNote.getId());

            db.close();

            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);
            activity.setupFrg(false);
        });

        dlgColor.setTitle(getString(R.string.dialog_title_color));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            int check = dlgColor.getCheck();

            ModelNote modelNote = vm.getModelNote();
            ItemNote itemNote = modelNote.getItemNote();
            itemNote.setColor(check);
            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);

            menuNote.startTint(check);
        });

        db = DbRoom.provideDb(context);
        String[] name = db.daoRank().getName();
        db.close();

        dlgRank.setTitle(getString(R.string.dialog_title_rank));
        dlgRank.setName(name);
        dlgRank.setPositiveListener((dialogInterface, i) -> {
            boolean[] check = dlgRank.getCheck();

            db = DbRoom.provideDb(context);
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

            ModelNote modelNote = vm.getModelNote();

            ItemNote itemNote = modelNote.getItemNote();
            itemNote.setRankId(ConvList.fromList(rankId));
            itemNote.setRankPs(ConvList.fromList(rankPs));
            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);
        });
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        recyclerView = frgView.findViewById(R.id.roll_recycler);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterRoll(context);
        adapter.setStNote(activity.vm.getStNote());
        adapter.setCallback(this, stDrag, this);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<ItemRoll> listRoll = vm.getModelNote().getListRoll();

        stCheck.setAll(listRoll);
        menuNote.setCheckTitle(stCheck.isAll());

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

            ItemRoll itemRoll = new ItemRoll();
            itemRoll.setIdNote(vm.getModelNote().getItemNote().getId());
            itemRoll.setText(text);
            itemRoll.setExist(false);

            ModelNote modelNote = vm.getModelNote();
            List<ItemRoll> listRoll = modelNote.getListRoll();

            listRoll.add(ps, itemRoll);

            modelNote.setListRoll(listRoll);
            vm.setModelNote(modelNote);

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

        StNote stNote = activity.vm.getStNote();
        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();

        if (!stNote.isCreate() && stNote.isEdit() && !itemNote.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DbRoom.provideDb(context);
            modelNote = db.daoNote().get(context, itemNote.getId());
            itemNote = modelNote.getItemNote();
            db.close();

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);

            adapter.setListRoll(modelNote.getListRoll());

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.controlSave.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    /**
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(View view, int p) {
        Log.i(TAG, "onItemClick");

        ModelNote modelNote = vm.getModelNote();

        List<ItemRoll> listRoll = modelNote.getListRoll();
        ItemRoll itemRoll = listRoll.get(p);
        itemRoll.setCheck(!itemRoll.isCheck());

        listRoll.set(p, itemRoll);
        modelNote.setListRoll(listRoll);

        adapter.setListRoll(p, itemRoll);

        int rollCheck = Help.Note.getRollCheck(listRoll);

        if (stCheck.setAll(rollCheck, listRoll.size())) {
            menuNote.setCheckTitle(stCheck.isAll());
        }

        ItemNote itemNote = modelNote.getItemNote();
        itemNote.setChange(context);
        itemNote.setText(rollCheck, listRoll.size());

        modelNote.setItemNote(itemNote);

        vm.setModelNote(modelNote);
        activity.vm.setModelNote(modelNote);

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemRoll.getId(), itemRoll.isCheck());
        db.daoNote().update(itemNote);
        db.close();
    }

    @Override
    public void onChanged(int p, String text) {
        Log.i(TAG, "onChanged");

        ModelNote modelNote = vm.getModelNote();

        List<ItemRoll> listRoll = modelNote.getListRoll();
        if (text.equals("")) {
            listRoll.remove(p);
            adapter.setListRoll(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            ItemRoll itemRoll = listRoll.get(p);
            itemRoll.setText(text);

            listRoll.set(p, itemRoll);
            adapter.setListRoll(p, itemRoll);
        }
        modelNote.setListRoll(listRoll);

        vm.setModelNote(modelNote);
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();
        List<ItemRoll> listRoll = modelNote.getListRoll();

        if (listRoll.size() != 0) {
            itemNote.setChange(context);      //Новое время редактирования
            itemNote.setText(Help.Note.getRollCheck(listRoll), listRoll.size());          //Новый текст

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);                                            //Переход в режим просмотра
            }

            db = DbRoom.provideDb(context);

            StNote stNote = activity.vm.getStNote();
            if (stNote.isCreate()) {
                stNote.setCreate(false);    //Теперь у нас заметка уже будет создана
                activity.vm.setStNote(stNote);

                long ntId = db.daoNote().insert(itemNote);
                itemNote.setId(ntId);

                for (int i = 0; i < listRoll.size(); i++) {           //Запись в пунктов в БД
                    ItemRoll itemRoll = listRoll.get(i);

                    itemRoll.setIdNote(ntId);
                    itemRoll.setPosition(i);
                    itemRoll.setId(db.daoRoll().insert(itemRoll));             //Обновление некоторых значений
                    itemRoll.setExist(true);

                    listRoll.set(i, itemRoll);
                }
                modelNote.setListRoll(listRoll);
                adapter.setListRoll(listRoll);
            } else {
                db.daoNote().update(itemNote);

                for (int i = 0; i < listRoll.size(); i++) {
                    ItemRoll itemRoll = listRoll.get(i);

                    itemRoll.setPosition(i);
                    if (!itemRoll.isExist()) {
                        itemRoll.setId(db.daoRoll().insert(itemRoll));
                        itemRoll.setExist(true);
                    } else {
                        db.daoRoll().update(itemRoll.getId(), i, itemRoll.getText());
                    }

                    listRoll.set(i, itemRoll);
                }
                modelNote.setListRoll(listRoll);
                adapter.setListRoll(listRoll);

                List<Long> rollId = new ArrayList<>();
                for (ItemRoll itemRoll : listRoll) {
                    rollId.add(itemRoll.getId());
                }
                db.daoRoll().delete(itemNote.getId(), rollId);
            }
            db.daoRank().update(itemNote.getId(), itemNote.getRankId());

            db.close();

            modelNote.setItemNote(itemNote);

            vm.setModelNote(modelNote);
            activity.vm.setModelNote(modelNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getModelNote().getItemNote();

        db = DbRoom.provideDb(context);
        boolean[] check = db.daoRank().getCheck(itemNote.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, DefDlg.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getModelNote().getItemNote();

        dlgColor.setArguments(itemNote.getColor());
        dlgColor.show(fm, DefDlg.COLOR);

        menuNote.setStartColor(itemNote.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        StNote stNote = activity.vm.getStNote();
        stNote.setEdit(editMode);

        menuNote.setDrawable(editMode && !stNote.isCreate(), !stNote.isCreate());
        menuNote.setMenuGroupVisible(stNote.isBin(), editMode, !stNote.isBin() && !editMode);

        if (stNote.isCreate() && editMode) rollContainer.setVisibility(View.VISIBLE);
        else if (stNote.isFirst()) rollContainer.setVisibility(View.GONE);
        else rollContainer.startAnimation(editMode ? translateIn : translateOut);

        bind(editMode);

        adapter.setStNote(stNote);
        adapter.notifyDataSetChanged();

        activity.vm.setStNote(stNote);
        activity.controlSave.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();
        itemNote.setChange(context);

        int size = modelNote.getListRoll().size();

        db = DbRoom.provideDb(context);
        if (stCheck.isAll()) {
            modelNote.updateListRoll(DefCheck.notDone);
            itemNote.setText(0, size);

            db.daoRoll().update(itemNote.getId(), DefCheck.notDone);
            db.daoNote().update(itemNote);
        } else {
            modelNote.updateListRoll(DefCheck.done);
            itemNote.setText(size, size);

            db.daoRoll().update(itemNote.getId(), DefCheck.done);
            db.daoNote().update(itemNote);
        }
        db.close();

        updateAdapter();

        modelNote.setItemNote(itemNote);

        vm.setModelNote(modelNote);
        activity.vm.setModelNote(modelNote);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        ModelNote modelNote = vm.getModelNote();
        ItemNote itemNote = modelNote.getItemNote();

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            modelNote.updateItemStatus(true);
        } else {
            itemNote.setStatus(false);
            modelNote.updateItemStatus(false);
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        modelNote.setItemNote(itemNote);

        vm.setModelNote(modelNote);
        activity.vm.setModelNote(modelNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, DefDlg.CONVERT);
    }

}