package sgtmelon.scriptum.cleanup.presentation.control.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.annotation.MainThread
import sgtmelon.extensions.getClipboardService
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IClipboardControl
import sgtmelon.scriptum.infrastructure.system.delegators.ToastDelegator

/**
 * Class for help control clipboard
 */
class ClipboardControl(
    private val context: Context,
    private val toast: ToastDelegator
) : IClipboardControl {

    private val manager: ClipboardManager = context.getClipboardService()

    override fun copy(text: String) {
        manager.let {
            val clipData = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
            it.setPrimaryClip(clipData)

            toast.show(context, context.getString(R.string.toast_text_copy))
        }
    }

    /**
     * Interface for fast access to this class.
     */
    interface Bridge {
        @MainThread fun copyClipboard(text: String)
    }
}