package sgtmelon.handynotes.office.intf;

public interface IntfDialog {

    interface OptionBin {
        void onOptionRestoreClick(int p);

        void onOptionCopyClick(int p);

        void onOptionClearClick(int p);
    }

    interface OptionNote {
        void onOptionCheckClick(int p);

        void onOptionBindClick(int p);

        void onOptionConvertClick(int p);

        void onOptionCopyClick(int p);

        void onOptionDeleteClick(int p);
    }

}
