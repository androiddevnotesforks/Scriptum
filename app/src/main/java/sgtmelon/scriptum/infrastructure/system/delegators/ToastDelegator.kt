package sgtmelon.scriptum.infrastructure.system.delegators

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import sgtmelon.test.idling.getWaitIdling

/**
 * Class, which delegates displaying of toast's. And destroy them for preventing memory leaks.
 */
// TODO Добавить assert'ы в UI тесты для текста внутри toast
class ToastDelegator(lifecycle: Lifecycle?) : DefaultLifecycleObserver {

    init {
        lifecycle?.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }

    private var lastToast: Toast? = null

    fun show(context: Context?, @StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) {
        if (context == null) return

        show(context, context.getString(stringId), length)
    }

    fun show(context: Context?, string: String, length: Int = Toast.LENGTH_SHORT) {
        if (context == null) return

        lastToast?.cancel()
        Toast.makeText(context, string, length)
            .also { lastToast = it }
            .show()

        runToastIdling(length)
    }

    private fun runToastIdling(length: Int) {
        val millis = when (length) {
            Toast.LENGTH_SHORT -> 2000L
            Toast.LENGTH_LONG -> 3500L
            else -> return
        }

        /** Run idling while toast is shown. */
        getWaitIdling().start(millis)
    }

    fun cancel() {
        lastToast?.cancel()
        lastToast = null
    }
}