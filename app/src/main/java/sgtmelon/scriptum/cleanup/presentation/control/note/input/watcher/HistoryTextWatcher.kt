package sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import sgtmelon.scriptum.data.noteHistory.HistoryAction.Change
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl

/**
 * Text watcher for track changes in [view], needed for [NoteHistoryImpl].
 */
class HistoryTextWatcher(
    private val view: EditText,
    private val callback: Callback,
    private val onEnter: (value: Change<String>, cursor: Change<Int>) -> Unit
) : TextWatcher {

    private var valueFrom = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        valueFrom = s.toString()
        cursorFrom = view.selectionEnd
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val valueTo = s.toString()
        val cursorTo = view.selectionEnd

        if (valueFrom == valueTo) return

        onEnter(Change(valueFrom, valueTo), Change(cursorFrom, cursorTo))

        valueFrom = valueTo
        cursorFrom = cursorTo
    }

    override fun afterTextChanged(s: Editable?) = callback.onHistoryEnterChanged()

    interface Callback {
        fun onHistoryEnterChanged()
    }
}