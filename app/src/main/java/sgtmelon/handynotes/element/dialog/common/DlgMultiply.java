package sgtmelon.handynotes.element.dialog.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgMultiply extends BlankDialog {

    public void setArguments(boolean[] check) {
        Bundle arg = new Bundle();
        arg.putBooleanArray(Dlg.VALUE, check);
        setArguments(arg);
    }

    private String[] name;
    private boolean[] check;

    public void setName(String[] name) {
        this.name = name;
    }

    public boolean[] getCheck() {
        return check;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            check = savedInstanceState.getBooleanArray(Dlg.VALUE);
        } else if (arg != null) {
            check = arg.getBooleanArray(Dlg.VALUE);
        }

        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(title)
                .setMultiChoiceItems(name, check, (dialog, which, isChecked) -> check[which] = isChecked)
                .setPositiveButton(getString(R.string.dialog_btn_accept), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(Dlg.VALUE, check);
    }

}
