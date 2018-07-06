package sgtmelon.handynotes.element.alert;

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
import sgtmelon.handynotes.app.adapter.AdpSort;
import sgtmelon.handynotes.app.model.item.ItemSort;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.DefSort;
import sgtmelon.handynotes.office.intf.IntfItem;

public class AlertSort extends AlertDialog.Builder implements IntfItem.Click {

    //region Variables
    private final Context context;
    private final String keys;

    private final String[] textSort;
    private List<ItemSort> listSort;
    //endregion

    @SuppressWarnings("unused")
    public AlertSort(@NonNull Context context, String keys) {
        super(context);

        this.context = context;
        this.keys = keys;

        textSort = context.getResources().getStringArray(R.array.pref_text_sort);

        setupRecycler();
    }

    public AlertSort(@NonNull Context context, String keys, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;
        this.keys = keys;

        textSort = context.getResources().getStringArray(R.array.pref_text_sort);

        setupRecycler();
    }

    private AdpSort adapter;

    private void setupRecycler() {
        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.alert_recycler_view_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdpSort();
        recyclerView.setAdapter(adapter);
        adapter.setCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        setView(recyclerView);

        listSort = new ArrayList<>();
        String[] keysArr = keys.split(DefSort.divider);
        for (String aKey : keysArr) {
            @DefSort int key = Integer.parseInt(aKey);
            ItemSort itemSort = new ItemSort(textSort[key], key);
            listSort.add(itemSort);
        }

        adapter.update(listSort);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int p) {
        ItemSort itemSort = listSort.get(p);

        @DefSort int key = itemSort.getKey() == DefSort.create ?
                DefSort.change : DefSort.create;

        itemSort.setText(textSort[key]);
        itemSort.setKey(key);

        listSort.set(p, itemSort);
        adapter.update(p, itemSort);
        adapter.notifyItemChanged(p);
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

            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
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

            adapter.update(listSort);
            adapter.notifyItemMoved(oldPs, newPs);

            if (oldPs == adapter.stSort.getEnd()) {
                adapter.notifyItemChanged(newPs);
            } else {
                adapter.notifyItemChanged(oldPs);
            }

            return true;
        }
    };

    public String getKeys() {
        return Help.Pref.getSortByList(listSort);
    }
}