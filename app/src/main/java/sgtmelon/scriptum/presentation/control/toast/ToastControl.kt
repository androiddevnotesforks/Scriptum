package sgtmelon.scriptum.presentation.control.toast

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import sgtmelon.scriptum.idling.WaitIdlingResource

/**
 * Class for displaying toast's with on time destroy for prevent memory leaks.
 */
class ToastControl(val context: Context?) : IToastControl {

    // TODO Добавить assert'ы в UI тесты для текста внутри toast

    private var currentToast: Toast? = null

    override fun show(@StringRes stringId: Int, length: Int) {
        if (context == null) return

        show(context.getString(stringId), length)
    }

    override fun show(string: String, length: Int) {
        if (context == null) return

        val toast = Toast.makeText(context, string, length)

        currentToast?.cancel()
        currentToast = toast

        toast.show()

        runToastIdling(length)
    }

    /**
     * Run idling while toast is shown.
     */
    private fun runToastIdling(length: Int) {
        WaitIdlingResource.getInstance().fireWork(waitMillis = when (length) {
            Toast.LENGTH_SHORT -> 2000
            Toast.LENGTH_LONG -> 3500
            else -> return
        })
    }

    fun onDestroy() {
        currentToast?.cancel()
        currentToast = null
    }
}