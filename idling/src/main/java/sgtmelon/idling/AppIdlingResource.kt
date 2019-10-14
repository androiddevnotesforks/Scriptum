package sgtmelon.idling

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource

/**
 * Class for maintain test work while app is freeze without Thread.sleep(...)
 */
class AppIdlingResource : IdlingResource, AppIdlingCallback {

    /**
     * true - don't need wait operation, false - need wait end of operation
     */
    private var idle = true

    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = AppIdlingResource::class.java.simpleName

    override fun isIdleNow() = idle

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    override fun startHardWork() {
        idle = false
    }

    override fun stopHardWork() {
        idle = true
        callback?.onTransitionToIdle()
    }

    companion object {

        val worker by lazy {
            AppIdlingResource().apply {
                IdlingRegistry.getInstance().register(this)
            }
        }

    }

}