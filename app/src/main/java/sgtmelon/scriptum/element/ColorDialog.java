package sgtmelon.scriptum.element;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.ColorAdapter;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.blank.DialogBlank;
import sgtmelon.scriptum.office.intf.ItemIntf;

public final class ColorDialog extends DialogBlank implements ItemIntf.Click {

    private int init, check;

    private int[] icons, colors;

    public void setArguments(int check) {
        Bundle arg = new Bundle();

        arg.putInt(DialogDef.INIT, check);
        arg.putInt(DialogDef.VALUE, check);

        setArguments(arg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(DialogDef.INIT);
            check = savedInstanceState.getInt(DialogDef.VALUE);
        } else if (arg != null) {
            init = arg.getInt(DialogDef.INIT);
            check = arg.getInt(DialogDef.VALUE);
        }

        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.dlg_recycler_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        GridLayoutManager layoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.dlg_color_column));
        recyclerView.setLayoutManager(layoutManager);

        ColorAdapter adapter = new ColorAdapter(context, check, icons, colors);
        adapter.setClick(this);
        recyclerView.setAdapter(adapter);

        SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) animator.setSupportsChangeAnimations(false);

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    public void setIcons(int[] icons) {
        this.icons = icons;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DialogDef.INIT, init);
        outState.putInt(DialogDef.VALUE, check);
    }

    public int getCheck() {
        return check;
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        if (init == check) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);
    }

    @Override
    public void onItemClick(View view, int p) {
        check = p;
        setEnable();
    }
}
