package sgtmelon.scriptum.cleanup.extension

import android.widget.EditText
import sgtmelon.scriptum.infrastructure.utils.extensions.showKeyboard

fun EditText.requestSelectionFocus() {
    if (!hasFocus()) requestFocus()

    /** Post here is needed because sometimes keyboard not shows and selection not applies. */
    post {
        setSelection(text.toString().length)
        showKeyboard()
    }
}