package sgtmelon.scriptum.control.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.showToast

/**
 * Class for help control clipboard
 */
class ClipboardControl(private val context: Context?) : IClipboardControl {

    private val manager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    override fun copy(text: String) {
        val context = context ?: return

        manager?.let {
            it.primaryClip = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
            context.showToast(context.getString(R.string.toast_text_copy))
        }
    }

    /**
     * Callback which need implement in interface what pass to Interactor
     * It's need to get access [ClipboardControl] inside Interactor
     */
    interface Bridge {
        fun copyClipboard(text: String)
    }

}