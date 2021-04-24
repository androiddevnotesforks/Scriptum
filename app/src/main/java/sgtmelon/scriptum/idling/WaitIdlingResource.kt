package sgtmelon.scriptum.idling

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource

/**
 * [IdlingResource] which will idle when [waitMillis] left.
 */
class WaitIdlingResource : ParentIdlingResource(), WaitIdlingCallback {

    private var startTime: Long? = null
    private var waitMillis: Long = 0

    override fun getName(): String = TAG

    override fun isIdleNow(): Boolean {
        val startTime = startTime ?: return true
        val timeDiff = System.currentTimeMillis() - startTime
        val isIdle = timeDiff >= waitMillis

        if (isIdle) {
            callback?.onTransitionToIdle()

            this.startTime = null
            this.waitMillis = 0
        }

        return isIdle
    }

    override fun fireWork(waitMillis: Long) {
        this.startTime = System.currentTimeMillis()
        this.waitMillis = waitMillis
    }

    override fun register() {
        IdlingRegistry.getInstance().register(this)
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