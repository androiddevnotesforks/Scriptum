package sgtmelon.safedialog.library;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.safedialog.office.annot.DialogAnn;

public final class OptionsDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private Context context;

    private String[] items;
    private int position;

    private DialogInterface.OnClickListener onClickListener;

    public void setArguments(String[] items, int p) {
        Bundle arg = new Bundle();

        arg.putStringArray(DialogAnn.INIT, items);
        arg.putInt(DialogAnn.VALUE, p);

        setArguments(arg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (savedInstanceState != null) {
            items = savedInstanceState.getStringArray(DialogAnn.INIT);
            position = savedInstanceState.getInt(DialogAnn.VALUE);
        } else if (arg != null) {
            items = arg.getStringArray(DialogAnn.INIT);
            position = arg.getInt(DialogAnn.VALUE);
        }

        return new AlertDialog.Builder(context)
                .setItems(items, this)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArray(DialogAnn.INIT, items);
        outState.putInt(DialogAnn.VALUE, position);
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (onClickListener != null) onClickListener.onClick(dialogInterface, i);
    }

}
