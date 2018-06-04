package sgtmelon.handynotes.ui.note;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.adapter.AdapterRoll;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.model.state.CheckState;
import sgtmelon.handynotes.model.state.DragState;
import sgtmelon.handynotes.service.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.service.menu.MenuNote;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.interfaces.RollTextWatcher;
import sgtmelon.handynotes.view.alert.AlertColor;

public class FrgRoll extends Fragment implements View.OnClickListener,
        ItemClick.Click, RollTextWatcher, MenuNoteClick.NoteClick, MenuNoteClick.RollClick {

    //TODO: В идеале отмена последнего действия

    //region Variables
    private NoteDB noteDB;

    private View frgView;
    private Context context;
    private ActNote activity;

    private ItemNote itemNote;
    //endregion

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FrgRoll", "onResume");

        String rollText = rollEnter.getText().toString();
        Help.Icon.tintButton(context, rollAdd, R.drawable.ic_button_add, rollText);

        if (!activity.noteState.isEdit()) updateAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgRoll", "onCreateView");

        frgView = inflater.inflate(R.layout.frg_n_roll, container, false);

        context = getContext();
        activity = (ActNote) getActivity();

        setupToolbar();
        setupRecyclerView();
        setupEnter();

        onMenuEditClick(activity.noteState.isEdit());

        return frgView;
    }

    public MenuNote menuNote;

    private void setupToolbar() {
        Log.i("FrgRoll", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_note);

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
        Log.i("FrgRoll", "onClick");

        if (activity.noteState.isEdit() && !itemNote.getText().equals("")) { //Если это редактирование и текст в хранилище не пустой
            menuNote.setStartColor(itemNote.getColor());

            noteDB = new NoteDB(context);
            itemNote = noteDB.getNote(itemNote.getId());
            listRoll = noteDB.getRoll(itemNote.getCreate());
            noteDB.close();

            adapterRoll.updateAdapter(listRoll);

            onMenuEditClick(false);

            menuNote.startTint(itemNote.getColor());
        } else {
            activity.prefNoteSave.setNeedSave(false);
            activity.finish(); //Иначе завершаем активность
        }
    }

    @Override
    public boolean onMenuSaveClick(boolean changeEditMode) {
        Log.i("FrgRoll", "onMenuSaveClick");

        if (listRoll.size() != 0) {
            itemNote.setChange(Help.Time.getCurrentTime(context));      //Новое время редактирования
            itemNote.setName(nameEnter.getText().toString());           //Новый заголовок
            itemNote.setText(Help.Note.getCheckStr(listRoll));          //Новый текст

            if (changeEditMode) {
                Help.hideKeyboard(context, activity.getCurrentFocus());
                onMenuEditClick(false);                                            //Переход в режим просмотра
            }

            noteDB = new NoteDB(context);
            if (activity.noteState.isCreate()) {
                activity.noteState.setCreate(false);    //Теперь у нас заметка уже будет создана

                int noteId = noteDB.insertNote(itemNote);
                itemNote.setId(noteId);             //Получаем её id

                for (int rollPs = 0; rollPs < listRoll.size(); rollPs++) {           //Запись в пунктов в БД
                    ItemRoll itemRoll = listRoll.get(rollPs);

                    int rollId = noteDB.insertRoll(itemNote.getCreate(), rollPs, itemRoll.isCheck(), itemRoll.getText());

                    itemRoll.setId(rollId);             //Обновление некоторых значений
                    itemRoll.setExist(true);

                    listRoll.set(rollPs, itemRoll);
                }
                adapterRoll.updateAdapter(listRoll);
            } else {
                noteDB.updateNote(itemNote);

                for (int rollPs = 0; rollPs < listRoll.size(); rollPs++) {           //Запись и обновление пунктов в БД
                    ItemRoll itemRoll = listRoll.get(rollPs);

                    if (!itemRoll.isExist()) {                          //Если НЕТ В БАЗЕ ДАННЫХ
                        int rlId = noteDB.insertRoll(itemNote.getCreate(), rollPs, false, itemRoll.getText());

                        itemRoll.setId(rlId);
                        itemRoll.setExist(true);
                    } else {                                           //Если УЖЕ ЕСТЬ В БАЗЕ ДАННЫХ
                        noteDB.updateRoll(itemRoll.getId(), rollPs, itemRoll.getText());
                    }
                    listRoll.set(rollPs, itemRoll);
                }
                adapterRoll.updateAdapter(listRoll);

                String[] rollId = new String[listRoll.size()];        //ID тех пунктов которые остались
                for (int rollPs = 0; rollPs < listRoll.size(); rollPs++) {
                    rollId[rollPs] = Integer.toString(listRoll.get(rollPs).getId());
                }

                noteDB.deleteRoll(itemNote.getCreate(), rollId);  //Удаление пунктов которые свайпнули
            }
            noteDB.updateRank(itemNote.getCreate(), itemNote.getRankId());
            noteDB.close();

            activity.setItemNote(itemNote);
            return true;
        } else return false;
    }

    @Override
    public void onMenuRankClick() {
        Log.i("FrgRoll", "onMenuRankClick");

        noteDB = new NoteDB(context);
        final String[] checkName = noteDB.getRankColumn(NoteDB.RK_NM);
        final String[] checkId = noteDB.getRankColumn(NoteDB.RK_ID);
        final boolean[] checkItem = noteDB.getRankCheck(itemNote.getRankId());
        noteDB.close();

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
                        String[] addRank = new String[0];
                        for (int i = 0; i < checkId.length; i++) {
                            if (checkItem[i])
                                addRank = Help.Array.addStrItem(addRank, checkId[i]);
                        }
                        itemNote.setRankId(addRank);
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
        Log.i("FrgRoll", "onMenuColorClick");

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
        Log.i("FrgRoll", "onMenuEditClick: " + editMode);

        activity.noteState.setEdit(editMode);

        menuNote.setNavigationIcon(activity.noteState.isEdit(), activity.noteState.isCreate());
        menuNote.setMenuGroupVisible(activity.noteState.isBin(), activity.noteState.isEdit(), !activity.noteState.isBin() && !activity.noteState.isEdit());

        nameEnter.setText(itemNote.getName());          //Установка имени
        nameView.setText(itemNote.getName());

        adapterRoll.updateAdapter(editMode);

        if (editMode) {
            activity.prefNoteSave.startSaveHandler();

            nameEnter.setVisibility(View.VISIBLE);          //Делаем видимыми редакторы
            nameScrollView.setVisibility(View.GONE);        //Убираем просмотр текста
            rollEnterLayout.setVisibility(View.VISIBLE);    //Показываем линию добавления пунктов

            nameEnter.setSelection(itemNote.getName().length());
        } else {
            activity.prefNoteSave.stopSaveHandler();

            nameEnter.setVisibility(View.GONE);             //Убираем редакторы
            nameScrollView.setVisibility(View.VISIBLE);     //Делаем видимыми просмотр текста
            rollEnterLayout.setVisibility(View.GONE);       //Показываем/скрываем линию добавления пунктов
        }
    }

    @Override
    public void onMenuCheckClick() {
        Log.i("FrgRoll", "onMenuCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        noteDB = new NoteDB(context);
        if (checkState.isAll()) {
            itemNote.setText(Help.Note.getCheckStr(0, listRoll.size()));

            noteDB.updateRoll(itemNote.getCreate(), NoteDB.checkFalse);
            noteDB.updateNoteText(itemNote);
        } else {
            itemNote.setText(Help.Note.getCheckStr(listRoll.size(), listRoll.size()));

            noteDB.updateRoll(itemNote.getCreate(), NoteDB.checkTrue);
            noteDB.updateNoteText(itemNote);
        }
        noteDB.close();

        updateAdapter();
        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuBindClick() {
        Log.i("FrgRoll", "onMenuBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.itemStatus.notifyNote();
        } else {
            itemNote.setStatus(false);
            activity.itemStatus.cancelNote();
        }

        menuNote.setStatusTitle(itemNote.isStatus());

        noteDB = new NoteDB(context);
        noteDB.updateNote(itemNote.getId(), itemNote.isStatus());
        noteDB.close();

        activity.setItemNote(itemNote);
    }

    @Override
    public void onMenuConvertClick() {
        Log.i("FrgRoll", "onMenuConvertClick");

        noteDB = new NoteDB(context);
        String rollToText = noteDB.getRollText(itemNote.getCreate());   //Получаем текст заметки

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setType(NoteDB.typeText);
        itemNote.setText(rollToText);

        noteDB.updateNoteType(itemNote);                               //Обновляем заметку (меняем тип и текст)
        noteDB.deleteRoll(itemNote.getCreate());                        //Удаляем пункты бывшего списка
        noteDB.close();

        activity.setItemNote(itemNote);
        activity.setupFrg();
    }

    //region RecyclerView Variable
    private DragState dragState;
    private CheckState checkState;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private List<ItemRoll> listRoll;
    private AdapterRoll adapterRoll;
    //endregion

    private void setupRecyclerView() {
        Log.i("FrgRoll", "setupRecyclerView");

        dragState = new DragState();
        checkState = new CheckState();

        recyclerView = frgView.findViewById(R.id.recyclerView_frgRoll);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listRoll = new ArrayList<>();

        adapterRoll = new AdapterRoll(context, activity.noteState.isBin(), activity.noteState.isEdit());
        recyclerView.setAdapter(adapterRoll);
        adapterRoll.setCallback(this, dragState, this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter() {
        Log.i("FrgRoll", "updateAdapter");

        noteDB = new NoteDB(getContext());
        listRoll = noteDB.getRoll(itemNote.getCreate());
        noteDB.close();

        checkState.setAll(Help.Note.isAllCheck(listRoll));
        menuNote.setCheckTitle(checkState.isAll());

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

        if (checkState.setAll(checkValue, listRoll.size())) {
            menuNote.setCheckTitle(checkState.isAll());
        }

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(checkValue, listRoll.size()));

        activity.setItemNote(itemNote);

        NoteDB noteDB = new NoteDB(context);
        noteDB.updateRoll(itemRoll.getId(), itemRoll.isCheck());
        noteDB.updateNoteText(itemNote);
        noteDB.close();
    }

    @Override
    public void onRollChanged(int p, String rollText) {
        Log.i("FrgRoll", "onRollChanged");

        if (rollText.equals("")) {
            listRoll.remove(p);
            adapterRoll.updateAdapter(listRoll);
            adapterRoll.notifyItemRemoved(p);
        } else {
            ItemRoll itemRoll = listRoll.get(p);
            itemRoll.setText(rollText);

            listRoll.set(p, itemRoll);
            adapterRoll.updateAdapter(p, itemRoll);
        }
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = 0;
            int flagsSwipe = 0;

            if (activity.noteState.isEdit()) {
                if (dragState.isDrag()) flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
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
    private EditText nameEnter;
    private TextView nameView;
    private HorizontalScrollView nameScrollView;

    private LinearLayout rollEnterLayout;
    private EditText rollEnter;
    private ImageButton rollAdd;
    //endregion

    private void setupEnter() {
        Log.i("FrgRoll", "setupEnter");

        nameEnter = frgView.findViewById(R.id.editText_toolbarNote_name);
        nameView = frgView.findViewById(R.id.tView_toolbarNote_name);
        nameScrollView = frgView.findViewById(R.id.hScrollView_toolbarNote_name);

        rollEnterLayout = frgView.findViewById(R.id.layout_frgRoll_enter);
        rollEnter = frgView.findViewById(R.id.editText_frgRoll_enter);
        rollAdd = frgView.findViewById(R.id.iButton_frgRoll_add);

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
                Log.i("FrgRoll", "onClick");
                scrollToInsert(true);
            }
        };

        final View.OnLongClickListener addLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i("FrgRoll", "onLongClick");
                scrollToInsert(false);
                return true;
            }
        };

        rollEnter.addTextChangedListener(enterRollTextWatcher);
        rollAdd.setOnClickListener(addClick);
        rollAdd.setOnLongClickListener(addLongClick);
    }

    private void scrollToInsert(boolean scrollDown) {
        Log.i("FrgRoll", "scrollToInsert");

        String text = rollEnter.getText().toString();       //Берём текст из поля
        if (!text.equals("")) {                             //Если он != пустому месту, то добавляем
            rollEnter.setText("");                          //Сразу же убираем текст с поля

            int rollPs;                                     //Позиция, куда будем добавлять новый пункт
            if (scrollDown) {
                rollPs = adapterRoll.getItemCount();        //Добавить в конце (размер адаптера = последняя позиция + 1, но тут мы добавим в конец и данный размер станет равен позиции)
            } else rollPs = 0;                              //Добавить в самое начало

            ItemRoll itemRoll = new ItemRoll(); //Создаём новый элемент
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
