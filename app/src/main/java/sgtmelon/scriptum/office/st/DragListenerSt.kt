package sgtmelon.scriptum.office.st

import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Состояние для перетаскивания элементов, определяющее можно перетаскивать его или нет
 */
class DragListenerSt : ItemIntf.DragListener {

    private var drag = false

    fun isDrag(): Boolean {
        return drag
    }

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

}