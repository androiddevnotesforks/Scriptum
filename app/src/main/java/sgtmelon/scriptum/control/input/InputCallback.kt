package sgtmelon.scriptum.control.input

import sgtmelon.scriptum.model.item.CursorItem

/**
 * Интерфейс для общения с [InputControl]
 */
interface InputCallback {

    val isChangeEnabled: Boolean

    fun setEnabled(enabled: Boolean)

    fun onRankChange(valueFrom: List<Long>, valueTo: List<Long>)

    fun onColorChange(valueFrom: Int, valueTo: Int)

    fun onNameChange(valueFrom: String, valueTo: String, cursorItem: CursorItem)

    fun onTextChange(valueFrom: String, valueTo: String, cursorItem: CursorItem)

    fun onRollChange(p: Int, valueFrom: String, valueTo: String, cursorItem: CursorItem)

    fun onRollAdd(p: Int, valueTo: String)

    fun onRollRemove(p: Int, valueFrom: String)

    fun onRollMove(valueFrom: Int, valueTo: Int)

}