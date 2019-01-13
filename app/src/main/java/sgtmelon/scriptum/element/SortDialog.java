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
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.SortAdapter;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.def.SortDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.utils.PrefUtils;

public final class SortDialog extends DialogBlank implements ItemIntf.ClickListener {

    // TODO: 07.10.2018 От частного к общему

    private final List<SortItem> listSort = new ArrayList<>();

    private String init, keys;
    private String[] text;
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
            final int positionFrom = viewHolder.getAdapterPosition();
            final int positionTo = target.getAdapterPosition();

            final SortItem sortItem = listSort.get(positionFrom);

            listSort.remove(positionFrom);
            listSort.add(positionTo, sortItem);

            adapter.setList(listSort);
            adapter.notifyItemMoved(positionFrom, positionTo);

            adapter.notifyItemChanged(positionFrom == adapter.sortSt.getEnd()
                    ? positionTo
                    : positionFrom);

            keys = PrefUtils.getSortByList(listSort);
            setEnable();
            return true;
        }
    };

    public void setArguments(String keys) {
        final Bundle bundle = new Bundle();

        bundle.putString(DialogAnn.INIT, keys);
        bundle.putString(DialogAnn.VALUE, keys);

        setArguments(bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();
        if (savedInstanceState != null) {
            init = savedInstanceState.getString(DialogAnn.INIT);
            keys = savedInstanceState.getString(DialogAnn.VALUE);
        } else if (bundle != null) {
            init = bundle.getString(DialogAnn.INIT);
            keys = bundle.getString(DialogAnn.VALUE);
        }

        text = getResources().getStringArray(R.array.pref_sort_text);

        final RecyclerView recyclerView = new RecyclerView(context);

        final int padding = context.getResources().getInteger(R.integer.dlg_recycler_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new SortAdapter(context);
        adapter.setClickListener(this);

        listSort.clear();
        for (String aKey : keys.split(SortDef.divider)) {
            final int key = Integer.parseInt(aKey);
            final SortItem sortItem = new SortItem(text[key], key);
            listSort.add(sortItem);
        }
        adapter.setList(listSort);

        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        final SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }

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

        outState.putString(DialogAnn.INIT, init);
        outState.putString(DialogAnn.VALUE, keys);
    }

    public String getKeys() {
        return keys;
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        buttonPositive.setEnabled(!PrefUtils.getSortEqual(init, keys));
        buttonNeutral.setEnabled(!PrefUtils.getSortEqual(SortDef.def, keys));
    }

    @Override
    public void onItemClick(View view, int p) {
        final SortItem sortItem = listSort.get(p);

        final int key = sortItem.getKey() == SortDef.create
                ? SortDef.change
                : SortDef.create;

        sortItem.setText(text[key]);
        sortItem.setKey(key);

        adapter.setListItem(p, sortItem);
        adapter.notifyItemChanged(p);

        keys = PrefUtils.getSortByList(listSort);
        setEnable();
    }

}
