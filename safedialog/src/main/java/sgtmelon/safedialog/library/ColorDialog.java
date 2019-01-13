package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.library.adapter.ColorAdapter;
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;
import sgtmelon.safedialog.office.intf.ColorIntf;

public final class ColorDialog extends DialogBlank implements ColorIntf.ClickListener {

    private int init, check;

    @IdRes private int[] fillColor, strokeColor, checkColor;
    private int columnCount;

    public void setArguments(int check) {
        final Bundle bundle = new Bundle();

        bundle.putInt(DialogAnn.INIT, check);
        bundle.putInt(DialogAnn.VALUE, check);

        setArguments(bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(DialogAnn.INIT);
            check = savedInstanceState.getInt(DialogAnn.VALUE);
        } else if (bundle != null) {
            init = bundle.getInt(DialogAnn.INIT);
            check = bundle.getInt(DialogAnn.VALUE);
        }

        if (fillColor == null || strokeColor == null || checkColor == null){
            throw new NullPointerException("One of color arrays is null");
        }

        if (fillColor.length != strokeColor.length && strokeColor.length != checkColor.length){
            throw new ArrayIndexOutOfBoundsException("Color arrays should have equal length");
        }

        final RecyclerView recyclerView = new RecyclerView(context);

        final int padding = 24;
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));

        final ColorAdapter adapter = new ColorAdapter(context, fillColor.length);
        adapter.setFillColor(fillColor);
        adapter.setStrokeColor(strokeColor);
        adapter.setCheckColor(checkColor);
        adapter.setCheck(check);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);

        final SimpleItemAnimator animator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (animator != null) {
            animator.setSupportsChangeAnimations(false);
        }

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

        outState.putInt(DialogAnn.INIT, init);
        outState.putInt(DialogAnn.VALUE, check);
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public void setFillColor(int[] fillColor) {
        this.fillColor = fillColor;
    }

    public void setStrokeColor(int[] strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setCheckColor(int[] checkColor) {
        this.checkColor = checkColor;
    }

    public int getCheck() {
        return check;
    }

    @Override
    protected void setEnable() {
        super.setEnable();
        buttonPositive.setEnabled(init != check);
    }

    @Override
    public void onColorClick(int p) {
        check = p;
        setEnable();
    }
}
