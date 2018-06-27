package sgtmelon.handynotes.app.model.state;

import sgtmelon.handynotes.office.intf.IntfItem;

public class StateDrag implements IntfItem.Drag {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setItemDrag(boolean itemDrag) {
        this.drag = itemDrag;
    }
}
