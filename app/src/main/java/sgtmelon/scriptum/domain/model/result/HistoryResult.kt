package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.infrastructure.model.key.preference.Color as AppColor

sealed class HistoryResult {

    class Name(val value: String, val cursor: Int) : HistoryResult()

    class Rank(val id: Long, val position: Int) : HistoryResult()

    class Color(val value: AppColor) : HistoryResult()

    sealed class Text : HistoryResult() {

        class Enter(val value: String, val cursor: Int) : Text()
    }

    sealed class Roll : HistoryResult() {

    }

}