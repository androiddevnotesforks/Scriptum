package sgtmelon.scriptum.idling

import androidx.test.espresso.IdlingResource

/**
 * Parent class for idling resources.
 */
abstract class ParentIdlingResource : IdlingResource,
    ParentIdlingCallback {

    protected var callback: IdlingResource.ResourceCallback? = null

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}