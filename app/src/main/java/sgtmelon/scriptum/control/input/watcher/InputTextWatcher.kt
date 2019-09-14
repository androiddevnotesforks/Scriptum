package sgtmelon.scriptum.control.input.watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.annotation.InputAction
import sgtmelon.scriptum.model.item.InputItem

/**
 * Контроллер ввода текста для [InputControl]
 */
class InputTextWatcher(private val view: EditText?,
                       @InputAction private val tag: Int,
                       private val changeCallback: TextChange,
                       private val inputCallback: InputCallback
) : TextWatcher {

    private var textFrom = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        textFrom = s.toString()
        cursorFrom = view?.selectionEnd ?: 0
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val textTo = s.toString()
        val cursorTo = view?.selectionEnd ?: 0

        if (textFrom == textTo) return

        val cursorItem = InputItem.Cursor(cursorFrom, cursorTo)

        when (tag) {
            InputAction.NAME -> inputCallback.onNameChange(textFrom, textTo, cursorItem)
            InputAction.TEXT -> inputCallback.onTextChange(textFrom, textTo, cursorItem)
        }

        textFrom = textTo
        cursorFrom = cursorTo
    }

    override fun afterTextChanged(s: Editable?) = changeCallback.onResultInputTextChange()

    interface TextChange {
        fun onResultInputTextChange()
    }

}