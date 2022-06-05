package sgtmelon.scriptum.presentation.control.snackbar

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import sgtmelon.common.utils.getDrawableCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.*

/**
 * Class for help control showing snackbar's.
 */
class SnackbarControl(
    @StringRes private val messageId: Int,
    @StringRes private val actionId: Int,
    private val callback: SnackbarCallback
) : ISnackbarControl {

    private var snackbar: Snackbar? = null

    private val dismissCallback = object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            /**
             * If user not click on action button (this mean: timeout dismiss, dismiss by swipe).
             */
            if (event != DISMISS_EVENT_ACTION) {
                callback.onSnackbarDismiss()
            }
        }
    }


    override fun show(parent: ViewGroup, withInsets: Boolean) {
        /**
         * Need remove callback before dismiss for prevent call [Snackbar.Callback.onDismissed].
         */
        snackbar?.removeCallback(dismissCallback)
        snackbar?.dismiss()

        Snackbar.make(parent, messageId, Snackbar.LENGTH_LONG)
            .setAction(actionId) { callback.onSnackbarAction() }
            .addCallback(dismissCallback)
            .setupTheme()
            .also { snackbar = it }
            .show()

        if (withInsets) {
            snackbar?.view?.doOnApplyWindowInsets { view, insets, _, _, margin ->
                view.updateMargin(InsetsDir.LEFT, insets, margin)
                view.updateMargin(InsetsDir.RIGHT, insets, margin)
                view.updateMargin(InsetsDir.BOTTOM, insets, margin)
                return@doOnApplyWindowInsets insets
            }
        }
    }

    override fun dismiss() {
        snackbar?.dismiss()
    }

    /**
     * If without callback it mean that [SnackbarCallback.onSnackbarDismiss] will not
     * call on dismiss.
     */
    override fun dismiss(withCallback: Boolean) {
        if (!withCallback) {
            snackbar?.removeCallback(dismissCallback)
        }

        snackbar?.dismiss()
    }

    private fun Snackbar.setupTheme() = apply {
        val background = view.context.getDrawableCompat(R.drawable.bg_snackbar)
        val textColor = view.context.getColorAttr(R.attr.clContent)
        val actionColor = view.context.getColorAttr(R.attr.clAccent)

        view.background = background
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(textColor)
        setActionTextColor(actionColor)
    }
}