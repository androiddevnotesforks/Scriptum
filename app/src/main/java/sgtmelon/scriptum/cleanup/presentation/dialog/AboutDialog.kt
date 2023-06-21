package sgtmelon.scriptum.cleanup.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.utils.applyTransparentBackground
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.utils.extensions.startEmailActivity

/**
 * Dialog with information about project.
 */
class AboutDialog : BlankEmptyDialog() {

    private val logoImage get() = dialog?.findViewById<ImageView?>(R.id.about_logo_image)
    private val versionText get() = dialog?.findViewById<TextView?>(R.id.about_version_text)
    private val emailText get() = dialog?.findViewById<TextView?>(R.id.about_email_text)

    private var clickCount = DEF_CLICK

    var hideOpen = DEF_HIDE
        private set

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setView(R.layout.view_about)
            .setCancelable(true)
            .create()
    }

    override fun transformDialog(dialog: Dialog): Dialog {
        return super.transformDialog(dialog)
            .applyTransparentBackground()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)

        clickCount = savedState.getInt(SavedTag.Common.VALUE)
        hideOpen = savedState.getBoolean(SavedTag.Common.KEY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Common.VALUE, clickCount)
        outState.putBoolean(SavedTag.Common.KEY, hideOpen)
    }

    override fun setupView() {
        super.setupView()

        logoImage?.setOnClickListener {
            if (++clickCount == context?.resources?.getInteger(R.integer.pref_develop_open)) {
                hideOpen = true
                dialog?.dismiss()
            }
        }
        versionText?.text = BuildConfig.VERSION_NAME

        emailText?.setOnClickListener {
            val text = emailText?.text?.toString() ?: return@setOnClickListener
            context?.startEmailActivity(toast = null, text, R.string.email_about_subject)
            dialog?.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        clickCount = DEF_CLICK
        hideOpen = DEF_HIDE
    }

    companion object {
        private const val DEF_CLICK = 0
        private const val DEF_HIDE = false
    }
}