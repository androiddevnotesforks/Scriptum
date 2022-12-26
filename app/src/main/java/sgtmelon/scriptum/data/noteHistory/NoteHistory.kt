package sgtmelon.scriptum.data.noteHistory

/**
 * Interface for communicate with [NoteHistoryImpl].
 */
// TODO add different interfaces for different noteType: Text, Roll
// TODO pass different interfaces to viewmodels by noteType
// TODO add tests
interface NoteHistory {

    val available: HistoryMoveAvailable

    fun reset()

    fun undo(): HistoryAction?

    fun redo(): HistoryAction?

    fun add(action: HistoryAction)

    var isEnabled: Boolean

}