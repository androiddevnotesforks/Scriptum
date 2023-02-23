package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color as AppColor

sealed class HistoryResult {

    data class Name(val value: String, val cursor: Int) : HistoryResult()

    data class Rank(val id: Long, val position: Int) : HistoryResult()

    data class Color(val value: AppColor) : HistoryResult()

    sealed class Text : HistoryResult() {

        data class Enter(val value: String, val cursor: Int) : Text()
    }

    sealed class Roll : HistoryResult() {

        data class Enter(val p: Int, val value: String, val cursor: Int) : Roll()

        data class Add(val p: Int, val item: RollItem) : Roll()

        data class Remove(val p: Int) : Roll()

        data class Move(val from: Int, val to: Int) : Roll()
    }
}