package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryMoveAvailable

interface NoteHistory {

    val list: List<HistoryAction>

    val available: HistoryMoveAvailable

    fun reset()

    fun undo(): HistoryAction?

    fun redo(): HistoryAction?

    fun add(action: HistoryAction)

    var saveChanges: Boolean

}