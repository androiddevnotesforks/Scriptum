package sgtmelon.handynotes.office.blank;

import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public abstract class BlankDialog extends DialogFragment {

    protected String title, message;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private DialogInterface.OnClickListener positiveButton;
    private DialogInterface.OnClickListener neutralButton;

    public void setPositiveButton(DialogInterface.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    public void setNeutralButton(DialogInterface.OnClickListener neutralButton) {
        this.neutralButton = neutralButton;
    }

    protected final DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            positiveButton.onClick(dialogInterface, i);
            dialogInterface.cancel();
        }
    };

    protected final DialogInterface.OnClickListener neutralClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            neutralButton.onClick(dialogInterface, i);
            dialogInterface.cancel();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        setEnable();
    }

    protected AlertDialog dialog;
    protected Button buttonPositive;

    protected void setEnable() {
        if (dialog == null) dialog = (AlertDialog) getDialog();
        if (buttonPositive == null) {
            buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        }
    }

}
