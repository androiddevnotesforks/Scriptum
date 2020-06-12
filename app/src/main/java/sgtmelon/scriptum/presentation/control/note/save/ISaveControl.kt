package sgtmelon.scriptum.presentation.control.note.save

/**
 * Interface for [SaveControl].
 */
interface ISaveControl {

    fun setModel(model: SaveControl.Model)

    var needSave: Boolean

    fun setSaveEvent(isWork: Boolean)

    fun onPauseSave()

}