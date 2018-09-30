package sgtmelon.scriptum.element.dialog.common;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.blank.BlankDlg;

public class DlgMessage extends BlankDlg {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_btn_yes), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

}
