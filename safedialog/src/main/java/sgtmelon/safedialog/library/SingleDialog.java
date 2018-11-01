package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;

public final class SingleDialog extends DialogBlank {

    private String[] name;
    private int init, check;

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

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setSingleChoiceItems(name, check, (dialogInterface, i) -> {
                    check = i;
                    setEnable();
                })
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

    public String[] getRows() {
        return name;
    }

    public void setRows(String[] name) {
        this.name = name;
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

}
