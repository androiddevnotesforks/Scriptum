package sgtmelon.scriptum.element;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.SortAdapter;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.blank.DialogBlank;
import sgtmelon.scriptum.office.intf.ItemIntf;

public final class SortDialog extends DialogBlank implements ItemIntf.Click {

    private String init, keys;
    private String[] text;

    private List<SortItem> listSort;
    private SortAdapter adapter;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int flagsSwipe = 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();
            int newPs = target.getAdapterPosition();

            SortItem sortItem = listSort.get(oldPs);

            listSort.remove(oldPs);
            listSort.add(newPs, sortItem);

            adapter.setListSort(listSort);
            adapter.notifyItemMoved(oldPs, newPs);

            if (oldPs == adapter.sortSt.getEnd()) {
                adapter.notifyItemChanged(newPs);
            } else {
                adapter.notifyItemChanged(oldPs);
            }

            keys = Help.Pref.getSortByList(listSort);
            setEnable();
            return true;
        }
    };

    public void setArguments(String keys) {
        Bundle arg = new Bundle();

        arg.putString(DialogDef.INIT, keys);
        arg.putString(DialogDef.VALUE, keys);

        setArguments(arg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (arg != null) {
            init = arg.getString(DialogDef.INIT);
            keys = arg.getString(DialogDef.VALUE);
        } else if (savedInstanceState != null) {
            init = savedInstanceState.getString(DialogDef.INIT);
            keys = savedInstanceState.getString(DialogDef.VALUE);
        }

        text = getResources().getStringArray(R.array.pref_sort_text);

        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.dlg_recycler_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SortAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setClick(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) animator.setSupportsChangeAnimations(false);

        listSort = new ArrayList<>();
        String[] keysArr = keys.split(SortDef.divider);
        for (String aKey : keysArr) {
            @SortDef int key = Integer.parseInt(aKey);
            SortItem sortItem = new SortItem(text[key], key);
            listSort.add(sortItem);
        }

        adapter.setListSort(listSort);
        adapter.notifyDataSetChanged();

        return new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_title_sort))
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setNeutralButton(getString(R.string.dialog_btn_reset), onNeutralClick)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(DialogDef.INIT, init);
        outState.putString(DialogDef.VALUE, keys);
    }

    public String getKeys() {
        return keys;
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        if (Help.Pref.getSortEqual(init, keys)) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);

        if (Help.Pref.getSortEqual(SortDef.def, keys)) buttonNeutral.setEnabled(false);
        else buttonNeutral.setEnabled(true);
    }

    @Override
    public void onItemClick(View view, int p) {
        SortItem sortItem = listSort.get(p);

        @SortDef int key = sortItem.getKey() == SortDef.create
                ? SortDef.change
                : SortDef.create;

        sortItem.setText(text[key]);
        sortItem.setKey(key);

        listSort.set(p, sortItem);
        adapter.setListSort(p, sortItem);
        adapter.notifyItemChanged(p);

        keys = Help.Pref.getSortByList(listSort);
        setEnable();
    }

}
