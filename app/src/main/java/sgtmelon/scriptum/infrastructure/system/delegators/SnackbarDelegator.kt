package sgtmelon.scriptum.infrastructure.system.delegators

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import sgtmelon.extensions.getColorAttr
import sgtmelon.extensions.getDimen
import sgtmelon.extensions.getDrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.InsetsDir
import sgtmelon.scriptum.infrastructure.utils.extensions.insets.setMarginInsets

class SnackbarDelegator(
    lifecycle: Lifecycle,
    @StringRes private val messageId: Int,
    @StringRes private val actionId: Int,
    private val callback: Callback
) : DefaultLifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        /**
         * Hide (not cancel) snackbar for control leave screen case. We don't want to lost saved
         * snackbar stack during rotation/app close/ect. So, need restore
         * [snackbar] after screen reopen - [onResume].
         */
        hide()
    }

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

    val isDisplayed: Boolean get() = snackbar.let { it != null && it.isShown }

    fun show(parent: ViewGroup, withInsets: Boolean) {
        /** Need remove callback before dismiss for preventing [Snackbar.Callback.onDismissed]. */
        snackbar?.removeCallback(dismissCallback)
        snackbar?.dismiss()

        Snackbar.make(parent, messageId, DISMISS_TIMEOUT)
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

        /** Set height to be sure, for beautiful radius */
        view.layoutParams.height = context.getDimen(R.dimen.snackbar_height)
        view.background = background
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(textColor)
        setActionTextColor(actionColor)
    }

    /**
     * Dismiss snackbar with ability to restore data and [show] it in future.
     */
    fun hide() = dismiss(skipDismissResult = true)

    /**
     * Dismiss snackbar without save data (with [dismissCallback] call).
     */
    fun cancel() = dismiss(skipDismissResult = false)

    /**
     * [skipDismissResult] means that [Callback.onSnackbarDismiss] will not be called
     * on [snackbar] dismiss.
     */
    private fun dismiss(skipDismissResult: Boolean) {
        if (skipDismissResult) {
            snackbar?.removeCallback(dismissCallback)
        }

        snackbar?.dismiss()
        snackbar = null
    }

    interface Callback {
        fun onSnackbarAction()
        fun onSnackbarDismiss()
    }

    companion object {
        const val DISMISS_TIMEOUT = 6000
    }
}