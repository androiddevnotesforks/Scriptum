package sgtmelon.handynotes.element.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpSort;
import sgtmelon.handynotes.app.model.item.ItemSort;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.annot.def.DefSort;
import sgtmelon.handynotes.office.blank.BlankDialog;
import sgtmelon.handynotes.office.intf.IntfItem;

public class DlgSort extends BlankDialog
        implements IntfItem.Click {

    public void setArguments(String keys) {
        Bundle arg = new Bundle();
        arg.putString(Dlg.VALUE, keys);
        setArguments(arg);
    }

    private String keys;

    public String getKeys() {
        return Help.Pref.getSortByList(listSort);
    }

    private String[] text;
    private List<ItemSort> listSort;

    private AdpSort adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            keys = savedInstanceState.getString(Dlg.VALUE);
        } else if (arg != null) {
            keys = arg.getString(Dlg.VALUE);
        }

        text = getResources().getStringArray(R.array.pref_text_sort);

        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.dlg_recycler_padding);
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

        listSort = new ArrayList<>();
        String[] keysArr = keys.split(DefSort.divider);
        for (String aKey : keysArr) {
            @DefSort int key = Integer.parseInt(aKey);
            ItemSort itemSort = new ItemSort(text[key], key);
            listSort.add(itemSort);
        }

        adapter.update(listSort);
        adapter.notifyDataSetChanged();

        return new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_title_sort))
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setNeutralButton(getString(R.string.dialog_btn_reset), neutralClick)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onItemClick(View view, int p) {
        ItemSort itemSort = listSort.get(p);

        @DefSort int key = itemSort.getKey() == DefSort.create
                ? DefSort.change
                : DefSort.create;

        itemSort.setText(text[key]);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(Dlg.VALUE, getKeys());
    }
}
