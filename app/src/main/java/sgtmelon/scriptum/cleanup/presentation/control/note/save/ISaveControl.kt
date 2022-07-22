package sgtmelon.scriptum.cleanup.presentation.control.note.save

interface ISaveControl {

    var isNeedSave: Boolean

    fun changeAutoSaveWork(isWork: Boolean)

    fun onPauseSave()
}