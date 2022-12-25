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

    fun undo(): HistoryItem?

    fun redo(): HistoryItem?

    var isEnabled: Boolean

    fun onRankChange(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int)

    fun onColorChange(valueFrom: Color, valueTo: Color)

    fun onNameChange(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor)

    fun onTextChange(valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor)

    fun onRollChange(p: Int, valueFrom: String, valueTo: String, cursor: HistoryItem.Cursor)

    fun onRollAdd(p: Int, valueTo: String)

    fun onRollRemove(p: Int, valueFrom: String)

    fun onRollMove(valueFrom: Int, valueTo: Int)

}