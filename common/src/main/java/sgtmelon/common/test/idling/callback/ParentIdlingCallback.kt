package sgtmelon.common.test.idling.callback

import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.common.test.idling.impl.ParentIdlingResource

/**
 * Interface for parent class [ParentIdlingResource] and them child. Use only from UI tests.
 */
interface ParentIdlingCallback {
    @RunPrivate fun register()
    @RunPrivate fun unregister()
}