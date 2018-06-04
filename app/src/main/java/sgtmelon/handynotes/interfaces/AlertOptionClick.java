package sgtmelon.handynotes.interfaces;

import sgtmelon.handynotes.model.item.ItemNote;

public interface AlertOptionClick {

    interface DialogNote {
        void onDialogCheckClick(ItemNote itemNote, int p, int rlCheck, String checkMax);

        void onDialogBindClick(ItemNote itemNote, int p);

        void onDialogConvertClick(ItemNote itemNote, int p);

        void onDialogDeleteClick(ItemNote itemNote, int p);
    }

    interface DialogBin {
        void onDialogRestoreClick(ItemNote itemNote, int p);

        void onDialogDeleteForeverClick(ItemNote itemNote, int p);
    }

}
