package sgtmelon.scriptum.cleanup.presentation.control.note.save

interface NoteAutoSave {

    fun skipPauseSave()

    fun changeAutoSaveWork(isWork: Boolean)
}