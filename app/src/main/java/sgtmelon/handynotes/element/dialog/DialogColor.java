package sgtmelon.handynotes.element.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdpColor;
import sgtmelon.handynotes.office.annot.Dlg;

public class DialogColor extends DialogFragment
        implements DialogInterface.OnClickListener {

    public void setArguments(int check) {
        Bundle arg = new Bundle();
        arg.putInt(Dlg.CHECK, check);
        setArguments(arg);
    }

    private int check;
    private AdpColor adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            check = savedInstanceState.getInt(Dlg.CHECK);
        } else if (arg != null) {
            check = arg.getInt(Dlg.CHECK);
        }

        RecyclerView recyclerView = new RecyclerView(context);

        int padding = context.getResources().getInteger(R.integer.alert_recycler_view_padding);
        recyclerView.setPadding(padding, padding, padding, padding);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        GridLayoutManager layoutManager = new GridLayoutManager(context, context.getResources().getInteger(R.integer.alert_color_column_count));
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdpColor(context, check);
        recyclerView.setAdapter(adapter);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.dialog_title_color))
                .setView(recyclerView)
                .setPositiveButton(getString(R.string.dialog_btn_accept), this)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    private DialogInterface.OnClickListener positiveButton;

    public void setPositiveButton(DialogInterface.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        positiveButton.onClick(dialogInterface, adapter.getCheck());
        dialogInterface.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Dlg.CHECK, adapter.getCheck());
    }

}
