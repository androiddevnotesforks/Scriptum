package sgtmelon.scriptum.presentation.control.system

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.annotation.MainThread
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.showToast
import sgtmelon.scriptum.presentation.control.system.callback.IClipboardControl

/**
 * Class for help control clipboard
 */
class ClipboardControl(private val context: Context?) : IClipboardControl {

    private val manager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    override fun copy(text: String) {
        val context = context ?: return

        manager?.let {
            val clipData = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
            it.setPrimaryClip(clipData)

            context.showToast(context.getString(R.string.toast_text_copy))
        }
    }

    /**
     * Callback which need implement in interface what pass to Interactor.
     * It's need to get access [ClipboardControl] inside Interactor.
     */
    interface Bridge {
        @MainThread fun copyClipboard(text: String)
    }

}