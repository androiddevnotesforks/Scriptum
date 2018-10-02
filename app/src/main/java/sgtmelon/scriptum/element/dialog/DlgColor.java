package sgtmelon.scriptum.element.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.AdapterColor;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.blank.BlankDlg;
import sgtmelon.scriptum.office.intf.IntfItem;

public final class DlgColor extends BlankDlg implements IntfItem.Click {

    private int init, check;

    public void setArguments(int check) {
        Bundle arg = new Bundle();

        arg.putInt(DefDlg.INIT, check);
        arg.putInt(DefDlg.VALUE, check);

        setArguments(arg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(DefDlg.INIT);
            check = savedInstanceState.getInt(DefDlg.VALUE);
        } else if (arg != null) {
            init = arg.getInt(DefDlg.INIT);
            check = arg.getInt(DefDlg.VALUE);
        }

        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.dlg_recycler_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        GridLayoutManager layoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.dlg_color_column));
        recyclerView.setLayoutManager(layoutManager);

        AdapterColor adapter = new AdapterColor(context, check);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDlg.INIT, init);
        outState.putInt(DefDlg.VALUE, check);
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
