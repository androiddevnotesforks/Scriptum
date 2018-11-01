package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.os.Bundle;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.office.annot.DialogAnn;
import sgtmelon.safedialog.office.blank.DialogBlank;

public final class MultiplyDialog extends DialogBlank {

    private String[] name;
    private boolean[] init, check;

    public void setArguments(boolean[] check) {
        final Bundle bundle = new Bundle();

        bundle.putBooleanArray(DialogAnn.INIT, check.clone());
        bundle.putBooleanArray(DialogAnn.VALUE, check);

        setArguments(bundle);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle bundle = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getBooleanArray(DialogAnn.INIT);
            check = savedInstanceState.getBooleanArray(DialogAnn.VALUE);
        } else if (bundle != null) {
            init = bundle.getBooleanArray(DialogAnn.INIT);
            check = bundle.getBooleanArray(DialogAnn.VALUE);
        }

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMultiChoiceItems(name, check, (dialog, which, isChecked) -> {
                    check[which] = isChecked;
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

        outState.putBooleanArray(DialogAnn.INIT, init);
        outState.putBooleanArray(DialogAnn.VALUE, check);
    }

    public void setRows(String[] name) {
        this.name = name;
    }

    public boolean[] getCheck() {
        return check;
    }

    @Override
    protected void setEnable() {
        super.setEnable();

        if (Arrays.equals(init, check)) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);
    }

}
