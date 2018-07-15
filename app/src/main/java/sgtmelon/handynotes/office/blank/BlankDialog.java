package sgtmelon.handynotes.office.blank;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

public class BlankDialog extends DialogFragment {

    protected String title, message;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private DialogInterface.OnClickListener positiveButton;

    public void setPositiveButton(DialogInterface.OnClickListener positiveButton) {
        this.positiveButton = positiveButton;
    }

    protected DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            positiveButton.onClick(dialogInterface, i);
            dialogInterface.cancel();
        }
    };

    private DialogInterface.OnClickListener neutralButton;

    public void setNeutralButton(DialogInterface.OnClickListener neutralButton) {
        this.neutralButton = neutralButton;
    }

    protected DialogInterface.OnClickListener neutralClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            neutralButton.onClick(dialogInterface, i);
            dialogInterface.cancel();
        }
    };

}
