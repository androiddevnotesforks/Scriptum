package sgtmelon.scriptum.office.st;

import sgtmelon.scriptum.office.intf.ItemIntf;

/**
 * Состояние для перетаскивания элементов, определяющее можно перетаскивать его или нет
 */
public final class DragListenerSt implements ItemIntf.DragListener {

    private boolean drag = false;

    public boolean isDrag() {
        return drag;
    }

    @Override
    public void setDrag(boolean drag) {
        this.drag = drag;
    }

}
