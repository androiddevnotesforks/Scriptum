package sgtmelon.scriptum.app.model.key

import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.model.item.InputItem

/**
 * Аннотация для [InputItem.tag], [InputControl]
 */
object InputAction {
    const val rank = 0
    const val color = 1

    const val name = 2
    const val text = 3
    const val roll = 4

    const val rollAdd = 5
    const val rollRemove = 6
    const val rollMove = 7
}