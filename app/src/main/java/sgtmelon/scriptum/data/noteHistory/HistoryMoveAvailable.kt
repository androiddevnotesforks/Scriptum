package sgtmelon.scriptum.data.noteHistory

/**
 * Class for store information about undo/redo moves availability.
 */
data class HistoryMoveAvailable(val undo: Boolean, val redo: Boolean)