package sgtmelon.scriptum.cleanup.extension

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import sgtmelon.scriptum.infrastructure.utils.extensions.isVisible
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

@SuppressLint("ClickableViewAccessibility")
fun View.requestFocusOnVisible(editText: EditText?) = setOnTouchListener { _, event ->
    if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false

    editText?.let { if (it.isVisible()) it.requestSelectionFocus() }

    return@setOnTouchListener false
}