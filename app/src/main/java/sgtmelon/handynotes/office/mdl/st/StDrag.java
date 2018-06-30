package sgtmelon.handynotes.office.mdl.st;

import sgtmelon.handynotes.office.intf.IntfItem;

public class StDrag implements IntfItem.Drag {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setItemDrag(boolean itemDrag) {
        this.drag = itemDrag;
    }
}
