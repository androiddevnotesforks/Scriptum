package sgtmelon.scriptum.infrastructure.system.delegators

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.setMarginInsets

class SnackbarDelegator(
    @StringRes private val messageId: Int,
    @StringRes private val actionId: Int,
    private val callback: Callback
) {

    private var snackbar: Snackbar? = null

    private val dismissCallback = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            /** If user not click on action button (in case: timeout dismiss, dismiss by swipe). */
            if (event != DISMISS_EVENT_ACTION) {
                callback.onSnackbarDismiss()
            }
        }
    }

    fun show(parent: ViewGroup, withInsets: Boolean) {
        /** Need remove callback before dismiss for preventing [Snackbar.Callback.onDismissed]. */
        snackbar?.removeCallback(dismissCallback)
        snackbar?.dismiss()

        Snackbar.make(parent, messageId, Snackbar.LENGTH_LONG)
            .setAction(actionId) { callback.onSnackbarAction() }
            .addCallback(dismissCallback)
            .setupTheme()
            .also { snackbar = it }
            .show()

        if (withInsets) {
            snackbar?.view?.setMarginInsets(InsetsDir.LEFT, InsetsDir.RIGHT, InsetsDir.BOTTOM)
        }
    }

    private fun Snackbar.setupTheme() = apply {
        val context = view.context
        val background = context.getDrawableCompat(R.drawable.bg_snackbar)
        val textColor = context.getColorAttr(R.attr.clContent)
        val actionColor = context.getColorAttr(R.attr.clAccent)

        view.background = background
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(textColor)
        setActionTextColor(actionColor)
    }

    /**
     * [skipDismissResult] means that [Callback.onSnackbarDismiss] will not be called
     * on [snackbar] dismiss.
     */
    fun dismiss(skipDismissResult: Boolean) {
        if (skipDismissResult) {
            snackbar?.removeCallback(dismissCallback)
        }

        snackbar?.dismiss()
    }

    interface Callback {
        fun onSnackbarAction()
        fun onSnackbarDismiss()
    }
}