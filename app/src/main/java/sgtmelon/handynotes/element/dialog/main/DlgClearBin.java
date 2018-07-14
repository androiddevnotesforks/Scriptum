package sgtmelon.handynotes.element.dialog.main;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.blank.BlankDialog;

public class DlgClearBin extends BlankDialog {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.dialog_title_clear_bin))
                .setMessage(getString(R.string.dialog_text_clear_bin))
                .setPositiveButton(getString(R.string.dialog_btn_yes), positiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

}
