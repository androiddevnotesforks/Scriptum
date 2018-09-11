package sgtmelon.scriptum.office.blank;

import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public abstract class BlankDlg extends DialogFragment {

    protected String title, message;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private DialogInterface.OnClickListener positiveListener;
    private DialogInterface.OnClickListener neutralListener;
    private DialogInterface.OnDismissListener dismissListener;

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNeutralListener(DialogInterface.OnClickListener neutralListener) {
        this.neutralListener = neutralListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    protected final DialogInterface.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (positiveListener != null) {
                positiveListener.onClick(dialogInterface, i);
            }
            dialogInterface.cancel();
        }
    };

    protected final DialogInterface.OnClickListener onNeutralClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (neutralListener != null) {
                neutralListener.onClick(dialogInterface, i);
            }
            dialogInterface.cancel();
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        setEnable();
    }

    protected Button buttonPositive, buttonNeutral;

    protected void setEnable() {
        AlertDialog dialog = (AlertDialog) getDialog();
        buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonNeutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) dismissListener.onDismiss(dialog);
    }
}
