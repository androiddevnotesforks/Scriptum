package sgtmelon.common.test.idling.callback

import sgtmelon.common.test.annotation.RunPrivate

/**
 * Interface for parent class [ParentIdlingResource] and them child.
 */
interface ParentIdlingCallback {
    @RunPrivate fun register()
    @RunPrivate fun unregister()
}