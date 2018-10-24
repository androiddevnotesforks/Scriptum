package sgtmelon.scriptum.app.view.frg;

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
import sgtmelon.scriptum.app.adapter.AdpRoll;
import sgtmelon.scriptum.app.control.CtrlMenu;
import sgtmelon.scriptum.app.control.CtrlMenuPreL;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComFrg;
import sgtmelon.scriptum.app.injection.component.DaggerComFrg;
import sgtmelon.scriptum.app.injection.module.ModBlankFrg;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.app.viewModel.VmFrgText;
import sgtmelon.scriptum.databinding.FrgRollBinding;
import sgtmelon.scriptum.element.dialog.DlgColor;
import sgtmelon.scriptum.element.dialog.common.DlgMessage;
import sgtmelon.scriptum.element.dialog.common.DlgMultiply;
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

public class FrgRoll extends Fragment implements View.OnClickListener,
        IntfItem.Click, IntfItem.Watcher, IntfMenu.NoteClick, IntfMenu.RollClick {

    //region Variable
    private static final String TAG = "FrgRoll";

    private DbRoom db;

    @Inject
    ActNote activity;
    @Inject
    Context context;
    @Inject
    FragmentManager fm;

    @Inject
    FrgRollBinding binding;
    @Inject
    public VmFrgText vm;

    private View frgView;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        String rollText = rollEnter.getText().toString();
        Help.Tint.button(context, rollAdd, R.drawable.ic_add, R.attr.clAccent, rollText);

        updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComFrg comFrg = DaggerComFrg.builder()
                .modBlankFrg(new ModBlankFrg(this, inflater, container))
                .build();
        comFrg.inject(this);

        if (vm.isEmpty()) vm.setRepoNote(activity.vm.getRepoNote());

        return frgView = binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

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

        binding.setItemNote(vm.getRepoNote().getItemNote());
        binding.setKeyEdit(keyEdit);

        binding.executePendingBindings();
    }

    @Inject
    public CtrlMenuPreL menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        ItemNote itemNote = vm.getRepoNote().getItemNote();

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_note);

        View indicator = frgView.findViewById(R.id.incToolbarNote_iv_color);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            menuNote = new CtrlMenuPreL(context, activity.getWindow());
        } else {
            menuNote = new CtrlMenu(context, activity.getWindow());
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
            activity.ctrlSave.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    @Inject
    @Named(DefDlg.CONVERT)
    DlgMessage dlgConvert;
    @Inject
    DlgColor dlgColor;
    @Inject
    @Named(DefDlg.RANK)
    DlgMultiply dlgRank;

    private void setupDialog() {
        Log.i(TAG, "setupDialog");

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
        dlgRank.show(fm, DefDlg.RANK);
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        ItemNote itemNote = vm.getRepoNote().getItemNote();

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
        else {
            rollContainer.startAnimation(editMode ? translateIn : translateOut);
        }

        bind(editMode);

        adapter.update(editMode);

        activity.vm.setStNote(stNote);
        activity.ctrlSave.setSaveHandlerEvent(editMode);
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

        dlgConvert.show(fm, DefDlg.CONVERT);
    }

    //region RecyclerView Variable
    private RecyclerView recyclerView;

    @Inject
    StDrag stDrag;
    @Inject
    StCheck stCheck;

    @Inject
    LinearLayoutManager layoutManager;
    @Inject
    AdpRoll adapter;
    //endregion

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        recyclerView = frgView.findViewById(R.id.frgRoll_rv);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setKey(activity.vm.getStNote().isBin(), activity.vm.getStNote().isEdit());
        adapter.setCallback(this, stDrag, this);

        recyclerView.setAdapter(adapter);

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
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
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
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
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

    //region EnterVariables
    private LinearLayout rollContainer;
    private Animation translateIn, translateOut;
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

        rollContainer = frgView.findViewById(R.id.incRollEnter_ll_container);

        translateIn = AnimationUtils.loadAnimation(context, R.anim.translate_in);
        translateOut = AnimationUtils.loadAnimation(context, R.anim.translate_out);

        translateIn.setAnimationListener(animationListener);
        translateOut.setAnimationListener(animationListener);

        rollEnter = frgView.findViewById(R.id.incRollEnter_et_enter);
        rollAdd = frgView.findViewById(R.id.incRollEnter_ib_add);

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