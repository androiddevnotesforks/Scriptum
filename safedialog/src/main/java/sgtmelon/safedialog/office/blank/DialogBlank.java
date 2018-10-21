package sgtmelon.safedialog.office.blank;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogBlank extends DialogFragment {

    protected Context context;

    protected String title, message;

    protected Button buttonPositive, buttonNeutral;

    private DialogInterface.OnClickListener positiveListener;
    protected final DialogInterface.OnClickListener onPositiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (positiveListener != null) {
                positiveListener.onClick(dialogInterface, i);
            }
            dialogInterface.cancel();
        }
    };

    private DialogInterface.OnClickListener neutralListener;
    protected final DialogInterface.OnClickListener onNeutralClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (neutralListener != null) {
                neutralListener.onClick(dialogInterface, i);
            }
            dialogInterface.cancel();
        }
    };

    private DialogInterface.OnDismissListener dismissListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();

        setEnable();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) dismissListener.onDismiss(dialog);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNeutralListener(DialogInterface.OnClickListener neutralListener) {
        this.neutralListener = neutralListener;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    protected void setEnable() {
        AlertDialog dialog = (AlertDialog) getDialog();
        buttonPositive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonNeutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
    }

}
