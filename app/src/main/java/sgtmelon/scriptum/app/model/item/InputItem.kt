package sgtmelon.scriptum.app.model.item

import sgtmelon.scriptum.app.control.input.InputControl
import sgtmelon.scriptum.app.control.input.InputDef

/**
 * Модель для [InputControl]
 */
class InputItem(
        @param:InputDef val tag: Int,
        private val valueFrom: String,
        private val valueTo: String,
        val cursorItem: CursorItem? = null,
        val position: Int = -1
) {

    init {
        if (tag == InputDef.name || tag == InputDef.text || tag == InputDef.roll) {
            if (cursorItem == null) {
                throw NullPointerException(InputItem::class.java.simpleName + "#cursorItem is null")
            }
        }
    }

    fun getValue(undo: Boolean) = if (undo) valueFrom else valueTo

    override fun toString(): String { // TODO прибрать
        val stringPosition = if (position != -1) " | position = $position" else ""

        val stringValueFrom = "from = " + if (valueFrom != "") valueFrom else "empty"
        val stringCursorFrom = cursorItem?.valueFrom?.toString() ?: "(null)"

        val stringValueTo = "to = " + if (valueTo != "") valueTo else "empty"
        val stringCursorTo = cursorItem?.valueTo?.toString() ?: "(null)"

        return tag.toString() + stringPosition +
                " | " + stringValueFrom + " / " + stringCursorFrom +
                " | " + stringValueTo + " / " + stringCursorTo
    }

    // TODO убрать
    class Builder {

        private var tag = InputDef.color
        private var position = -1

        private var valueFrom: String? = null
        private var valueTo: String? = null

        private var cursorItem: CursorItem? = null

        fun setTag(@InputDef tag: Int): Builder {
            this.tag = tag
            return this
        }

        fun setPosition(position: Int): Builder {
            this.position = position
            return this
        }

        fun setValueFrom(valueFrom: String): Builder {
            this.valueFrom = valueFrom
            return this
        }

        fun setValueTo(valueTo: String): Builder {
            this.valueTo = valueTo
            return this
        }

        fun setCursorItem(cursorItem: CursorItem): Builder {
            this.cursorItem = cursorItem
            return this
        }

        fun create(): InputItem {

            if (valueTo == null) {
                throw NullPointerException(InputItem::class.java.simpleName + "#valueTo is null")
            }

            if (tag == InputDef.name || tag == InputDef.text || tag == InputDef.roll) {
                if (cursorItem == null) {
                    throw NullPointerException(InputItem::class.java.simpleName + "#cursorItem is null")
                }
            }

            return InputItem(tag, valueFrom!!, valueTo!!, cursorItem, position)
        }

    }

}