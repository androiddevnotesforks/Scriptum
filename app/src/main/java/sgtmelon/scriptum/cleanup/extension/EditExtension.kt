package sgtmelon.scriptum.cleanup.extension

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import sgtmelon.scriptum.infrastructure.utils.extensions.showKeyboard

/**
 * Return false - close keyboard.
 */
inline fun EditText.addOnNextAction(crossinline func: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_NEXT) {
            func()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

/**
 * Return false - close keyboard.
 */
inline fun EditText.addOnDoneAction(crossinline func: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_DONE) {
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