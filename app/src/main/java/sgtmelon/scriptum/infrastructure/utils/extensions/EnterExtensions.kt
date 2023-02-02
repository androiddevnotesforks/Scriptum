package sgtmelon.scriptum.infrastructure.utils.extensions

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.ViewDataBinding

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

fun EditText.requestFocusWithCursor(binding: ViewDataBinding?) =
    requestFocusWithCursor(binding?.root)

fun EditText.requestFocusWithCursor(rootView: View? = null) {
    val selectionRunnable = {
        if (!hasFocus()) requestFocus()
        setSelection(text.toString().length)
        showKeyboard()
    }

    /**
     * Post here is needed because sometimes keyboard not shows and selection not applies.
     *
     * Bulletproof way - pass [rootView] and selection will applies when [rootView] will be
     * ready.
     */
    rootView?.post(selectionRunnable) ?: post(selectionRunnable)
}