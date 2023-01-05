package sgtmelon.scriptum.infrastructure.listener

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import sgtmelon.scriptum.cleanup.extension.requestSelectionFocus
import sgtmelon.scriptum.infrastructure.utils.extensions.isVisible

/**
 * Touch listener for make selection on [editText] if it's visible and had
 * [MotionEvent.ACTION_DOWN] event.
 *
 * Example of usage: If you some scrollable container and [editText] within (but it's not
 * fully width). And when user touch scrollable container - this listener will make a selection
 * for [editText].
 */
@SuppressLint("ClickableViewAccessibility")
class OnTouchSelectionListener(private val editText: EditText?) : View.OnTouchListener {

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (editText == null || event == null) return false

        if (event.action == MotionEvent.ACTION_DOWN && editText.isVisible()) {
            editText.requestSelectionFocus()
        }

        return false
    }
}