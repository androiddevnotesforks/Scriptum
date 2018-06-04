package sgtmelon.handynotes.model.state;

import sgtmelon.handynotes.interfaces.ItemClick;

public class DragState implements ItemClick.Drag {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setDrag(boolean itemDrag) {
        this.drag = itemDrag;
    }
}
