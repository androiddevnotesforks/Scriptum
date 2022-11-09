package sgtmelon.scriptum.infrastructure.model.state

import androidx.recyclerview.widget.ListAdapter

/**
 * States for custom updates of [ListAdapter].
 */
sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    object NotifyHard : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()
}