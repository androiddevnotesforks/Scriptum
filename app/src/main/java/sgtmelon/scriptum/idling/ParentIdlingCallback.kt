package sgtmelon.scriptum.idling

/**
 * Interface for parent class [ParentIdlingResource] and them child.
 */
interface ParentIdlingCallback {

    /**
     * Call only from the tests.
     */
    fun register()

    /**
     * Call only from the tests.
     */
    fun unregister()
}