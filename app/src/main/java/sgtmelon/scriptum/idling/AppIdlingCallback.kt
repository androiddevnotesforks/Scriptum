package sgtmelon.scriptum.idling

/**
 * Interface for [AppIdlingResource]
 */
internal interface AppIdlingCallback {

    fun startHardWork()

    fun stopHardWork()

    fun clearWork()
}