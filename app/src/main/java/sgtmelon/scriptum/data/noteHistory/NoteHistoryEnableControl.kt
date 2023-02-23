package sgtmelon.scriptum.data.noteHistory

interface NoteHistoryEnableControl {

    fun disableHistoryChanges(func: () -> Unit)
}