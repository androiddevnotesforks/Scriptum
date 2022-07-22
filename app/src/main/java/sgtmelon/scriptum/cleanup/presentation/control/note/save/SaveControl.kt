package sgtmelon.scriptum.cleanup.presentation.control.note.save

interface SaveControl {

    var isNeedSave: Boolean

    fun changeAutoSaveWork(isWork: Boolean)

    fun onPauseSave()
}