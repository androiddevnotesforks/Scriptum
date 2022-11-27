package sgtmelon.scriptum.domain.model.result

import java.util.Calendar

sealed class TidyUpResult {

    data class Cancel(val noteId: Long) : TidyUpResult()

    data class Update(val noteId: Long, val calendar: Calendar) : TidyUpResult()
}