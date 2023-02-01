package sgtmelon.scriptum.cleanup.extension

import android.view.View
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.infrastructure.utils.extensions.showKeyboard

fun EditText.requestSelectionFocus(binding: ViewDataBinding?) = requestSelectionFocus(binding?.root)

fun EditText.requestSelectionFocus(rootView: View? = null) {
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