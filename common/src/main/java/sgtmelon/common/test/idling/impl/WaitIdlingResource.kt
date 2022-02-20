package sgtmelon.common.test.idling.impl

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import sgtmelon.common.BuildConfig
import sgtmelon.common.test.idling.callback.WaitIdlingCallback

/**
 * [IdlingResource] which will idle when [waitMillis] left.
 */
class WaitIdlingResource : ParentIdlingResource(), WaitIdlingCallback {

    private var startTime: Long? = null
    private var waitMillis: Long = 0

    private fun reset() {
        startTime = null
        waitMillis = 0
    }

    override fun getName(): String = TAG

    override fun isIdleNow(): Boolean {
        val startTime = startTime ?: return true
        val timeDiff = System.currentTimeMillis() - startTime
        val isIdle = timeDiff >= waitMillis

        if (isIdle) {
            callback?.onTransitionToIdle()
            reset()
        }

        return isIdle
    }

    override fun startWork(waitMillis: Long) {
        if (!BuildConfig.DEBUG) return

        this.startTime = System.currentTimeMillis()
        this.waitMillis = waitMillis
    }

    override fun unregister() {
        if (isIdleNow) {
            callback?.onTransitionToIdle()
        }

        IdlingRegistry.getInstance().unregister(this)
        worker = null
    }

    companion object {
        private val TAG = WaitIdlingResource::class.java.simpleName

        private var worker: WaitIdlingCallback? = null

        fun getInstance(): WaitIdlingCallback {
            return worker ?: WaitIdlingResource().also { worker = it }
        }
    }
}