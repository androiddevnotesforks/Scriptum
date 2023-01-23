package sgtmelon.scriptum.data.noteHistory.model

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.infrastructure.model.key.preference.Color as AppColor

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

    data class Color(val value: HistoryChange<AppColor>) : HistoryAction()

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

        sealed class List(val p: Int, val item: RollItem) : Roll() {

            class Add(p: Int, item: RollItem) : List(p, item)

            class Remove(p: Int, item: RollItem) : List(p, item)
        }

        data class Move(val value: HistoryChange<Int>) : Roll()
    }
}