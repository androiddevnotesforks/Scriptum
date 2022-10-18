package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import sgtmelon.extensions.getClipboardService
import sgtmelon.scriptum.R

class ClipboardDelegator(private val context: Context, private val toast: ToastDelegator) {

    private val manager: ClipboardManager = context.getClipboardService()

    fun copy(text: String) {
        val clipData = ClipData.newPlainText(context.getString(R.string.clipboard_label), text)
        manager.setPrimaryClip(clipData)

        toast.show(context, R.string.toast_text_copy)
    }
}