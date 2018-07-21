package sgtmelon.handynotes.element.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.intf.IntfDialog;

public class DlgOptionBin extends DialogFragment implements DialogInterface.OnClickListener {

    public void setArguments(int p) {
        Bundle arg = new Bundle();
        arg.putInt(Dlg.VALUE, p);
        setArguments(arg);
    }

    private int p;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (arg != null) {
            p = arg.getInt(Dlg.VALUE);
        } else if (savedInstanceState != null) {
            p = savedInstanceState.getInt(Dlg.VALUE);
        }

        String[] itemOption = context.getResources().getStringArray(R.array.dialog_menu_bin);

        return new AlertDialog.Builder(context)
                .setItems(itemOption, this)
                .setCancelable(true)
                .create();
    }

    private IntfDialog.OptionBin optionBin;

    public void setOptionBin(IntfDialog.OptionBin optionBin) {
        this.optionBin = optionBin;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case 0:
                optionBin.onOptionRestoreClick(p);
                break;
            case 1:
                optionBin.onOptionCopyClick(p);
                break;
            case 2:
                optionBin.onOptionClearClick(p);
                break;
        }
        dialogInterface.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Dlg.VALUE, p);
    }

}
