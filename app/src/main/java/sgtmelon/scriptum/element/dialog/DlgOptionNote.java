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
import sgtmelon.scriptum.office.annot.AnnDb;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfDialog;

public final class DlgOptionNote extends DialogFragment implements DialogInterface.OnClickListener {

    private Context context;

    private int position, type;
    private boolean status, check;

    private IntfDialog.OptionNote optionNote;

    public void setArguments(int p, @DefType int type, boolean status, boolean check) {
        Bundle arg = new Bundle();

        arg.putInt(DefDlg.VALUE, p);
        arg.putInt(AnnDb.NT_TP, type);
        arg.putBoolean(AnnDb.NT_ST, status);
        arg.putBoolean(AnnDb.RL_CH, check);

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
            type = arg.getInt(AnnDb.NT_TP);
            status = arg.getBoolean(AnnDb.NT_ST);
            check = arg.getBoolean(AnnDb.RL_CH);
            position = arg.getInt(DefDlg.VALUE);
        } else if (savedInstanceState != null) {
            type = savedInstanceState.getInt(AnnDb.NT_TP);
            status = savedInstanceState.getBoolean(AnnDb.NT_ST);
            check = savedInstanceState.getBoolean(AnnDb.RL_CH);
            position = savedInstanceState.getInt(DefDlg.VALUE);
        }

        String[] itemOption = new String[0];
        switch (type) {
            case DefType.text:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_text);

                itemOption[0] = status ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);
                break;
            case DefType.roll:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_roll);

                itemOption[0] = check ? context.getString(R.string.dialog_menu_check_zero) : context.getString(R.string.dialog_menu_check_all);
                itemOption[1] = status ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);
                break;
        }

        return new AlertDialog.Builder(context)
                .setItems(itemOption, this)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(AnnDb.NT_TP, type);
        outState.putBoolean(AnnDb.NT_ST, status);
        outState.putBoolean(AnnDb.RL_CH, check);
        outState.putInt(DefDlg.VALUE, position);
    }

    public void setOptionNote(IntfDialog.OptionNote optionNote) {
        this.optionNote = optionNote;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (type) {
            case DefType.text:
                switch (i) {
                    case 0:
                        optionNote.onOptionBindClick(position);
                        break;
                    case 1:
                        optionNote.onOptionConvertClick(position);
                        break;
                    case 2:
                        optionNote.onOptionCopyClick(position);
                        break;
                    case 3:
                        optionNote.onOptionDeleteClick(position);
                        break;
                }
                break;
            case DefType.roll:
                switch (i) {
                    case 0:
                        optionNote.onOptionCheckClick(position);
                        break;
                    case 1:
                        optionNote.onOptionBindClick(position);
                        break;
                    case 2:
                        optionNote.onOptionConvertClick(position);
                        break;
                    case 3:
                        optionNote.onOptionCopyClick(position);
                        break;
                    case 4:
                        optionNote.onOptionDeleteClick(position);
                        break;
                }
                break;
        }
        dialogInterface.cancel();
    }

}
