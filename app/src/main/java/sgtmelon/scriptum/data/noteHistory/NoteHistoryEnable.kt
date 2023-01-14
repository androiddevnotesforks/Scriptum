package sgtmelon.scriptum.data.noteHistory

interface NoteHistoryEnable {

    fun disableHistoryChanges(func: () -> Unit)
}