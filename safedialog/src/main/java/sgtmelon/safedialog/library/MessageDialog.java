package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import sgtmelon.safedialog.R;
import sgtmelon.safedialog.office.blank.DialogBlank;

public final class MessageDialog extends DialogBlank {

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
