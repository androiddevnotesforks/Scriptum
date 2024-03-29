package sgtmelon.scriptum.infrastructure.listener

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import sgtmelon.extensions.emptyString
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange as Change

/**
 * Text watcher for track changes in [view], needed for [NoteHistoryImpl].
 */
class HistoryTextWatcher(
    private val view: EditText,
    private val callback: Callback,
    private val getAction: (value: Change<String>, cursor: Change<Int>) -> HistoryAction?
) : TextWatcher {

    private var valueFrom = emptyString()
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        valueFrom = s.toString()
        cursorFrom = view.selectionEnd
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val valueTo = s.toString()
        val cursorTo = view.selectionEnd

        if (valueFrom == valueTo) return

        val action = getAction(Change(valueFrom, valueTo), Change(cursorFrom, cursorTo))
        if (action != null) {
            callback.onHistoryAdd(action)
        }

        valueFrom = valueTo
        cursorFrom = cursorTo
    }

    override fun afterTextChanged(s: Editable?) = callback.onHistoryEnterChanged(s.toString())

    interface Callback {
        fun onHistoryAdd(action: HistoryAction)
        fun onHistoryEnterChanged(text: String)
    }
}