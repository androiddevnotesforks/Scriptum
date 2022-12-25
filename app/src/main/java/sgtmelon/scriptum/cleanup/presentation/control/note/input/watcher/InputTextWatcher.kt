package sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.data.noteHistory.HistoryItem
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl

/**
 * Text watcher of enter text for [NoteHistoryImpl]
 */
class InputTextWatcher(
    private val view: EditText?,
    @InputAction private val tag: Int,
    private val callback: Callback,
    private val history: NoteHistory
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

        val cursorItem = HistoryItem.Cursor(cursorFrom, cursorTo)

        when (tag) {
            InputAction.NAME -> history.onNameChange(textFrom, textTo, cursorItem)
            InputAction.TEXT -> history.onTextChange(textFrom, textTo, cursorItem)
        }

        textFrom = textTo
        cursorFrom = cursorTo
    }

    override fun afterTextChanged(s: Editable?) = callback.onInputTextChange()

    interface Callback {
        fun onInputTextChange()
    }

}