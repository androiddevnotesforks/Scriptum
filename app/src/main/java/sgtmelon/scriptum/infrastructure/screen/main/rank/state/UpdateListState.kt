package sgtmelon.scriptum.infrastructure.screen.main.rank.state

@Deprecated("Use single class for this staff: check same class in notifications package")
sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    object NotifyHard : UpdateListState()

    class Remove(val p: Int) : UpdateListState()

    class Insert(val p: Int) : UpdateListState()

}