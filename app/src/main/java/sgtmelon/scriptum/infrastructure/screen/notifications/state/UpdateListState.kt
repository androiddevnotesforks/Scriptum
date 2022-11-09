package sgtmelon.scriptum.infrastructure.screen.notifications.state

sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    object NotifyHard : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()
}