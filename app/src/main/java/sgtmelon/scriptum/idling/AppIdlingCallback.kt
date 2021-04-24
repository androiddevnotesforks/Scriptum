package sgtmelon.scriptum.idling

/**
 * Interface for [AppIdlingResource].
 */
interface AppIdlingCallback : ParentIdlingCallback {

    fun startWork(@IdlingTag tag: String)

    fun stopWork(@IdlingTag tag: String)

    fun changeWork(isWork: Boolean, @IdlingTag tag: String)
}