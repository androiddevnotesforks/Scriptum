package sgtmelon.common.test.idling.callback

import sgtmelon.common.test.idling.impl.WaitIdlingResource

/**
 * Interface for work with [WaitIdlingResource].
 */
interface WaitIdlingCallback : ParentIdlingCallback {

    fun startWork(waitMillis: Long)
}