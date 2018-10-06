package sgtmelon.scriptum.office.st;

import java.util.List;

import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.office.Help;

public final class CheckSt {

    private boolean all;

    public boolean isAll() {
        return all;
    }

    public void setAll(List<RollItem> listRoll) {
        this.all = Help.Note.isAllCheck(listRoll);
    }

    public boolean setAll(int checkValue, int listSize) {
        boolean all = checkValue == listSize;

        if (this.all != all) {
            this.all = all;
            return true;
        }

        return false;
    }

}
