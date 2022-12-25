package sgtmelon.scriptum.data.noteHistory

import androidx.annotation.IntDef

/**
 * Describes actions in [NoteHistoryImpl]
 */
@IntDef(
    HistoryAction.RANK, HistoryAction.COLOR,
    HistoryAction.NAME, HistoryAction.TEXT_CHANGE, HistoryAction.ROLL_CHANGE,
    HistoryAction.ROLL_ADD, HistoryAction.ROLL_REMOVE, HistoryAction.ROLL_MOVE
)
annotation class HistoryAction {
    companion object {
        // Common staff
        const val RANK = 0
        const val COLOR = 1
        const val NAME = 2

        // For text note
        const val TEXT_CHANGE = 3

        // For roll note
        const val ROLL_CHANGE = 4
        const val ROLL_ADD = 5
        const val ROLL_REMOVE = 6
        const val ROLL_MOVE = 7
    }
}