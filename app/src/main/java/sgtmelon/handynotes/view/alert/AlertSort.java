package sgtmelon.handynotes.view.alert;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.adapter.AdapterSort;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.item.ItemSort;

public class AlertSort extends AlertDialog.Builder implements ItemClick.Click {

    //region Variables
    private final Context context;
    private final String sortKeys;
    private List<ItemSort> listSort;
    //endregion

    @SuppressWarnings("unused")
    public AlertSort(@NonNull Context context, String sortKeys) {
        super(context);

        this.context = context;
        this.sortKeys = sortKeys;

        setupRecycler();
    }

    public AlertSort(@NonNull Context context, String sortKeys, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;
        this.sortKeys = sortKeys;

        setupRecycler();
    }

    private AdapterSort adapterSort;

    private void setupRecycler() {
        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.alert_recycler_view_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterSort = new AdapterSort(context);
        recyclerView.setAdapter(adapterSort);
        adapterSort.setCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        setView(recyclerView);

        listSort = new ArrayList<>();
        String[] sortKeysArr = sortKeys.split(Help.Pref.divider);
        for (String aSortKey : sortKeysArr) {
            int key = Integer.parseInt(aSortKey);

            ItemSort itemSort = new ItemSort();
            itemSort.setText(context.getResources().getStringArray(R.array.pref_text_sort)[key]);
            itemSort.setKey(key);

            listSort.add(itemSort);
        }

        adapterSort.updateAdapter(listSort);
        adapterSort.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int p) {
        ItemSort itemSort = listSort.get(p);

        int key = itemSort.getKey();
        if (key == Help.Pref.sortCr) key = Help.Pref.sortCh;
        else key = Help.Pref.sortCr;

        itemSort.setText(context.getResources().getStringArray(R.array.pref_text_sort)[key]);
        itemSort.setKey(key);

        listSort.set(p, itemSort);
        adapterSort.updateAdapter(p, itemSort);
        adapterSort.notifyItemChanged(p);
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int flagsSwipe = 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            adapterSort.notifyItemChanged(viewHolder.getAdapterPosition());
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();
            int newPs = target.getAdapterPosition();

            ItemSort itemSort = listSort.get(oldPs);

            listSort.remove(oldPs);
            listSort.add(newPs, itemSort);

            adapterSort.updateAdapter(listSort);
            adapterSort.notifyItemMoved(oldPs, newPs);

            if (oldPs == adapterSort.sortState.getEnd()) {
                adapterSort.notifyItemChanged(newPs);
            } else adapterSort.notifyItemChanged(oldPs);

            return true;
        }
    };

    public String getSortKeys() {
        return Help.Pref.getSortByList(listSort);
    }
}