package sgtmelon.scriptum.watcher

import android.widget.EditText
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.model.key.InputAction

/**
 * Контроллер ввода текста для [InputControl]
 *
 * @author SerjantArbuz
 */
class InputTextWatcher(private val view: EditText?,
                       private val tag: Int,
                       private val textChangeCallback: TextChange,
                       private val inputCallback: InputCallback
) : AppTextWatcher() {

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
            InputAction.name -> inputCallback.onNameChange(textFrom, textTo, cursorItem)
            InputAction.text -> inputCallback.onTextChange(textFrom, textTo, cursorItem)
        }

        textFrom = textTo
        cursorFrom = cursorTo

        textChangeCallback.onResultInputTextChange()
    }

    interface TextChange {
        fun onResultInputTextChange()
    }

}