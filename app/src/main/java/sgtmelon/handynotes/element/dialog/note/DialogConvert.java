package sgtmelon.handynotes.element.dialog.note;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefType;

public class DialogConvert extends DialogFragment
        implements DialogInterface.OnClickListener {

    public void setArguments(@DefType int type) {
        Bundle arg = new Bundle();
        arg.putInt(Db.NT_TP, type);
        setArguments(arg);
    }

    private int type;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (arg != null) {
            type = arg.getInt(Db.NT_TP);
        } else if (savedInstanceState != null) {
            type = savedInstanceState.getInt(Db.NT_TP);
        }

        String message = type == DefType.text
                ? context.getString(R.string.dialog_text_convert_to_roll)
                : context.getString(R.string.dialog_roll_convert_to_text);

        return new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog)
                .setTitle(context.getString(R.string.dialog_title_convert))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.dialog_btn_yes), this)
                .setNegativeButton(context.getString(R.string.dialog_btn_no), (dialog, id) -> dialog.cancel())
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Db.NT_TP, type);
    }

}
