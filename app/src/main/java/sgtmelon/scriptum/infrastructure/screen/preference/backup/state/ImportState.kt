package sgtmelon.scriptum.infrastructure.screen.preference.backup.state


/**
 * State of file import.
 */
sealed class ImportState {

    object ShowLoading : ImportState()

    object HideLoading : ImportState()

    object LoadSuccess : ImportState()

    class LoadSkip(val count: Int) : ImportState()

    object LoadError : ImportState()

    object Finish : ImportState()
}