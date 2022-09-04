package sgtmelon.test.idling.callback

import sgtmelon.test.idling.impl.AppIdlingResource

/**
 * Interface for work with [AppIdlingResource].
 */
interface AppIdlingCallback : ParentIdlingCallback {

    fun startWork(tag: String)

    fun stopWork(tag: String)

    fun changeWork(isWork: Boolean, tag: String)
}