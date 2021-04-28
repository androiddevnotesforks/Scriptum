package sgtmelon.scriptum.presentation.control.system

import android.content.ClipData
import android.content.Context
import androidx.annotation.MainThread
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getClipboardService
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.control.system.callback.IClipboardControl

/**
 * Class for help control clipboard
 */
class ClipboardControl(private val context: Context?) : IClipboardControl {

    private val manager = context?.getClipboardService()

    override fun copy(text: String) {
        val context = context ?: return

        manager?.let {
            val clipData = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
            it.setPrimaryClip(clipData)

            context.showToast(context.getString(R.string.toast_text_copy))
        }
    }

    /**
     * Interface for fast access to this class.
     */
    interface Bridge {
        @MainThread fun copyClipboard(text: String)
    }
}