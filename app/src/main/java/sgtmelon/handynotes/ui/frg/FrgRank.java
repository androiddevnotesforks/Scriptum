package sgtmelon.handynotes.ui.frg;

import android.arch.persistence.room.Room;
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
import sgtmelon.handynotes.database.DataBaseRoom;
import sgtmelon.handynotes.interfaces.InfoPageReply;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.model.manager.ManagerRank;
import sgtmelon.handynotes.model.state.StateDrag;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.service.InfoPageEmpty;
import sgtmelon.handynotes.ui.act.ActMain;
import sgtmelon.handynotes.view.alert.AlertRename;

public class FrgRank extends Fragment implements ItemClick.Click, ItemClick.LongClick,
        View.OnClickListener, View.OnLongClickListener, InfoPageReply {

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FrgRank", "onResume");

        updateAdapter(true);

        managerRank.tintButton();
    }

    //region Variables
//    private NoteDB noteDB;
    private DataBaseRoom db;

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

        LinearLayout info = frgView.findViewById(R.id.frgRank_ll_empty);
        infoPageEmpty = new InfoPageEmpty(context, info);
        infoPageEmpty.setInfoPageReply(this);

        return frgView;
    }

    public ManagerRank managerRank;

    private void setupToolbar() {
        Log.i("FrgRank", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_rank));

        ImageButton rankCancel = frgView.findViewById(R.id.incToolbarRank_ib_cancel);
        ImageButton rankAdd = frgView.findViewById(R.id.incToolbarRank_ib_add);
        EditText rankEnter = frgView.findViewById(R.id.incToolbarRank_et_enter);

        managerRank = new ManagerRank(context);

        managerRank.setOnClickListener(this);
        managerRank.setControl(rankCancel, rankAdd, rankEnter);

        rankEnter.addTextChangedListener(managerRank);
        rankEnter.setOnEditorActionListener(managerRank);

        rankCancel.setOnClickListener(this);
        rankAdd.setOnClickListener(this);
        rankAdd.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i("FrgRank", "onClick");

        switch (view.getId()) {
            case R.id.incToolbarRank_ib_cancel:
                managerRank.clearEnter();
                break;
            case R.id.incToolbarRank_ib_add:
                int rankPs = managerRank.size();
                String rankNm = managerRank.clearEnter();                           //Сбрасываем поле ввода

                ItemRank itemRank = new ItemRank(rankPs, rankNm);

                db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                        .allowMainThreadQueries()
                        .build();

                int rankId = (int) db.daoRank().insertRank(itemRank);

                db.close();

                itemRank.setId(rankId);

//                noteDB = new NoteDB(context);
//                int rankId = noteDB.insertRank(rankPs, rankNm);                         //Добавляем заметку (с позицией равной количеству элементов)
//                noteDB.close();
//
//                ItemRank itemRank = new ItemRank(rankId, rankPs, rankNm);
                managerRank.add(rankPs, itemRank);
                adapterRank.updateAdapter(managerRank.getListRank());

                if (managerRank.size() == 1) infoPageEmpty.hide();
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
        String rankNm = managerRank.clearEnter();                         //Сбрасываем поле ввода

        ItemRank itemRank = new ItemRank(rankPs - 1, rankNm);

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        int rankId = (int) db.daoRank().insertRank(itemRank);
        db.daoRank().updateRank(rankPs);

        db.close();

        itemRank.setId(rankId);

//        NoteDB noteDB = new NoteDB(context);
//        int rankId = noteDB.insertRank(rankPs - 1, rankNm);        //Добавляем заметку (с позицией -1)
//        noteDB.updateRank(rankPs);                                          //Затем обновляем позиции с самого начала
//        noteDB.close();

//        ItemRank itemRank = new ItemRank(rankId, rankPs, rankNm);
        managerRank.add(rankPs, itemRank);
        adapterRank.updateAdapter(managerRank.getListRank());

        if (managerRank.size() == 1) infoPageEmpty.hide();
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
    private StateDrag stateDrag;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private AdapterRank adapterRank;
    //endregion

    private void setupRecyclerView() {
        Log.i("FrgRank", "setupRecyclerView");

        stateDrag = new StateDrag();

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                infoPageEmpty.setVisible(true, managerRank.size());
            }
        };

        recyclerView = frgView.findViewById(R.id.frgRank_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapterRank = new AdapterRank(context);
        recyclerView.setAdapter(adapterRank);

        adapterRank.setCallback(this, this, stateDrag);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter(boolean updateAll) {
        Log.i("FrgRank", "updateAdapter");

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        managerRank.setListRank(db.daoRank().getRank());
        if(updateAll) managerRank.setListRankName(db.daoRank().getRankNameUpper());

        db.close();

//        noteDB = new NoteDB(context);
//        managerRank.setListRank(noteDB.getRank());
//        if (updateAll) managerRank.setListRankName(noteDB.getRankName());
//        noteDB.close();

        adapterRank.updateAdapter(managerRank.getListRank());
        adapterRank.notifyDataSetChanged();

        infoPageEmpty.setVisible(false, managerRank.size());
    }

    @Override
    public void notifyInsert(int position) {
        adapterRank.notifyItemInserted(position);
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i("FrgRank", "onItemClick");

        final ItemRank itemRank = managerRank.get(p);

        switch (view.getId()) {
            case R.id.itemRank_bv_visible:
                itemRank.setVisible(!itemRank.isVisible());

                managerRank.set(p, itemRank);
                adapterRank.updateAdapter(p, itemRank);

                db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                        .allowMainThreadQueries()
                        .build();

                db.daoRank().updateRank(itemRank);

                db.close();

//                noteDB = new NoteDB(context);
//                noteDB.updateRank(itemRank.getId(), itemRank.isVisible());
//                noteDB.close();

                activity.managerStatus.updateItemVisible(itemRank);
                activity.frgNote.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
            case R.id.itemRank_ll_click:
                final AlertRename alert = new AlertRename(context, R.style.AppTheme_AlertDialog);
                alert.setTitle(itemRank.getName())
                        .setPositiveButton(getString(R.string.dialog_btn_accept), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                itemRank.setName(alert.getRename());

                                db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                                        .allowMainThreadQueries()
                                        .build();

                                db.daoRank().updateRank(itemRank);

                                db.close();

//                                noteDB = new NoteDB(context);
//                                noteDB.updateRank(itemRank.getId(), itemRank.getName());
//                                noteDB.close();

                                managerRank.set(p, itemRank);

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

                alert.setTextChange(dialog, managerRank.getListRankName());
                break;
            case R.id.itemRank_ib_cancel:
                itemRank.setVisible(true);                  //Чтобы отобразить заметки в статус баре, если были скрыты

                db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                        .allowMainThreadQueries()
                        .build();

                db.daoRank().deleteRank(managerRank.get(p).getName());
                db.daoRank().updateRank(p);

                db.close();

//                noteDB = new NoteDB(context);
//                noteDB.deleteRank(managerRank.get(p).getName());   //Удаляем категорию из БД
//                noteDB.updateRank(p);                           //Обновление позиций категорий
//                noteDB.close();

                managerRank.remove(p);

                adapterRank.updateAdapter(managerRank.getListRank());
                adapterRank.notifyItemRemoved(p);

                activity.managerStatus.updateItemVisible(itemRank);
                activity.frgNote.updateAdapter();
                activity.frgBin.updateAdapter();
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i("FrgRank", "onItemLongClick");

        boolean[] iconStartAnim = adapterRank.getIconStartAnim();
        boolean clickVisible = managerRank.get(p).isVisible();

        for (int i = 0; i < managerRank.size(); i++) {
            if (i != p) {
                ItemRank itemRank = managerRank.get(i);
                boolean isVisible = itemRank.isVisible();

                if (clickVisible == isVisible) {
                    iconStartAnim[i] = true;
                    itemRank.setVisible(!isVisible);
                    managerRank.set(i, itemRank);
                }
            }
        }

        List<ItemRank> listRank = managerRank.getListRank();

        adapterRank.updateAdapter(listRank, iconStartAnim);
        adapterRank.notifyDataSetChanged();

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoRank().updateRank(listRank);

        db.close();

//        noteDB = new NoteDB(context);
//        noteDB.updateRank(listRank);
//        noteDB.close();

        activity.managerStatus.updateItemVisible(listRank);
        activity.frgNote.updateAdapter();
        activity.frgBin.updateAdapter();
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStartPs;
        private int dragEndPs;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = 0;
            int flagsSwipe = 0;

            if (stateDrag.isDrag()) flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

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
                db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                        .allowMainThreadQueries()
                        .build();

                db.daoRank().updateRank(dragStartPs, dragEndPs);
                managerRank.setListRank(db.daoRank().getRank());
                activity.managerStatus = db.daoNote().getManagerStatus(context);

                db.close();

//                noteDB = new NoteDB(context);
//                noteDB.updateRank(dragStartPs, dragEndPs);      //Обновляем позиции категорий от начальной и до конечной
//                managerRank.setListRank(noteDB.getRank());  //И получаем обновлённый массив с категориями
//                activity.managerStatus = noteDB.getListStatusManager();
//                noteDB.close();

                adapterRank.updateAdapter(managerRank.getListRank());
                adapterRank.notifyDataSetChanged();

                activity.frgNote.updateAdapter();
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

            managerRank.move(oldPs, newPs);

            adapterRank.updateAdapter(managerRank.getListRank());
            adapterRank.notifyItemMoved(oldPs, newPs);

            return true;
        }
    };

}