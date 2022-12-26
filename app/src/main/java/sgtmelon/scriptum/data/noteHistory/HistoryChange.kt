package sgtmelon.scriptum.data.noteHistory

/**
 * Model for store information about some changes.
 */
class HistoryChange<T>(val from: T, val to: T) {
    operator fun get(isUndo: Boolean) = if (isUndo) from else to
}