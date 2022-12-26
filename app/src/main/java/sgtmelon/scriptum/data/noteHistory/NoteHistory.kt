package sgtmelon.scriptum.data.noteHistory

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Interface for communicate with [NoteHistoryImpl].
 */
// TODO add different interfaces for different noteType: Text, Roll
// TODO pass different interfaces to viewmodels by noteType
// TODO move to data module
// TODO add tests
interface NoteHistory {

    val available: HistoryMoveAvailable

    fun reset()

    fun undo(): HistoryAction?

    fun redo(): HistoryAction?

    fun add(action: HistoryAction)

    var isEnabled: Boolean

    @Deprecated("Refactor")
    fun onRank(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int)

    @Deprecated("Refactor")
    fun onColor(valueFrom: Color, valueTo: Color)

    @Deprecated("Refactor")
    fun onTextEnter(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor)

    @Deprecated("Refactor")
    fun onRollEnter(p: Int, valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor)

    @Deprecated("Refactor")
    fun onRollAdd(p: Int, valueTo: String)

    @Deprecated("Refactor")
    fun onRollRemove(p: Int, valueFrom: String)

    @Deprecated("Refactor")
    fun onRollMove(valueFrom: Int, valueTo: Int)

}