package sgtmelon.handynotes.app.view.frg;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Editable;
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
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpRoll;
import sgtmelon.handynotes.app.control.MenuNote;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.app.viewModel.VmFrgTextRoll;
import sgtmelon.handynotes.databinding.FrgRollBinding;
import sgtmelon.handynotes.element.dialog.DlgColor;
import sgtmelon.handynotes.element.dialog.common.DlgMessage;
import sgtmelon.handynotes.element.dialog.common.DlgMultiply;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.office.st.StCheck;
import sgtmelon.handynotes.office.st.StDrag;
import sgtmelon.handynotes.office.st.StNote;

public class FrgRoll extends Fragment implements View.OnClickListener,
        IntfItem.Click, IntfItem.Watcher, IntfMenu.NoteClick, IntfMenu.RollClick {

    //region Variable
    private static final String TAG = "FrgRoll";

    private DbRoom db;

    private Context context;
    private ActNote activity;
    private FragmentManager fm;

    private FrgRollBinding binding;
    private View frgView;

    public VmFrgTextRoll vm;
    //endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");

        this.context = context;
        activity = (ActNote) getActivity();
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_roll, container, false);
        frgView = binding.getRoot();

        return frgView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        vm = ViewModelProviders.of(this).get(VmFrgTextRoll.class);
        if (vm.isEmpty()) vm.setRepoNote(activity.vm.getRepoNote());

        setupToolbar();
        setupDialog();

        setupRecyclerView();
        setupEnter();

        onMenuEditClick(activity.vm.getStNote().isEdit());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        String rollText = rollEnter.getText().toString();
        Help.Tint.button(context, rollAdd, R.drawable.ic_add, R.attr.clAccent, rollText);

        updateAdapter();
    }

    private void bind(boolean keyEdit, boolean keyCreate) {
        binding.setItemNote(vm.getRepoNote().getItemNote());
        binding.setKeyEdit(keyEdit);
        binding.setKeyCreate(keyCreate);

        binding.executePendingBindings();
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_note);

        View indicator = frgView.findViewById(R.id.incToolbarNote_iv_color);

        menuNote = new MenuNote(context, activity.getWindow(), toolbar, indicator, itemNote.getType());
        menuNote.setColor(itemNote.getColor());

        menuNote.setNoteClick(this);
        menuNote.setRollClick(this);
        menuNote.setDeleteClick(activity);
        menuNote.setupMenu(toolbar.getMenu(), itemNote.isStatus());

        toolbar.setOnMenuItemClickListener(menuNote);
        toolbar.setNavigationOnClickListener(this);
    }

    /**
     * Нажатие на клавишу назад
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        StNote stNote = activity.vm.getStNote();
        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();

        if (!stNote.isCreate() && stNote.isEdit() && !itemNote.getText().equals("")) { //Если редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DbRoom.provideDb(context);
            repoNote = db.daoNote().get(context, itemNote.getId());
            itemNote = repoNote.getItemNote();
            db.close();

            vm.setRepoNote(repoNote);
            activity.vm.setRepoNote(repoNote);

            adapter.update(repoNote.getListRoll());

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.saveNote.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    private DlgMessage dlgConvert;
    private DlgColor dlgColor;
    private DlgMultiply dlgRank;

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

        dlgConvert = (DlgMessage) fm.findFragmentByTag(Dlg.CONVERT);
        if (dlgConvert == null) dlgConvert = new DlgMessage();

        dlgConvert.setTitle(getString(R.string.dialog_title_convert));
        dlgConvert.setMessage(getString(R.string.dialog_roll_convert_to_text));
        dlgConvert.setPositiveListener((dialogInterface, i) -> {
            RepoNote repoNote = vm.getRepoNote();
            ItemNote itemNote = repoNote.getItemNote();

            db = DbRoom.provideDb(context);

            String text = db.daoRoll().getText(itemNote.getId());
            itemNote.setChange(context);
            itemNote.setType(DefType.text);
            itemNote.setText(text);

            db.daoNote().update(itemNote);
            db.daoRoll().delete(itemNote.getId());

            db.close();

            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);
            activity.vm.setRepoNote(repoNote);
            activity.setupFrg(false);
        });

        dlgColor = (DlgColor) fm.findFragmentByTag(Dlg.COLOR);
        if (dlgColor == null) dlgColor = new DlgColor();
        dlgColor.setTitle(getString(R.string.dialog_title_color));
        dlgColor.setPositiveListener((dialogInterface, i) -> {
            int check = dlgColor.getCheck();

            RepoNote repoNote = vm.getRepoNote();
            ItemNote itemNote = repoNote.getItemNote();
            itemNote.setColor(check);
            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);

            menuNote.startTint(check);
        });

        dlgRank = (DlgMultiply) fm.findFragmentByTag(Dlg.RANK);
        if (dlgRank == null) dlgRank = new DlgMultiply();

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

            RepoNote repoNote = vm.getRepoNote();

            ItemNote itemNote = repoNote.getItemNote();
            itemNote.setRankId(ConvList.fromList(rankId));
            itemNote.setRankPs(ConvList.fromList(rankPs));
            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);
        });
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        List<ItemRoll> listRoll = repoNote.getListRoll();

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

                bind(stNote.isEdit(), stNote.isCreate());

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
                repoNote.setListRoll(listRoll);
                adapter.update(listRoll);
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
                repoNote.setListRoll(listRoll);
                adapter.update(listRoll);

                List<Long> rollId = new ArrayList<>();
                for (ItemRoll itemRoll : listRoll) {
                    rollId.add(itemRoll.getId());
                }
                db.daoRoll().delete(itemNote.getId(), rollId);
            }
            db.daoRank().update(itemNote.getId(), itemNote.getRankId());

            db.close();

            repoNote.setItemNote(itemNote);

            vm.setRepoNote(repoNote);
            activity.vm.setRepoNote(repoNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        db = DbRoom.provideDb(context);
        boolean[] check = db.daoRank().getCheck(itemNote.getRankId());
        db.close();

        dlgRank.setArguments(check);
        dlgRank.show(fm, Dlg.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        dlgColor.setArguments(itemNote.getColor());
        dlgColor.show(fm, Dlg.COLOR);

        menuNote.setStartColor(itemNote.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        StNote stNote = activity.vm.getStNote();
        stNote.setEdit(editMode);

        menuNote.setMenuGroupVisible(stNote.isBin(), editMode, !stNote.isBin() && !editMode);
        bind(editMode, stNote.isCreate());

        adapter.update(editMode);

        activity.vm.setStNote(stNote);
        activity.saveNote.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();
        itemNote.setChange(context);

        int size = repoNote.getListRoll().size();

        db = DbRoom.provideDb(context);
        if (stCheck.isAll()) {
            repoNote.updateListRoll(DefCheck.notDone);
            itemNote.setText(0, size);

            db.daoRoll().update(itemNote.getId(), DefCheck.notDone);
            db.daoNote().update(itemNote);
        } else {
            repoNote.updateListRoll(DefCheck.done);
            itemNote.setText(size, size);

            db.daoRoll().update(itemNote.getId(), DefCheck.done);
            db.daoNote().update(itemNote);
        }
        db.close();

        updateAdapter();

        repoNote.setItemNote(itemNote);

        vm.setRepoNote(repoNote);
        activity.vm.setRepoNote(repoNote);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        RepoNote repoNote = vm.getRepoNote();
        ItemNote itemNote = repoNote.getItemNote();

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            repoNote.updateItemStatus(true);
        } else {
            itemNote.setStatus(false);
            repoNote.updateItemStatus(false);
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DbRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        repoNote.setItemNote(itemNote);

        vm.setRepoNote(repoNote);
        activity.vm.setRepoNote(repoNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        dlgConvert.show(fm, Dlg.CONVERT);
    }

    //region RecyclerView Variable
    private StDrag stDrag;
    private StCheck stCheck;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdpRoll adapter;
    //endregion

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        stDrag = new StDrag();
        stCheck = new StCheck();

        recyclerView = frgView.findViewById(R.id.frgRoll_rv);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdpRoll(activity.vm.getStNote().isBin(), activity.vm.getStNote().isEdit());
        recyclerView.setAdapter(adapter);
        adapter.setCallback(this, stDrag, this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        List<ItemRoll> listRoll = vm.getRepoNote().getListRoll();

        stCheck.setAll(listRoll);
        menuNote.setCheckTitle(stCheck.isAll());

        adapter.update(listRoll);
        adapter.notifyDataSetChanged();
    }

    /**
     * Обновление отметки пункта
     */
    @Override
    public void onItemClick(View view, int p) {

        RepoNote repoNote = vm.getRepoNote();

        List<ItemRoll> listRoll = repoNote.getListRoll();
        ItemRoll itemRoll = listRoll.get(p);
        itemRoll.setCheck(!itemRoll.isCheck());

        listRoll.set(p, itemRoll);
        repoNote.setListRoll(listRoll);

        adapter.update(p, itemRoll);

        int rollCheck = Help.Note.getRollCheck(listRoll);

        if (stCheck.setAll(rollCheck, listRoll.size())) {
            menuNote.setCheckTitle(stCheck.isAll());
        }

        ItemNote itemNote = repoNote.getItemNote();
        itemNote.setChange(context);
        itemNote.setText(rollCheck, listRoll.size());

        repoNote.setItemNote(itemNote);

        vm.setRepoNote(repoNote);
        activity.vm.setRepoNote(repoNote);

        db = DbRoom.provideDb(context);
        db.daoRoll().update(itemRoll.getId(), itemRoll.isCheck());
        db.daoNote().update(itemNote);
        db.close();
    }

    @Override
    public void onChanged(int p, String text) {
        Log.i(TAG, "onChanged");

        RepoNote repoNote = vm.getRepoNote();

        List<ItemRoll> listRoll = repoNote.getListRoll();
        if (text.equals("")) {
            listRoll.remove(p);
            adapter.update(listRoll);
            adapter.notifyItemRemoved(p);
        } else {
            ItemRoll itemRoll = listRoll.get(p);
            itemRoll.setText(text);

            listRoll.set(p, itemRoll);
            adapter.update(p, itemRoll);
        }
        repoNote.setListRoll(listRoll);

        vm.setRepoNote(repoNote);
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
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

            RepoNote repoNote = vm.getRepoNote();
            List<ItemRoll> listRoll = repoNote.getListRoll();

            listRoll.remove(p);                     //Убираем элемент из массива данных

            repoNote.setListRoll(listRoll);
            vm.setRepoNote(repoNote);

            adapter.update(listRoll);    //Обновление массива данных в адаптере
            adapter.notifyItemRemoved(p);       //Обновление удаления элемента
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();    //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();        //Новая позиция (куда отпустили)

            RepoNote repoNote = vm.getRepoNote();
            List<ItemRoll> listRoll = repoNote.getListRoll();

            ItemRoll itemRoll = listRoll.get(oldPs);
            listRoll.remove(oldPs);                        //Удаляем
            listRoll.add(newPs, itemRoll);             //И устанавливаем на новое место

            repoNote.setListRoll(listRoll);
            vm.setRepoNote(repoNote);

            adapter.update(listRoll);            //Обновление массива данных в адаптере
            adapter.notifyItemMoved(oldPs, newPs);    //Обновление передвижения
            return true;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                float itemWidth = viewHolder.itemView.getWidth();           //Ширина плитки
                float targetX = itemWidth / 2;                              //Конечная точка, где альфа = 0

                float translationX;                                         //Сдвиг, между начальной точкой и конечной
                if (dX > 0) {                                               //Сдвиг слева вправо
                    translationX = Math.abs(Math.min(dX, targetX));         //Выбираем минимальное (если dX превышает targetX, то выбираем второе)
                } else {                                                    //Сдвиг справа влево
                    translationX = Math.abs(Math.max(dX, -targetX));        //Выбираем максимальное (если dX пренижает targetX, то выбираем второе)
                }

                float alpha = 1.0f - translationX / targetX;                //Значение прозрачности

                viewHolder.itemView.setAlpha((float) Math.max(alpha, 0.2)); //Установка прозрачности
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //region EnterVariables
    private EditText rollEnter;
    private ImageButton rollAdd;
    //endregion

    private void setupEnter() {
        Log.i(TAG, "setupEnter");

        EditText nameEnter = frgView.findViewById(R.id.incToolbarNote_et_name);
        nameEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                rollEnter.requestFocus();
                return true;
            }
            return false;
        });

        rollEnter = frgView.findViewById(R.id.incNoteEnter_et_enter);
        rollAdd = frgView.findViewById(R.id.incNoteEnter_ib_add);

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

        String text = rollEnter.getText().toString();       //Берём текст из поля
        if (!text.equals("")) {                             //Если он != пустому месту, то добавляем
            rollEnter.setText("");                          //Сразу же убираем текст с поля

            int rollPs;                                     //Позиция, куда будем добавлять новый пункт
            if (scrollDown) {
                rollPs = adapter.getItemCount();        //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
            } else rollPs = 0;                              //Добавить в самое начало

            ItemRoll itemRoll = new ItemRoll(); //Создаём новый элемент
            itemRoll.setIdNote(vm.getRepoNote().getItemNote().getId());
            itemRoll.setText(text);
            itemRoll.setExist(false);

            RepoNote repoNote = vm.getRepoNote();
            List<ItemRoll> listRoll = repoNote.getListRoll();

            listRoll.add(rollPs, itemRoll);             //Добавляем его

            repoNote.setListRoll(listRoll);
            vm.setRepoNote(repoNote);

            adapter.update(listRoll);            //Обновляем адаптер

            int visiblePs;                                  //Видимая позиция
            if (scrollDown) {
                visiblePs = layoutManager.findLastVisibleItemPosition() + 1;   //Видимая последняя позиция +1 (добавленный элемент)
            } else {
                visiblePs = layoutManager.findFirstVisibleItemPosition();      //Видимая первая позиция
            }

            if (visiblePs == rollPs) {                                       //Если видимая позиция равна позиции куда добавили пункт
                recyclerView.scrollToPosition(rollPs);                        //Прокручиваем до края, незаметно
                adapter.notifyItemInserted(rollPs);                       //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(rollPs);                  //Медленно прокручиваем, через весь список
                adapter.notifyDataSetChanged();                             //Добавляем элемент без анимации
            }
        }
    }

}