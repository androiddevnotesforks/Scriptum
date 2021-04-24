package sgtmelon.scriptum.idling

/**
 * Interface for [WaitIdlingResource].
 */
interface WaitIdlingCallback : ParentIdlingCallback {

    fun fireWork(waitMillis: Long)
}