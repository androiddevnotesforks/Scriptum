package sgtmelon.handynotes.view.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.def.data.DefCheck;
import sgtmelon.handynotes.office.annot.def.data.DefType;
import sgtmelon.handynotes.office.intf.IntfAlert;
import sgtmelon.handynotes.app.model.item.ItemNote;

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
                itemOption = new String[]{"", context.getString(R.string.dialog_menu_convert_to_roll), context.getString(R.string.dialog_menu_copy), context.getString(R.string.dialog_menu_delete)};

                if (itemNote.isStatus()) {
                    itemOption[0] = context.getString(R.string.dialog_menu_status_unbind);
                } else {
                    itemOption[0] = context.getString(R.string.dialog_menu_status_bind);
                }

                alert.setItems(itemOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
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
                    }
                });
                break;
            case DefType.roll:
                itemOption = new String[]{"", "", context.getString(R.string.dialog_menu_convert_to_text), context.getString(R.string.dialog_menu_copy), context.getString(R.string.dialog_menu_delete)};

                final String[] checkText = itemNote.getText().split(DefCheck.divider);
                final @DefCheck int check;

                if (checkText[0].equals(checkText[1])) {
                    itemOption[0] = context.getString(R.string.dialog_menu_check_zero);
                    check = DefCheck.notDone;
                } else {
                    itemOption[0] = context.getString(R.string.dialog_menu_check_all);
                    check = DefCheck.done;
                }

                if (itemNote.isStatus()) {
                    itemOption[1] = context.getString(R.string.dialog_menu_status_unbind);
                } else {
                    itemOption[1] = context.getString(R.string.dialog_menu_status_bind);
                }

                alert.setItems(itemOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
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

        String[] itemOption = {context.getString(R.string.dialog_menu_restore), context.getString(R.string.dialog_menu_copy), context.getString(R.string.dialog_menu_clear)};

        alert.setItems(itemOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: //Восстанавливаем заметку
                        optionBin.onOptionRestoreClick(itemNote, p);
                        break;
                    case 1:
                        Help.optionsCopy(context, itemNote);
                        break;
                    case 2: //Удаляем навсегда
                        optionBin.onOptionClearClick(itemNote, p);
                        break;
                }
            }
        }).setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
    }

}
