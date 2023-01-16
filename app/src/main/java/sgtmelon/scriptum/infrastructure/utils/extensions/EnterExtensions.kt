package sgtmelon.scriptum.infrastructure.utils.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

inline fun EditText.setEditorNextAction(crossinline func: () -> Unit) {
    setOnEditorAction(EditorInfo.IME_ACTION_NEXT, func)
}

inline fun EditText.setEditorDoneAction(crossinline func: () -> Unit) {
    setOnEditorAction(EditorInfo.IME_ACTION_DONE, func)
}

/**
 * Return false value will close a keyboard.
 */
inline fun EditText.setOnEditorAction(expected: Int, crossinline func: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == expected) {
            func()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}