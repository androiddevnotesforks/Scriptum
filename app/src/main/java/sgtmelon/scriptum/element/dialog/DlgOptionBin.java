package sgtmelon.scriptum.element.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.intf.IntfDialog;

public final class DlgOptionBin extends DialogFragment implements DialogInterface.OnClickListener {

    private Context context;

    private int p;

    private IntfDialog.OptionBin optionBin;

    public void setArguments(int p) {
        Bundle arg = new Bundle();

        arg.putInt(DefDlg.VALUE, p);

        setArguments(arg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();

        if (arg != null) {
            p = arg.getInt(DefDlg.VALUE);
        } else if (savedInstanceState != null) {
            p = savedInstanceState.getInt(DefDlg.VALUE);
        }

        String[] itemOption = context.getResources().getStringArray(R.array.dialog_menu_bin);

        return new AlertDialog.Builder(context)
                .setItems(itemOption, this)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDlg.VALUE, p);
    }

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

}
