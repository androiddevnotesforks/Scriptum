package sgtmelon.scriptum.infrastructure.screen.notifications

sealed class UpdateListState {

    object Set : UpdateListState()

    object Notify : UpdateListState()

    class Removed(val p: Int) : UpdateListState()

    object SkipInsert : UpdateListState()

    class Insert(val p: Int) : UpdateListState()
}