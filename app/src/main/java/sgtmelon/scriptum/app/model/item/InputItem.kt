package sgtmelon.scriptum.app.model.item

import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.model.key.InputAction

/**
 * Модель для [InputControl]
 */
class InputItem(
        val tag: Int,
        private val valueFrom: String,
        private val valueTo: String,
        val cursorItem: CursorItem? = null,
        val position: Int = -1
) {

    init {
        if (tag == InputAction.name || tag == InputAction.text || tag == InputAction.roll) {
            if (cursorItem == null) {
                throw NullPointerException(InputItem::class.java.simpleName + "#cursorItem is null")
            }
        }
    }

    fun getValue(undo: Boolean) = if (undo) valueFrom else valueTo

    override fun toString(): String {
        val stringPosition = if (position != -1) " | position = $position" else ""

        val stringValueFrom = "from = " + if (valueFrom != "") valueFrom else "empty"
        val stringCursorFrom = cursorItem?.valueFrom?.toString() ?: "(null)"

        val stringValueTo = "to = " + if (valueTo != "") valueTo else "empty"
        val stringCursorTo = cursorItem?.valueTo?.toString() ?: "(null)"

        return tag.toString() + stringPosition +
                " | " + stringValueFrom + " / " + stringCursorFrom +
                " | " + stringValueTo + " / " + stringCursorTo
    }

}