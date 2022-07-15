package sgtmelon.scriptum.cleanup.domain.model.item

import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.presentation.control.note.input.InputControl

/**
 * Model for [InputControl]
 */
data class InputItem(
        @InputAction val tag: Int,
        private val valueFrom: String,
        private val valueTo: String,
        val cursor: Cursor? = ND_CURSOR,
        val p: Int = ND_POSITION
) {

    init {
        if (tag == InputAction.NAME || tag == InputAction.TEXT || tag == InputAction.ROLL) {
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
     * Model for save text cursor inside [InputItem]
     */
    data class Cursor(val valueFrom: Int, val valueTo: Int) {

        companion object {
            operator fun Cursor?.get(isUndo: Boolean) = if (this != null) {
                if (isUndo) valueFrom else valueTo
            } else ND_VALUE

            const val ND_VALUE = 0
        }

        override fun toString() = "valueFrom = $valueFrom | valueTo = $valueTo"

    }

    companion object {
        val ND_CURSOR = null
        const val ND_POSITION = -1
    }

}