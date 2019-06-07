package sgtmelon.scriptum.model.key

import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.item.InputItem

/**
 * Аннотация для [InputItem.tag], [InputControl]
 *
 * @author SerjantArbuz
 */
object InputAction {

    // TODO перевести в аннотацию

    const val rank = 0
    const val color = 1

    const val name = 2
    const val text = 3
    const val roll = 4

    const val rollAdd = 5
    const val rollRemove = 6
    const val rollMove = 7
}