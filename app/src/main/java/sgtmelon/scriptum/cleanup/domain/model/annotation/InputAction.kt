package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl

/**
 * Describes actions in [InputControl]
 */
@IntDef(
        InputAction.RANK, InputAction.COLOR,
        InputAction.NAME, InputAction.TEXT, InputAction.ROLL,
        InputAction.ROLL_ADD, InputAction.ROLL_REMOVE, InputAction.ROLL_MOVE
)
annotation class InputAction {
    companion object {
        const val RANK = 0
        const val COLOR = 1

        const val NAME = 2
        const val TEXT = 3
        const val ROLL = 4

        const val ROLL_ADD = 5
        const val ROLL_REMOVE = 6
        const val ROLL_MOVE = 7
    }
}