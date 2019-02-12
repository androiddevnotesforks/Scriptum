package sgtmelon.scriptum.office.state

import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Состояние для перетаскивания элементов, определяющее можно перетаскивать его или нет
 */
class DragState : ItemIntf.DragListener { // TODO (убрать)

    private var drag = false

    fun isDrag(): Boolean {
        return drag
    }

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

}