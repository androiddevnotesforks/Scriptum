package sgtmelon.scriptum.idling

/**
 * Interface for [AppIdlingResource]
 */
internal interface AppIdlingCallback {

    fun startHardWork(@IdlingTag tag: String)

    fun stopHardWork(@IdlingTag tag: String)

    fun clearWork()
}