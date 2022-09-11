package sgtmelon.scriptum.infrastructure.screen

import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sgtmelon.extensions.runMain

/**
 * Delegator for start some function with [gapTime] delay.
 */
class DelayJobDelegator(private val gapTime: Long) : DefaultLifecycleObserver {

    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var job: Job? = null

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        job?.cancel()
        job = null
    }

    fun run(@MainThread func: () -> Unit) {
        job?.cancel()
        job = ioScope.launch {
            delay(gapTime)
            runMain { func() }
            job = null
        }
    }
}