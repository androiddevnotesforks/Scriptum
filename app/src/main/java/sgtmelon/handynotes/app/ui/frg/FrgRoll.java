package sgtmelon.handynotes.app.ui.frg;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterRoll;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.databinding.FrgRollBinding;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.state.StateCheck;
import sgtmelon.handynotes.app.model.state.StateDrag;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.app.control.menu.MenuNote;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.app.ui.act.ActNote;
import sgtmelon.handynotes.view.alert.AlertColor;

public class FrgRoll extends Fragment implements View.OnClickListener,
        IntfItem.Click, IntfItem.Watcher, IntfMenu.NoteClick, IntfMenu.RollClick {

    //region Variables
    final String TAG = "FrgRoll";

    private DataRoom db;

    private View frgView;
    private Context context;
    private ActNote activity;

    private ItemNote itemNote;
    //endregion

    private FrgRollBinding binding;

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    //TODO смена цвета в XML
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        String rollText = rollEnter.getText().toString();
        Help.Icon.tintButton(context, rollAdd, R.drawable.ic_button_add, rollText);

        if (!activity.stateNote.isEdit()) updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_roll, container, false);
        frgView = binding.getRoot();

        context = getContext();
        activity = (ActNote) getActivity();

        setupToolbar();
        setupRecyclerView();
        setupEnter();

        onMenuEditClick(activity.stateNote.isEdit());

        return frgView;
    }

    private void bind(boolean keyEdit, boolean keyCreate) {
        binding.setItemNote(itemNote);
        binding.setKeyEdit(keyEdit);
        binding.setKeyCreate(keyCreate);

        binding.executePendingBindings();
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.inflateMenu(R.menu.menu_act_note);

        menuNote = new MenuNote(context, activity.getWindow(), toolbar, itemNote.getType());
        menuNote.setColor(itemNote.getColor());

        menuNote.setNoteClick(this);
        menuNote.setRollClick(this);
        menuNote.setDeleteClick(activity);
        menuNote.setupMenu(toolbar.getMenu(), itemNote.isStatus());

        toolbar.setOnMenuItemClickListener(menuNote);
        toolbar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        if (activity.stateNote.isEdit() && !itemNote.getText().equals("")) { //Если это редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            db = DataRoom.provideDb(context);
            itemNote = db.daoNote().get(itemNote.getId());
            listRoll = db.daoRoll().get(itemNote.getId());
            db.close();

            adapterRoll.updateAdapter(listRoll);

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.controlSave.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean editModeChange) {
        Log.i(TAG, "onMenuSaveClick");

        if (listRoll.size() != 0) {
            itemNote.setChange(Help.Time.getCurrentTime(context));      //Новое время редактирования
            itemNote.setText(Help.Note.getCheckStr(listRoll));          //Новый текст

            if (editModeChange) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);                                            //Переход в режим просмотра
            }

            db = DataRoom.provideDb(context);

            if (activity.stateNote.isCreate()) {
                activity.stateNote.setCreate(false);    //Теперь у нас заметка уже будет создана

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
                adapterRoll.updateAdapter(listRoll);
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
                adapterRoll.updateAdapter(listRoll);

                List<Long> rollId = new ArrayList<>();
                for (ItemRoll itemRoll : listRoll) {
                    rollId.add(itemRoll.getId());
                }
                db.daoRoll().delete(itemNote.getId(), rollId);
            }
            db.daoRank().update(itemNote.getId(), itemNote.getRankId());

            db.close();

            activity.setItemNote(itemNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i(TAG, "onMenuRankClick");

        db = DataRoom.provideDb(context);

        final String[] checkName = db.daoRank().getName();
        final Long[] checkId = db.daoRank().getId();
        final boolean[] checkItem = db.daoRank().getCheck(itemNote.getRankId());

        db.close();

        Help.hideKeyboard(context, activity.getCurrentFocus());

        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_rank))
                .setMultiChoiceItems(checkName, checkItem, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkItem[which] = isChecked;
                    }
                })
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        List<Long> rankId = new ArrayList<>();
                        List<Long> rankPs = new ArrayList<>();

                        for (int i = 0; i < checkId.length; i++) {
                            if (checkItem[i]) {
                                rankId.add(checkId[i]);
                                rankPs.add((long) i);
                            }
                        }

                        itemNote.setRankId(ConvList.fromList(rankId));
                        itemNote.setRankPs(ConvList.fromList(rankPs));

                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onMenuColorClick() {
        Log.i(TAG, "onMenuColorClick");

        Help.hideKeyboard(context, activity.getCurrentFocus());

        final AlertColor alert = new AlertColor(context, itemNote.getColor(), R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_color))
                .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int color = alert.getCheckPosition();

                        itemNote.setColor(color);
                        menuNote.startTint(color);

                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();

        menuNote.setStartColor(itemNote.getColor());
    }

    @Override
    public void onMenuEditClick(boolean editMode) {
        Log.i(TAG, "onMenuEditClick: " + editMode);

        activity.stateNote.setEdit(editMode);

        menuNote.setMenuGroupVisible(activity.stateNote.isBin(), editMode, !activity.stateNote.isBin() && !editMode);
        bind(editMode, activity.stateNote.isCreate());

        adapterRoll.updateAdapter(editMode);

        activity.controlSave.setSaveHandlerEvent(editMode);
    }

    @Override
    public void onMenuCheckClick() {
        Log.i(TAG, "onMenuCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        db = DataRoom.provideDb(context);
        if (stateCheck.isAll()) {
            itemNote.setText(Help.Note.getCheckStr(0, listRoll.size()));

            db.daoRoll().update(itemNote.getId(), DefCheck.notDone);
            db.daoNote().update(itemNote);
        } else {
            itemNote.setText(Help.Note.getCheckStr(listRoll.size(), listRoll.size()));

            db.daoRoll().update(itemNote.getId(), DefCheck.done);
            db.daoNote().update(itemNote);
        }
        db.close();

        updateAdapter();
        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuBindClick() {
        Log.i(TAG, "onMenuBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.itemStatus.notifyNote();
        } else {
            itemNote.setStatus(false);
            activity.itemStatus.cancelNote();
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        db = DataRoom.provideDb(context);
        db.daoNote().update(itemNote.getId(), itemNote.isStatus());
        db.close();

        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i(TAG, "onMenuConvertClick");

        db = DataRoom.provideDb(context);

        String rollToText = db.daoRoll().getText(itemNote.getId());

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(DefType.text);
        itemNote.setText(rollToText);

        db.daoNote().update(itemNote);
        db.daoRoll().deleteRoll(itemNote.getId());

        db.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    //region RecyclerView Variable
    private StateDrag stateDrag;
    private StateCheck stateCheck;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private List<ItemRoll> listRoll;
    private AdapterRoll adapterRoll;
    //endregion

    private void setupRecyclerView() {
        Log.i(TAG, "setupRecyclerView");

        stateDrag = new StateDrag();
        stateCheck = new StateCheck();

        recyclerView = frgView.findViewById(R.id.frgRoll_rv);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listRoll = new ArrayList<>();

        adapterRoll = new AdapterRoll(activity.stateNote.isBin(), activity.stateNote.isEdit());
        recyclerView.setAdapter(adapterRoll);
        adapterRoll.setCallback(this, stateDrag, this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        db = DataRoom.provideDb(context);
        listRoll = db.daoRoll().get(itemNote.getId());
        db.close();

        stateCheck.setAll(Help.Note.isAllCheck(listRoll));
        menuNote.setCheckTitle(stateCheck.isAll());

        adapterRoll.updateAdapter(listRoll);
        adapterRoll.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int p) {
        ItemRoll itemRoll = listRoll.get(p);
        itemRoll.setCheck(!itemRoll.isCheck());

        listRoll.set(p, itemRoll);
        adapterRoll.updateAdapter(p, itemRoll);

        int checkValue = Help.Note.getCheckValue(listRoll);

        if (stateCheck.setAll(checkValue, listRoll.size())) {
            menuNote.setCheckTitle(stateCheck.isAll());
        }

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(checkValue, listRoll.size()));

        activity.setItemNote(itemNote);

        db = DataRoom.provideDb(context);
        db.daoRoll().update(itemRoll.getId(), itemRoll.isCheck());
        db.daoNote().update(itemNote);
        db.close();
    }

    @Override
    public void onChanged(int p, String text) {
        Log.i(TAG, "onChanged");

        if (text.equals("")) {
            listRoll.remove(p);
            adapterRoll.updateAdapter(listRoll);
            adapterRoll.notifyItemRemoved(p);
        } else {
            ItemRoll itemRoll = listRoll.get(p);
            itemRoll.setText(text);

            listRoll.set(p, itemRoll);
            adapterRoll.updateAdapter(p, itemRoll);
        }
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = 0;
            int flagsSwipe = 0;

            if (activity.stateNote.isEdit()) {
                if (stateDrag.isDrag()) flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                flagsSwipe = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int p = viewHolder.getAdapterPosition(); //Получаем позицию

            listRoll.remove(p);                      //Убираем элемент из массива данных
            adapterRoll.updateAdapter(listRoll);            //Обновление массива данных в адаптере
            adapterRoll.notifyItemRemoved(p);        //Обновление удаления элемента
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();    //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();        //Новая позиция (куда отпустили)

            ItemRoll itemRoll = listRoll.get(oldPs);
            listRoll.remove(oldPs);                        //Удаляем
            listRoll.add(newPs, itemRoll);             //И устанавливаем на новое место

            adapterRoll.updateAdapter(listRoll);            //Обновление массива данных в адаптере
            adapterRoll.notifyItemMoved(oldPs, newPs);    //Обновление передвижения
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

        rollEnter = frgView.findViewById(R.id.frgRoll_et_enter);
        rollAdd = frgView.findViewById(R.id.frgRoll_ib_add);

        nameEnter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    rollEnter.requestFocus();
                    return true;
                }
                return false;
            }
        });

        final TextWatcher enterRollTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String rollText = rollEnter.getText().toString();
                Help.Icon.tintButton(context, rollAdd, R.drawable.ic_button_add, rollText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        final View.OnClickListener addClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick");
                scrollToInsert(true);
            }
        };

        final View.OnLongClickListener addLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i(TAG, "onLongClick");
                scrollToInsert(false);
                return true;
            }
        };

        rollEnter.addTextChangedListener(enterRollTextWatcher);
        rollAdd.setOnClickListener(addClick);
        rollAdd.setOnLongClickListener(addLongClick);
    }

    private void scrollToInsert(boolean scrollDown) {
        Log.i(TAG, "scrollToInsert");

        String text = rollEnter.getText().toString();       //Берём текст из поля
        if (!text.equals("")) {                             //Если он != пустому месту, то добавляем
            rollEnter.setText("");                          //Сразу же убираем текст с поля

            int rollPs;                                     //Позиция, куда будем добавлять новый пункт
            if (scrollDown) {
                rollPs = adapterRoll.getItemCount();        //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
            } else rollPs = 0;                              //Добавить в самое начало

            ItemRoll itemRoll = new ItemRoll(); //Создаём новый элемент

            itemRoll.setIdNote(itemNote.getId());
            itemRoll.setText(text);
            itemRoll.setExist(false);

            listRoll.add(rollPs, itemRoll);             //Добавляем его
            adapterRoll.updateAdapter(listRoll);            //Обновляем адаптер

            int visiblePs;                                  //Видимая позиция
            if (scrollDown) {
                visiblePs = layoutManager.findLastVisibleItemPosition() + 1;   //Видимая последняя позиция +1 (добавленный элемент)
            } else {
                visiblePs = layoutManager.findFirstVisibleItemPosition();      //Видимая первая позиция
            }

            if (visiblePs == rollPs) {                                       //Если видимая позиция равна позиции куда добавили пункт
                recyclerView.scrollToPosition(rollPs);                        //Прокручиваем до края, незаметно
                adapterRoll.notifyItemInserted(rollPs);                       //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(rollPs);                  //Медленно прокручиваем, через весь список
                adapterRoll.notifyDataSetChanged();                             //Добавляем элемент без анимации
            }
        }
    }
}
