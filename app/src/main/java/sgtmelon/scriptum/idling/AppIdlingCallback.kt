package sgtmelon.scriptum.idling

/**
 * Interface for [AppIdlingResource]
 */
interface AppIdlingCallback {

    fun startWork(@IdlingTag tag: String)

    fun stopWork(@IdlingTag tag: String)

    fun changeWork(isWork: Boolean, @IdlingTag tag: String)

    fun clearWork()
}