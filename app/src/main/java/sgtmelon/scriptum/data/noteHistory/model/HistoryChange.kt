package sgtmelon.scriptum.data.noteHistory.model

/**
 * Model for store information about some changes.
 */
class HistoryChange<T>(val from: T, val to: T) {
    operator fun get(isPrevious: Boolean) = if (isPrevious) from else to
}