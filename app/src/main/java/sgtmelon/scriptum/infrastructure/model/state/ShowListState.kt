package sgtmelon.scriptum.infrastructure.model.state

/**
 * State of list loading process.
 */
sealed class ShowListState {

    object Loading : ShowListState()

    object List : ShowListState()

    object Empty : ShowListState()
}