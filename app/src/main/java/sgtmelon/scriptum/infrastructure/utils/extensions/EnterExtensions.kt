package sgtmelon.scriptum.infrastructure.utils.extensions

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.databinding.ViewDataBinding
import sgtmelon.extensions.emptyString
import kotlin.math.min

inline fun EditText.setEditorNextAction(crossinline func: () -> Unit) {
    setOnEditorAction(EditorInfo.IME_ACTION_NEXT, func)
}

inline fun EditText.setEditorDoneAction(crossinline func: () -> Unit) {
    setOnEditorAction(EditorInfo.IME_ACTION_DONE, func)
}

/**
 * Return FALSE value will close a keyboard.
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
        setSelectionSafe()
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

fun EditText.setTextSelectionSafe(text: String, @IntRange(from = 0) cursor: Int) {
    requestFocus()
    setText(text)
    setSelection(getCorrectCursor(cursor))
}

/**
 * Extension helps to skip this error:
 * java.lang.IndexOutOfBoundsException: setSpan (6 ... 6) ends beyond length 5
 */
fun EditText.setSelectionSafe(@IntRange(from = 0) cursor: Int = length()) {
    requestFocus()
    setSelection(getCorrectCursor(cursor))
}

private fun EditText.getCorrectCursor(value: Int) = min(value, length())

fun EditText.selectAllText() {
    if (text.isNotEmpty()) {
        setSelection(0, length())
    }
    requestFocus()
}

fun EditText.clearText() = setText(emptyString())

/**
 * Set [text] to [EditText] if it's different from the current one.
 */
fun EditText.setTextIfDifferent(text: CharSequence) {
    if (this.text.toString() != text) {
        setText(text)
    }
}