package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem

/**
 * Describes actions for [NoteHistoryImpl]
 */
sealed class HistoryAction {

    data class Name(
        val value: HistoryChange<String>,
        val cursor: HistoryChange<Int>
    ) : HistoryAction()

    data class Rank(
        val id: HistoryChange<Long>,
        val position: HistoryChange<Int>
    ) : HistoryAction()

    data class Color(val value: HistoryChange<Color>) : HistoryAction()

    sealed class Text : HistoryAction() {

        data class Enter(
            val value: HistoryChange<String>,
            val cursor: HistoryChange<Int>
        ) : HistoryAction()

    }

    sealed class Roll : HistoryAction() {

        data class Enter(
            val p: Int,
            val value: HistoryChange<String>,
            val cursor: HistoryChange<Int>
        ) : Roll()

        data class Add(val p: Int, val item: RollItem) : Roll()

        data class Remove(val p: Int, val item: RollItem) : Roll()

        data class Move(val value: HistoryChange<Int>) : Roll()

    }
}