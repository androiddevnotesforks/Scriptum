package sgtmelon.scriptum.infrastructure.utils

import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.extensions.runMain

/**
 * Delegator for start some function with delay. Replacement for Handler().postDelayed(..).
 */
internal class DelayedJob(lifecycle: Lifecycle?) : DefaultLifecycleObserver {

    init {
        lifecycle?.addObserver(this)
    }

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancel()
    }

    inline fun start(gapTime: Long, @MainThread crossinline func: () -> Unit) {
        job?.cancel()
        job = ioScope.launch {
            delay(gapTime)
            runMain { func() }
            job = null
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}