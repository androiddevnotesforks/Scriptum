package sgtmelon.common.test.idling.impl

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import sgtmelon.common.test.idling.callback.ParentIdlingCallback

/**
 * Parent class for idling resources.
 */
abstract class ParentIdlingResource : IdlingResource,
    ParentIdlingCallback {

    protected var callback: IdlingResource.ResourceCallback? = null

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    override fun register() {
        IdlingRegistry.getInstance().register(this)
    }
}