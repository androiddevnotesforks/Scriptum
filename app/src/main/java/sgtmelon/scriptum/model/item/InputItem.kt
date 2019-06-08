package sgtmelon.scriptum.model.item

import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.annotation.InputAction

/**
 * Модель для [InputControl]
 *
 * @author SerjantArbuz
 */
class InputItem(
        @InputAction val tag: Int,
        private val valueFrom: String,
        private val valueTo: String,
        val cursor: Cursor? = null,
        val p: Int = -1
) {

    init {
        if (tag == InputAction.name || tag == InputAction.text || tag == InputAction.roll) {
            if (cursor == null) {
                throw NullPointerException(InputItem::class.java.simpleName + "#cursor is null")
            }
        }
    }

    operator fun get(isUndo: Boolean) = if (isUndo) valueFrom else valueTo

    override fun toString(): String {
        val stringPosition = if (p != -1) " | p = $p" else ""

        val stringValueFrom = "from = " + if (valueFrom != "") valueFrom else "empty"
        val stringCursorFrom = cursor?.valueFrom?.toString() ?: "(null)"

        val stringValueTo = "to = " + if (valueTo != "") valueTo else "empty"
        val stringCursorTo = cursor?.valueTo?.toString() ?: "(null)"

        return tag.toString() + stringPosition +
                " | " + stringValueFrom + " / " + stringCursorFrom +
                " | " + stringValueTo + " / " + stringCursorTo
    }

    /**
     * Модель для сохранения в [InputItem] курсора текста
     *
     * @author SerjantArbuz
     */
    class Cursor(val valueFrom: Int, val valueTo: Int) {

        operator fun get(isUndo: Boolean) = if (isUndo) valueFrom else valueTo

        override fun toString() = "valueFrom = $valueFrom | valueTo = $valueTo"

    }

}