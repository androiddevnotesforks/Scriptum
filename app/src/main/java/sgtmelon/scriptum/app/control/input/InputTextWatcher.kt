package sgtmelon.scriptum.app.control.input

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import sgtmelon.scriptum.app.model.item.CursorItem

/**
 * Контроллер ввода текста для [InputControl]
 */
class InputTextWatcher(private val view: EditText?,
                       @param:InputDef private val tag: Int,
                       private val result: Result,
                       private val inputCallback: InputCallback
) : TextWatcher {

    private var textFrom = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        textFrom = s.toString()
        cursorFrom = view?.selectionEnd ?: 0
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val textTo = s.toString()
        val cursorTo = view?.selectionEnd ?: 0

        if (textFrom == textTo) return

        val cursorItem = CursorItem(cursorFrom, cursorTo)

        //TODO вынести в интерфейс результата
        when (tag) {
            InputDef.name -> inputCallback.onNameChange(textFrom, textTo, cursorItem)
            InputDef.text -> inputCallback.onTextChange(textFrom, textTo, cursorItem)
        }

        textFrom = textTo
        cursorFrom = cursorTo

        result.onInputTextChangeResult()
    }

    override fun afterTextChanged(s: Editable) {}

    interface Result {
        fun onInputTextChangeResult()
    }

}