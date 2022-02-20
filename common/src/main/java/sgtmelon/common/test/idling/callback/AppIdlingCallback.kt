package sgtmelon.common.test.idling.callback

/**
 * Interface for [AppIdlingResource].
 */
interface AppIdlingCallback : ParentIdlingCallback {

    fun startWork(tag: String)

    fun stopWork(tag: String)

    fun changeWork(isWork: Boolean, tag: String)
}