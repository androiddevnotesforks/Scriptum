package sgtmelon.handynotes.element.dialog.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgSingle extends BlankDialog {

    public void setArguments(int check) {
        Bundle arg = new Bundle();
        arg.putInt(Dlg.INIT, check);
        arg.putInt(Dlg.VALUE, check);
        setArguments(arg);
    }

    private String[] name;
    private int init, check;

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public int getCheck() {
        return check;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            init = savedInstanceState.getInt(Dlg.INIT);
            check = savedInstanceState.getInt(Dlg.VALUE);
        } else if (arg != null) {
            init = arg.getInt(Dlg.INIT);
            check = arg.getInt(Dlg.VALUE);
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
    protected void setEnable() {
        super.setEnable();

        if (init == check) buttonPositive.setEnabled(false);
        else buttonPositive.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Dlg.INIT, init);
        outState.putInt(Dlg.VALUE, check);
    }

}
