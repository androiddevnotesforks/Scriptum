package sgtmelon.idling

/**
 * Интерфейс для управления [AppIdlingResource]
 */
interface AppIdlingCallback {

    fun startHardWork()

    fun stopHardWork()

}