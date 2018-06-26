package sgtmelon.handynotes.app.model.state;

import sgtmelon.handynotes.office.intf.ItemClick;

public class StateDrag implements ItemClick.Drag {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setDrag(boolean itemDrag) {
        this.drag = itemDrag;
    }
}
