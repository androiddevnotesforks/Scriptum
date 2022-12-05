package sgtmelon.scriptum.infrastructure.screen.main.rank

/**
 * State of adding rank by name
 */
sealed class AddState {

    object Deny : AddState()

    object Prepare : AddState()

    object Complete : AddState()
}