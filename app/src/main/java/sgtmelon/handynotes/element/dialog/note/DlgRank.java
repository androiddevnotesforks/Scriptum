package sgtmelon.handynotes.element.dialog.note;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgRank extends BlankDialog {

    public void setArguments(String[] name, boolean[] check) {
        Bundle arg = new Bundle();
        arg.putStringArray(Db.RK_NM, name);
        arg.putBooleanArray(Dlg.VALUE, check);
        setArguments(arg);
    }

    private String[] name;
    private boolean[] check;

    public boolean[] getCheck() {
        return check;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            name = savedInstanceState.getStringArray(Db.RK_NM);
            check = savedInstanceState.getBooleanArray(Dlg.VALUE);
        } else if (arg != null) {
            name = arg.getStringArray(Db.RK_NM);
            check = arg.getBooleanArray(Dlg.VALUE);
        }

        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setMultiChoiceItems(name, check, (dialog, which, isChecked) -> check[which] = isChecked)
                .setPositiveButton(getString(R.string.dialog_btn_accept), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray(Db.RK_NM, name);
        outState.putBooleanArray(Dlg.VALUE, check);
    }

}
