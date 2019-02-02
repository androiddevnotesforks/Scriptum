package sgtmelon.scriptum.office.intf

import sgtmelon.scriptum.app.control.InputControl
import sgtmelon.scriptum.app.model.item.CursorItem

/**
 * Интерфейс для общения с [InputControl]
 */
interface InputIntf {

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