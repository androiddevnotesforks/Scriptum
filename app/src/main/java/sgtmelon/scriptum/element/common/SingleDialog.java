package sgtmelon.scriptum.element.common;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.blank.DialogBlank;

public final class SingleDialog extends DialogBlank {

    private String[] name;
    private int init, check;

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

        outState.putInt(DialogDef.INIT, init);
        outState.putInt(DialogDef.VALUE, check);
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
