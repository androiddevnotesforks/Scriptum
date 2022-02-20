package sgtmelon.common.test.idling

/**
 * Interface for [WaitIdlingResource].
 */
interface WaitIdlingCallback : ParentIdlingCallback {

    fun fireWork(waitMillis: Long)
}