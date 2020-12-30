package sgtmelon.scriptum.presentation.control.snackbar

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.InsetsDir
import sgtmelon.scriptum.extension.doOnApplyWindowInsets
import sgtmelon.scriptum.extension.getCompatColor
import sgtmelon.scriptum.extension.updateMargin

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
             * If user not click on action button (timeout dismiss, dismiss by swipe).
             */
            if (event != DISMISS_EVENT_ACTION) {
                callback.onSnackbarDismiss()
            }
        }
    }


    override fun show(parent: ViewGroup, @Theme theme: Int, withInsets: Boolean) {
        /**
         * Need remove callback before dismiss for prevent call [Snackbar.Callback.onDismissed].
         */
        snackbar?.removeCallback(dismissCallback)
        snackbar?.dismiss()

        Snackbar.make(parent, messageId, Snackbar.LENGTH_LONG)
                .setAction(actionId) { callback.onSnackbarAction() }
                .addCallback(dismissCallback)
                .setTheme(theme)
                .also { snackbar = it }
                .show()

        if (withInsets) {
            snackbar?.view?.doOnApplyWindowInsets { view, insets, _, margin ->
                view.updateMargin(InsetsDir.LEFT, insets, margin)
                view.updateMargin(InsetsDir.RIGHT, insets, margin)
                view.updateMargin(InsetsDir.BOTTOM, insets, margin)
                return@doOnApplyWindowInsets insets
            }
        }
    }

    /**
     * Need remove callback before dismiss for prevent call [Snackbar.Callback.onDismissed].
     */
    override fun dismiss() {
        snackbar?.dismiss()
    }


    private fun Snackbar.setTheme(@Theme theme: Int) = apply {
        val background = view.context.getDrawable(when (theme) {
            Theme.LIGHT -> R.drawable.bg_snackbar_light
            Theme.DARK -> R.drawable.bg_snackbar_dark
            else -> return@apply
        })

        val textColor = view.context.getCompatColor(when (theme) {
            Theme.LIGHT -> R.color.content_light
            Theme.DARK -> R.color.content_dark
            else -> return@apply
        })

        val actionColor = view.context.getCompatColor(when (theme) {
            Theme.LIGHT -> R.color.accent_light
            Theme.DARK -> R.color.accent_dark
            else -> return@apply
        })

        view.background = background
        view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(textColor)
        setActionTextColor(actionColor)
    }

}