package sgtmelon.handynotes.element.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.Dlg;
import sgtmelon.handynotes.office.annot.def.db.DefDb;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfDialog;

public class DlgOptionNote extends DialogFragment implements DialogInterface.OnClickListener {

    public void setArguments(@DefType int type, boolean status, boolean isAll, int p) {
        Bundle arg = new Bundle();

        arg.putInt(DefDb.NT_TP, type);
        arg.putBoolean(DefDb.NT_ST, status);
        arg.putBoolean(DefDb.RL_CH, isAll);
        arg.putInt(Dlg.VALUE, p);

        setArguments(arg);
    }

    private int type;
    private boolean status;
    private boolean isAll;
    private int p;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getContext();
        Bundle arg = getArguments();

        if (arg != null) {
            type = arg.getInt(DefDb.NT_TP);
            status = arg.getBoolean(DefDb.NT_ST);
            isAll = arg.getBoolean(DefDb.RL_CH);
            p = arg.getInt(Dlg.VALUE);
        } else if (savedInstanceState != null) {
            type = savedInstanceState.getInt(DefDb.NT_TP);
            status = savedInstanceState.getBoolean(DefDb.NT_ST);
            isAll = savedInstanceState.getBoolean(DefDb.RL_CH);
            p = savedInstanceState.getInt(Dlg.VALUE);
        }

        String[] itemOption = new String[0];
        switch (type) {
            case DefType.text:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_text);

                itemOption[0] = status ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);
                break;
            case DefType.roll:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_roll);

                itemOption[0] = isAll ? context.getString(R.string.dialog_menu_check_zero) : context.getString(R.string.dialog_menu_check_all);
                itemOption[1] = status ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);
                break;
        }

        return new AlertDialog.Builder(context)
                .setItems(itemOption, this)
                .setCancelable(true)
                .create();
    }

    private IntfDialog.OptionNote optionNote;

    public void setOptionNote(IntfDialog.OptionNote optionNote) {
        this.optionNote = optionNote;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (type) {
            case DefType.text:
                switch (i) {
                    case 0:
                        optionNote.onOptionBindClick(p);
                        break;
                    case 1:
                        optionNote.onOptionConvertClick(p);
                        break;
                    case 2:
                        optionNote.onOptionCopyClick(p);
                        break;
                    case 3:
                        optionNote.onOptionDeleteClick(p);
                        break;
                }
                break;
            case DefType.roll:
                switch (i) {
                    case 0:
                        optionNote.onOptionCheckClick(p);
                        break;
                    case 1:
                        optionNote.onOptionBindClick(p);
                        break;
                    case 2:
                        optionNote.onOptionConvertClick(p);
                        break;
                    case 3:
                        optionNote.onOptionCopyClick(p);
                        break;
                    case 4:
                        optionNote.onOptionDeleteClick(p);
                        break;
                }
                break;
        }
        dialogInterface.cancel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefDb.NT_TP, type);
        outState.putBoolean(DefDb.NT_ST, status);
        outState.putBoolean(DefDb.RL_CH, isAll);
        outState.putInt(Dlg.VALUE, p);
    }

}
