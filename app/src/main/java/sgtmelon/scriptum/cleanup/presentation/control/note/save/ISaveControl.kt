package sgtmelon.scriptum.cleanup.presentation.control.note.save

/**
 * Interface for [SaveControl].
 */
interface ISaveControl {

    var needSave: Boolean

    fun setSaveEvent(isWork: Boolean)

    fun onPauseSave()

}