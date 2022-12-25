package sgtmelon.scriptum.data.noteHistory

import androidx.annotation.IntDef

/**
 * Describes actions in [NoteHistoryImpl]
 */
@IntDef(
    HistoryAction.RANK, HistoryAction.COLOR,
    HistoryAction.NAME, HistoryAction.TEXT, HistoryAction.ROLL,
    HistoryAction.ROLL_ADD, HistoryAction.ROLL_REMOVE, HistoryAction.ROLL_MOVE
)
annotation class HistoryAction {
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