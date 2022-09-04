package sgtmelon.test.idling.callback

import sgtmelon.test.idling.impl.WaitIdlingResource

/**
 * Interface for work with [WaitIdlingResource].
 */
interface WaitIdlingCallback : ParentIdlingCallback {

    fun startWork(waitMillis: Long)
}