package sgtmelon.scriptum.office.st;

import sgtmelon.scriptum.office.intf.ItemIntf;

public final class DragSt implements ItemIntf.Drag {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setItemDrag(boolean itemDrag) {
        this.drag = itemDrag;
    }

}
