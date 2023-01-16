package sgtmelon.scriptum.cleanup.extension

import android.widget.EditText
import sgtmelon.scriptum.infrastructure.utils.extensions.showKeyboard

fun EditText.requestSelectionFocus() {
    if (!hasFocus()) requestFocus()
    setSelection(text.toString().length)
    showKeyboard()
}