package sgtmelon.scriptum.infrastructure.screen.notifications

sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    object NotifyHard : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()
}