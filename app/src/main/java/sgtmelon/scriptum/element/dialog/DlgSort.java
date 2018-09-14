package sgtmelon.scriptum.element.dialog;

import android.app.Dialog;
import android.content.Context;
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
import sgtmelon.scriptum.app.adapter.AdpSort;
import sgtmelon.scriptum.app.model.item.ItemSort;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefSort;
import sgtmelon.scriptum.office.blank.BlankDlg;
import sgtmelon.scriptum.office.intf.IntfItem;

public class DlgSort extends BlankDlg implements IntfItem.Click {

    public void setArguments(String keys) {
        Bundle arg = new Bundle();
        arg.putString(DefDlg.INIT, keys);
        arg.putString(DefDlg.VALUE, keys);
        setArguments(arg);
    }

    private String init, keys;

    public String getKeys() {
        return keys;
    }

    private String[] text;
    private List<ItemSort> listSort;

    private AdpSort adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getString(DefDlg.INIT);
            keys = savedInstanceState.getString(DefDlg.VALUE);
        } else if (arg != null) {
            init = arg.getString(DefDlg.INIT);
            keys = arg.getString(DefDlg.VALUE);
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

        SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) animator.setSupportsChangeAnimations(false);

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
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setNeutralButton(getString(R.string.dialog_btn_reset), onNeutralClick)
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

        keys = Help.Pref.getSortByList(listSort);
        setEnable();
    }

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

            keys = Help.Pref.getSortByList(listSort);
            setEnable();
            return true;
        }
    };

    @Override
    protected void setEnable() {
        super.setEnable();

        if (Help.Pref.getSortEqual(init, keys)) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);

        if (Help.Pref.getSortEqual(DefSort.def, keys)) buttonNeutral.setEnabled(false);
        else buttonNeutral.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(DefDlg.INIT, init);
        outState.putString(DefDlg.VALUE, keys);
    }
}
