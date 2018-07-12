package sgtmelon.handynotes.element.dialog.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.handynotes.R;

public class DialogClearBin extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog)
                .setTitle(getString(R.string.dialog_title_clear_bin))
                .setMessage(getString(R.string.dialog_text_clear_bin))
                .setPositiveButton(getString(R.string.dialog_btn_yes), this)
                .setNegativeButton(getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel())
                .setCancelable(true)
                .create();
    }

    private DialogInterface.OnClickListener positiveButton;

    public void setPositiveButton(DialogInterface.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        positiveButton.onClick(dialogInterface, i);
        dialogInterface.cancel();
    }
}
