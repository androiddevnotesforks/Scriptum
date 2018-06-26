package sgtmelon.handynotes.app.ui.frg;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
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

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterRank;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.databinding.FrgMRankBinding;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.manager.ManagerRank;
import sgtmelon.handynotes.app.model.state.StateDrag;
import sgtmelon.handynotes.databinding.FrgRankBinding;
import sgtmelon.handynotes.office.intf.ItemClick;
import sgtmelon.handynotes.app.ui.act.ActMain;
import sgtmelon.handynotes.view.alert.AlertRename;

public class FrgRank extends Fragment implements ItemClick.Click, ItemClick.LongClick,
        View.OnClickListener, View.OnLongClickListener {

    //region Variable
    private final String TAG = "FrgRank";

    private DataRoom db;

    private View frgView;
    private Context context;
    private ActMain activity;

    private FrgRankBinding binding;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter(true);

        managerRank.tintButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.frg_rank, container, false);

        frgView = binding.getRoot();

        context = getContext();
        activity = (ActMain) getActivity();

        setupToolbar();
        setupRecyclerView();

        return frgView;
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    public ManagerRank managerRank;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

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
        Log.i(TAG, "onClick");

        switch (view.getId()) {
            case R.id.incToolbarRank_ib_cancel:
                managerRank.clearEnter();
                break;
            case R.id.incToolbarRank_ib_add:
                int rankPs = managerRank.size();
                String rankNm = managerRank.clearEnter();                           //Сбрасываем поле ввода

                ItemRank itemRank = new ItemRank(rankPs, rankNm);

                db = DataRoom.provideDb(context);
                int rankId = (int) db.daoRank().insert(itemRank);
                db.close();

                itemRank.setId(rankId);

                managerRank.add(rankPs, itemRank);
                adapterRank.updateAdapter(managerRank.getListRank());

                if (managerRank.size() == 1) {
                    bind(managerRank.size());
                    adapterRank.notifyItemInserted(rankPs);
                } else {
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
        Log.i(TAG, "onLongClick");

        int rankPs = 0;
        String rankNm = managerRank.clearEnter();                         //Сбрасываем поле ввода

        ItemRank itemRank = new ItemRank(rankPs - 1, rankNm);

        db = DataRoom.provideDb(context);
        int rankId = (int) db.daoRank().insert(itemRank);
        db.daoRank().update(rankPs);
        db.close();

        itemRank.setId(rankId);

        managerRank.add(rankPs, itemRank);
        adapterRank.updateAdapter(managerRank.getListRank());

        if (managerRank.size() == 1) bind(managerRank.size());
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
        Log.i(TAG, "setupRecyclerView");

        stateDrag = new StateDrag();

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                bind(managerRank.size());
            }
        };

        recyclerView = frgView.findViewById(R.id.frgRank_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapterRank = new AdapterRank();
        recyclerView.setAdapter(adapterRank);

        adapterRank.setCallback(this, this, stateDrag);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void updateAdapter(boolean updateAll) {
        Log.i(TAG, "updateAdapter");

        db = DataRoom.provideDb(context);
        managerRank.setListRank(db.daoRank().get());
        if (updateAll) managerRank.setListRankName(db.daoRank().getNameUp());
        db.close();

        adapterRank.updateAdapter(managerRank.getListRank());
        adapterRank.notifyDataSetChanged();

        bind(managerRank.size());
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i(TAG, "onItemClick");

        final ItemRank itemRank = managerRank.get(p);

        switch (view.getId()) {
            case R.id.itemRank_bv_visible:
                itemRank.setVisible(!itemRank.isVisible());

                managerRank.set(p, itemRank);
                adapterRank.updateAdapter(p, itemRank);

                db = DataRoom.provideDb(context);
                db.daoRank().update(itemRank);
                db.close();

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

                                db = DataRoom.provideDb(context);
                                db.daoRank().update(itemRank);
                                db.close();

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

                db = DataRoom.provideDb(context);
                db.daoRank().delete(managerRank.get(p).getName());
                db.daoRank().update(p);
                db.close();

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
        Log.i(TAG, "onItemLongClick");

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

        db = DataRoom.provideDb(context);
        db.daoRank().updateRank(listRank);
        db.close();

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
                db = DataRoom.provideDb(context);
                db.daoRank().update(dragStartPs, dragEndPs);
                managerRank.setListRank(db.daoRank().get());
                activity.managerStatus = db.daoNote().getManagerStatus(context);
                db.close();

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