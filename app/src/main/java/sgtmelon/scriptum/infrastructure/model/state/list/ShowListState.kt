package sgtmelon.scriptum.infrastructure.model.state.list

import kotlinx.serialization.Serializable

/**
 * State of list loading process.
 */
@Serializable
sealed class ShowListState {

    @Serializable
    object Loading : ShowListState()

    @Serializable
    object List : ShowListState()

    @Serializable
    object Empty : ShowListState()

}