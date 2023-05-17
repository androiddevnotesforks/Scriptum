package sgtmelon.scriptum.infrastructure.model.state.list

import androidx.recyclerview.widget.ListAdapter

/**
 * States for custom updates of [ListAdapter].
 */
sealed class UpdateListState {

    object Set : UpdateListState()

    /** Rely on [ListAdapter] power. */
    object Notify : UpdateListState()

    class Change(val p: Int) : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()

    class Move(val from: Int, val to: Int) : UpdateListState()

}