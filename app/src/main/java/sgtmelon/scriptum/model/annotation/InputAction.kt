package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.item.InputItem

/**
 * Для [InputItem.tag], [InputControl]
 *
 * @author SerjantArbuz
 */
@IntDef(
        InputAction.rank, InputAction.color,
        InputAction.name, InputAction.text, InputAction.roll,
        InputAction.rollAdd, InputAction.rollRemove, InputAction.rollMove
)
annotation class InputAction {

    companion object {
        const val rank = 0
        const val color = 1

        const val name = 2
        const val text = 3
        const val roll = 4

        const val rollAdd = 5
        const val rollRemove = 6
        const val rollMove = 7
    }

}