package sgtmelon.scriptum.infrastructure.model.state

import androidx.recyclerview.widget.ListAdapter

/**
 * States for custom updates of [ListAdapter].
 */
sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    object NotifyHard : UpdateListState()

    class Change(val p: Int) : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()

    class Move(val from: Int, val to: Int) : UpdateListState()

    companion object {
        /**
         * If list size equals 1 -> need just show list without animation, because of
         * animation glitch.
         */
        fun chooseInsert(size: Int, p: Int): UpdateListState {
            return if (size == 1) NotifyHard else Insert(p)
        }
    }
}