package sgtmelon.handynotes.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.adapter.AdapterRank;
import sgtmelon.handynotes.interfaces.InfoPageReply;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.model.manager.ListRankManager;
import sgtmelon.handynotes.model.state.DragState;
import sgtmelon.handynotes.service.NoteDB;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.service.InfoPageEmpty;
import sgtmelon.handynotes.view.alert.AlertRename;

public class FrgRank extends Fragment implements ItemClick.Click, ItemClick.LongClick,
        View.OnClickListener, View.OnLongClickListener, InfoPageReply {

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FrgRank", "onResume");

        updateAdapter(true);

        listRankManager.tintButton();
    }

    //region Variables
    private NoteDB noteDB;

    private View frgView;
    private Context context;
    public ActMain activity;

    private InfoPageEmpty infoPageEmpty;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgRank", "onCreateView");

        frgView = inflater.inflate(R.layout.frg_m_rank, container, false);

        context = getContext();
        activity = (ActMain) getActivity();

        setupToolbar();
        setupRecyclerView();

        LinearLayout info = frgView.findViewById(R.id.layout_frgRank_empty);
        infoPageEmpty = new InfoPageEmpty(context, info);
        infoPageEmpty.setInfoPageReply(this);

        return frgView;
    }

    public ListRankManager listRankManager;

    private void setupToolbar() {
        Log.i("FrgRank", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_frg_rank));

        ImageButton rankCancel = frgView.findViewById(R.id.iButton_toolbarRank_cancel);
        ImageButton rankAdd = frgView.findViewById(R.id.iButton_toolbarRank_add);
        EditText rankEnter = frgView.findViewById(R.id.editText_toolbarRank_enter);

        listRankManager = new ListRankManager(context);

        listRankManager.setOnClickListener(this);
        listRankManager.setControl(rankCancel, rankAdd, rankEnter);

        rankEnter.addTextChangedListener(listRankManager);
        rankEnter.setOnEditorActionListener(listRankManager);

        rankCancel.setOnClickListener(this);
        rankAdd.setOnClickListener(this);
        rankAdd.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("FrgRank", "onClick");

        switch (view.getId()) {
            case R.id.iButton_toolbarRank_cancel:
                listRankManager.clearEnter();
                break;
            case R.id.iButton_toolbarRank_add:
                int rankPs = listRankManager.size();
                String rankNm = listRankManager.clearEnter();                           //Сбрасываем поле ввода

                noteDB = new NoteDB(context);
                int rankId = noteDB.insertRank(rankPs, rankNm);                         //Добавляем заметку (с позицией равной количеству элементов)
                noteDB.close();

                ItemRank itemRank = new ItemRank(rankId, rankPs, rankNm);
                listRankManager.add(rankPs, itemRank);
                adapterRank.updateAdapter(listRankManager.getListRank());

                if (listRankManager.size() == 1) infoPageEmpty.hide();
                else {
                    if (layoutManager.findLastVisibleItemPosition() == rankPs - 1) {    //Если видимая позиция равна позиции куда добавили заметку
                        recyclerView.scrollToPosition(rankPs);                          //Прокручиваем до края, незаметно
                        adapterRank.notifyItemInserted(rankPs);                         //Добавляем элемент с анимацией
                    } else {
                        recyclerView.smoothScrollToPosition(rankPs);                    //Медленно прокручиваем, через весь список
                        adapterRank.notifyDataSetChanged();                             //Добавляем элемент без анимации
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i("FrgRank", "onLongClick");

        int rankPs = 0;
        String rankNm = listRankManager.clearEnter();                         //Сбрасываем поле ввода

        noteDB = new NoteDB(context);
        int rankId = noteDB.insertRank(rankPs - 1, rankNm);        //Добавляем заметку (с позицией -1)
        noteDB.updateRank(rankPs);                                          //Затем обновляем позиции с самого начала
        noteDB.close();

        ItemRank itemRank = new ItemRank(rankId, rankPs, rankNm);
        listRankManager.add(rankPs, itemRank);
        adapterRank.updateAdapter(listRankManager.getListRank());

        if (listRankManager.size() == 1) infoPageEmpty.hide();
        else {
            if (layoutManager.findFirstVisibleItemPosition() == rankPs) {   //Если видимая позиция равна позиции куда добавили заметку
                recyclerView.scrollToPosition(rankPs);                      //Прокручиваем до края, незаметно
                adapterRank.notifyItemInserted(rankPs);                     //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(rankPs);                //Медленно прокручиваем, через весь список
                adapterRank.notifyDataSetChanged();                         //Добавляем элемент без анимации
            }
        }
        return true;
    }

    //region RecyclerView variables
    private DragState dragState;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterRank adapterRank;
    //endregion

    private void setupRecyclerView() {
        Log.i("FrgRank", "setupRecyclerView");

        dragState = new DragState();

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                infoPageEmpty.setVisible(true, listRankManager.size());
            }
        };

        recyclerView = frgView.findViewById(R.id.recyclerView_frgRank);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapterRank = new AdapterRank(context);
        recyclerView.setAdapter(adapterRank);

        adapterRank.setCallback(this, this, dragState);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter(boolean updateAll) {
        Log.i("FrgRank", "updateAdapter");

        noteDB = new NoteDB(context);
        listRankManager.setListRank(noteDB.getRank());
        if (updateAll) listRankManager.setListRankName(noteDB.getRankName());
        noteDB.close();

        adapterRank.updateAdapter(listRankManager.getListRank());
        adapterRank.notifyDataSetChanged();

        infoPageEmpty.setVisible(false, listRankManager.size());
    }

    @Override
    public void notifyInsert(int position) {
        adapterRank.notifyItemInserted(position);
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i("FrgRank", "onItemClick");

        final ItemRank itemRank = listRankManager.get(p);

        switch (view.getId()) {
            case R.id.iButton_itemRank_visible:
                itemRank.setVisible(!itemRank.isVisible());

                listRankManager.set(p, itemRank);
                adapterRank.updateAdapter(p, itemRank);

                noteDB = new NoteDB(context);
                noteDB.updateRank(itemRank.getId(), itemRank.isVisible());
                noteDB.close();

                activity.listStatusManager.updateItemVisible(itemRank);
                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
            case R.id.layout_itemRank_click:
                final AlertRename alert = new AlertRename(context, R.style.AppTheme_AlertDialog);
                alert.setTitle(itemRank.getName())
                        .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                itemRank.setName(alert.getRename());

                                noteDB = new NoteDB(context);
                                noteDB.updateRank(itemRank.getId(), itemRank.getName());
                                noteDB.close();

                                listRankManager.set(p, itemRank);

                                adapterRank.updateAdapter(p, itemRank);
                                adapterRank.notifyItemChanged(p);

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

                alert.setTextChange(dialog, listRankManager.getListRankName());
                break;
            case R.id.iButton_itemRank_cancel:
                itemRank.setVisible(true);                  //Чтобы отобразить заметки в статус баре, если были скрыты

                noteDB = new NoteDB(context);
                noteDB.deleteRank(listRankManager.get(p).getName());   //Удаляем категорию из БД
                noteDB.updateRank(p);                           //Обновление позиций категорий
                noteDB.close();

                listRankManager.remove(p);

                adapterRank.updateAdapter(listRankManager.getListRank());
                adapterRank.notifyItemRemoved(p);

                activity.listStatusManager.updateItemVisible(itemRank);
                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i("FrgRank", "onItemLongClick");

        boolean[] iconStartAnim = adapterRank.getIconStartAnim();
        boolean clickVisible = listRankManager.get(p).isVisible();

        for (int i = 0; i < listRankManager.size(); i++) {
            if (i != p) {
                ItemRank itemRank = listRankManager.get(i);
                boolean isVisible = itemRank.isVisible();

                if (clickVisible == isVisible) {
                    iconStartAnim[i] = true;
                    itemRank.setVisible(!isVisible);
                    listRankManager.set(i, itemRank);
                }
            }
        }

        List<ItemRank> listRank = listRankManager.getListRank();

        adapterRank.updateAdapter(listRank, iconStartAnim);
        adapterRank.notifyDataSetChanged();

        noteDB = new NoteDB(context);
        noteDB.updateRank(listRank);
        noteDB.close();

        activity.listStatusManager.updateItemVisible(listRank);
        activity.frgNotes.updateAdapter();
        activity.frgBin.updateAdapter();
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStartPs;
        private int dragEndPs;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = 0;
            int flagsSwipe = 0;

            if (dragState.isDrag()) flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    dragStartPs = viewHolder.getAdapterPosition();
                    break;
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragEndPs = viewHolder.getAdapterPosition();
            if (dragStartPs != dragEndPs) {
                noteDB = new NoteDB(context);
                noteDB.updateRank(dragStartPs, dragEndPs);      //Обновляем позиции категорий от начальной и до конечной
                listRankManager.setListRank(noteDB.getRank());  //И получаем обновлённый массив с категориями
                activity.listStatusManager = noteDB.getListStatusManager();
                noteDB.close();

                adapterRank.updateAdapter(listRankManager.getListRank());
                adapterRank.notifyDataSetChanged();

                activity.frgNotes.updateAdapter();
                activity.frgBin.updateAdapter();
            }
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();        //Старая позиция (откуда взяли)
            int newPs = target.getAdapterPosition();            //Новая позиция (куда отпустили)

            listRankManager.move(oldPs, newPs);

            adapterRank.updateAdapter(listRankManager.getListRank());
            adapterRank.notifyItemMoved(oldPs, newPs);

            return true;
        }
    };

}