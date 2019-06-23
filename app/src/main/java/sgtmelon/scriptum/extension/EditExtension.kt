package sgtmelon.scriptum.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.addOnNextAction(func: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_NEXT) {
            func()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

fun EditText.requestSelectionFocus() {
    if (!hasFocus()) requestFocus()
    setSelection(text.toString().length)
    showKeyboard()
}

fun View.requestFocusOnVisible(editText: EditText?) = setOnTouchListener { _, event ->
    if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false

    editText?.let { if (it.visibility == View.VISIBLE) it.requestSelectionFocus() }

    return@setOnTouchListener false
}

fun String.clearSpace() = trim().replace("\\s+".toRegex(), replacement = " ")

fun EditText.addTextChangedListener(before: (String) -> Unit = {},
                                    on: (String) -> Unit = {},
                                    after: (String) -> Unit = {}) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                before(s.toString())

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                on(s.toString())

        override fun afterTextChanged(s: Editable?) = after(s.toString())
    })
}