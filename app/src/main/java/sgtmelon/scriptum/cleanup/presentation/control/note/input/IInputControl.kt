package sgtmelon.scriptum.cleanup.presentation.control.note.input

import sgtmelon.scriptum.cleanup.domain.model.item.InputItem

/**
 * Interface for communicate with [InputControl].
 */
interface IInputControl {

    val access: InputControl.Access

    fun reset()

    fun undo(): InputItem?

    fun redo(): InputItem?

    var isEnabled: Boolean

    fun onRankChange(idFrom: Long, psFrom: Int, idTo: Long, psTo: Int)

    fun onColorChange(valueFrom: Int, valueTo: Int)

    fun onNameChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onTextChange(valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onRollChange(p: Int, valueFrom: String, valueTo: String, cursor: InputItem.Cursor)

    fun onRollAdd(p: Int, valueTo: String)

    fun onRollRemove(p: Int, valueFrom: String)

    fun onRollMove(valueFrom: Int, valueTo: Int)

}