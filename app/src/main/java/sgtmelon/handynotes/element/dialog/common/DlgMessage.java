package sgtmelon.handynotes.element.dialog.common;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgMessage extends BlankDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_btn_yes), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

}
