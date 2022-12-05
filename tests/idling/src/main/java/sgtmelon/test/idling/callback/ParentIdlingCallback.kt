package sgtmelon.test.idling.callback

import sgtmelon.test.idling.impl.ParentIdlingResource

/**
 * Interface for parent class [ParentIdlingResource] and them child. Use only from UI tests.
 */
interface ParentIdlingCallback {

    fun register()

    fun unregister()
}