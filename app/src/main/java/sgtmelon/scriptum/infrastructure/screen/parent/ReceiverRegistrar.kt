package sgtmelon.scriptum.infrastructure.screen.parent

/**
 * Common interface for register/unregister receivers.
 */
interface ReceiverRegistrar {
    // TODO add intent filter?
    fun registerReceivers() = Unit
    fun unregisterReceivers() = Unit
}