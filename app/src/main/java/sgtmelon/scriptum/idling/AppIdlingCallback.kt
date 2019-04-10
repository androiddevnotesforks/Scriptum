package sgtmelon.scriptum.idling

/**
 * Интерфейс для управления [AppIdlingResource]
 *
 * @author SerjantArbuz
 */
interface AppIdlingCallback {

    fun startHardWork()

    fun stopHardWork()

}