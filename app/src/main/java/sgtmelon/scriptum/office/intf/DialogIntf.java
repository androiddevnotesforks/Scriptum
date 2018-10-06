package sgtmelon.scriptum.office.intf;

public interface DialogIntf {

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
