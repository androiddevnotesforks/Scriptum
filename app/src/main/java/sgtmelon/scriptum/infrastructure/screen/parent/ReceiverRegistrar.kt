package sgtmelon.scriptum.infrastructure.screen.parent

/**
 * Common interface for register/unregister receivers.
 */
interface ReceiverRegistrar {
    fun registerReceivers() = Unit
    fun unregisterReceivers() = Unit
}