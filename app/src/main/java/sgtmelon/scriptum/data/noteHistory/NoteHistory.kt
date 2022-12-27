package sgtmelon.scriptum.data.noteHistory

// TODO add tests
interface NoteHistory {

    val available: HistoryMoveAvailable

    fun reset()

    fun undo(): HistoryAction?

    fun redo(): HistoryAction?

    fun add(action: HistoryAction)

    var saveChanges: Boolean

}