package sgtmelon.scriptum.cleanup.presentation.control.note.save

interface NoteAutoSave {

    var isNeedSave: Boolean

    fun changeAutoSaveWork(isWork: Boolean)

    fun onPauseSave()
}