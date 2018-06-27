package sgtmelon.handynotes.office.intf;

import sgtmelon.handynotes.office.def.data.DefCheck;
import sgtmelon.handynotes.app.model.item.ItemNote;

public interface IntfAlert {

    interface OptionNote {
        void onOptionCheckClick(ItemNote itemNote, int p, @DefCheck int rollCheck, String rollAll);

        void onOptionBindClick(ItemNote itemNote, int p);

        void onOptionConvertClick(ItemNote itemNote, int p);

        void onOptionDeleteClick(ItemNote itemNote, int p);
    }

    interface OptionBin {
        void onOptionRestoreClick(ItemNote itemNote, int p);

        void onOptionClearClick(ItemNote itemNote, int p);
    }

}
