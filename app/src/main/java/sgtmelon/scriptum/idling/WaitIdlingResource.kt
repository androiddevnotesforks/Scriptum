package sgtmelon.scriptum.idling

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource

/**
 * [IdlingResource] which will idle when [waitMillis] left.
 */
class WaitIdlingResource(private val waitMillis: Long) : IdlingResource {

    init {
        IdlingRegistry.getInstance().register(this)
    }

    private val startTime: Long = System.currentTimeMillis()

    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = WaitIdlingResource::class.java.simpleName
        .plus(other = " : $waitMillis")

    override fun isIdleNow(): Boolean {
        val timeDiff = System.currentTimeMillis() - startTime
        val isIdle = timeDiff >= waitMillis

        if (isIdle) {
            callback?.onTransitionToIdle()
            IdlingRegistry.getInstance().unregister(this)
        }

        return isIdle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}