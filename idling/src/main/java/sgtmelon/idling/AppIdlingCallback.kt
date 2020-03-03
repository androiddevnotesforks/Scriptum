package sgtmelon.idling

/**
 * Interface for [AppIdlingResource]
 */
internal interface AppIdlingCallback {

    fun startHardWork()

    fun stopHardWork()

}