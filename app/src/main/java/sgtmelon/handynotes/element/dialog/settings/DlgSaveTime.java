package sgtmelon.handynotes.element.dialog.settings;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgSaveTime extends BlankDialog {

    public void setArguments(int check){
        Bundle arg = new Bundle();
        arg.putInt(Dlg.VALUE, check);
        setArguments(arg);
    }

    private String[] name;
    private int check;

    public String[] getName() {
        return name;
    }

    public int getCheck() {
        return check;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            check = savedInstanceState.getInt(Dlg.VALUE);
        } else if (arg != null) {
            check = arg.getInt(Dlg.VALUE);
        }

        name = getResources().getStringArray(R.array.pref_text_save_time);

        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.pref_title_auto_save_time))
                .setSingleChoiceItems(name, check, (dialogInterface, i) -> check = i)
                .setPositiveButton(getString(R.string.dialog_btn_accept), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Dlg.VALUE, check);
    }
}
