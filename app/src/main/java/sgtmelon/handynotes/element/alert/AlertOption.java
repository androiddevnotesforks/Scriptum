package sgtmelon.handynotes.element.alert;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfAlert;

public class AlertOption {

    //region Variables
    private final Context context;

    private final ItemNote itemNote;
    private final int p;
    //endregion

    public AlertOption(Context context, ItemNote itemNote, int p) {
        this.context = context;

        this.itemNote = itemNote;
        this.p = p;
    }

    private IntfAlert.OptionNote optionNote;
    private IntfAlert.OptionBin optionBin;

    public void setOptionNote(IntfAlert.OptionNote optionNote) {
        this.optionNote = optionNote;
    }

    public void setOptionBin(IntfAlert.OptionBin optionBin) {
        this.optionBin = optionBin;
    }

    public void showOptionNote() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);

        String[] itemOption;

        switch (itemNote.getType()) {
            case DefType.text:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_text);
                itemOption[0] = itemNote.isStatus() ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);

                alert.setItems(itemOption, (dialog, item) -> {
                    switch (item) {
                        case 0:
                            optionNote.onOptionBindClick(itemNote, p);
                            break;
                        case 1:
                            optionNote.onOptionConvertClick(itemNote, p);
                            break;
                        case 2:
                            Help.optionsCopy(context, itemNote);
                            break;
                        case 3:
                            optionNote.onOptionDeleteClick(itemNote, p);
                            break;
                    }
                });
                break;
            case DefType.roll:
                itemOption = context.getResources().getStringArray(R.array.dialog_menu_roll);

                final int[] checkText = itemNote.getCheck();
                boolean checkAll = checkText[0] == checkText[1];

                final int check = checkAll ? DefCheck.notDone : DefCheck.done;

                itemOption[0] = checkAll ? context.getString(R.string.dialog_menu_check_zero) : context.getString(R.string.dialog_menu_check_all);
                itemOption[1] = itemNote.isStatus() ? context.getString(R.string.dialog_menu_status_unbind) : context.getString(R.string.dialog_menu_status_bind);

                alert.setItems(itemOption, (dialog, item) -> {
                    switch (item) {
                        case 0:
                            optionNote.onOptionCheckClick(itemNote, p, check, checkText[1]);
                            break;
                        case 1:
                            optionNote.onOptionBindClick(itemNote, p);
                            break;
                        case 2:
                            optionNote.onOptionConvertClick(itemNote, p);
                            break;
                        case 3:
                            Help.optionsCopy(context, itemNote);
                            break;
                        case 4:
                            optionNote.onOptionDeleteClick(itemNote, p);
                            break;
                    }
                });
                break;
        }

        alert.setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void showOptionBin() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AppTheme_AlertDialog);

        String[] itemOption = context.getResources().getStringArray(R.array.dialog_menu_bin);

        alert.setItems(itemOption, (dialog, item) -> {
            switch (item) {
                case 0:
                    optionBin.onOptionRestoreClick(itemNote, p);
                    break;
                case 1:
                    Help.optionsCopy(context, itemNote);
                    break;
                case 2:
                    optionBin.onOptionClearClick(itemNote, p);
                    break;
            }
        }).setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
