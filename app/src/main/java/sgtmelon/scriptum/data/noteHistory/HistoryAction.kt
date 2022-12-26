package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

/**
 * Describes actions for [NoteHistoryImpl]
 */
sealed class HistoryAction {

    data class Name(val value: Change<String>, val cursor: Change<Int>) : HistoryAction()

    data class Rank(val id: Change<Long>, val position: Change<Int>) : HistoryAction()

    data class Color(val value: Change<Color>) : HistoryAction()

    sealed class Text : HistoryAction() {

        data class Enter(val value: Change<String>, val cursor: Change<Int>) : HistoryAction()

    }

    sealed class Roll : HistoryAction() {

        data class Enter(val p: Int, val value: Change<String>, val cursor: Change<Int>) : Roll()

        data class Add(val p: Int, val item: RollItem) : Roll()

        data class Remove(val p: Int, val item: RollItem) : Roll()

        data class Move(val value: Change<Int>) : Roll()

    }

    class Change<T>(val from: T, val to: T) {
        operator fun get(isUndo: Boolean) = if (isUndo) from else to
    }
}