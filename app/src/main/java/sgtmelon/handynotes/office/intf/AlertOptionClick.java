package sgtmelon.handynotes.office.intf;

import sgtmelon.handynotes.office.def.data.DefCheck;
import sgtmelon.handynotes.app.model.item.ItemNote;

public interface AlertOptionClick {

    interface DialogNote {
        void onDialogCheckClick(ItemNote itemNote, int p, @DefCheck int rollCheck, String rollAll);

        void onDialogBindClick(ItemNote itemNote, int p);

        void onDialogConvertClick(ItemNote itemNote, int p);

        void onDialogDeleteClick(ItemNote itemNote, int p);
    }

    interface DialogBin {
        void onDialogRestoreClick(ItemNote itemNote, int p);

        void onDialogDeleteForeverClick(ItemNote itemNote, int p);
    }

}
