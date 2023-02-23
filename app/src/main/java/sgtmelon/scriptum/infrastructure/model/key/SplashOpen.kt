package sgtmelon.scriptum.infrastructure.model.key

import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Describes which screen need open after splash.
 */
sealed class SplashOpen {

    object Main : SplashOpen()

    data class Alarm(val id: Long) : SplashOpen()

    data class BindNote(
        val id: Long,
        val type: Int,
        val color: Int,
        val name: String
    ) : SplashOpen()

    object Notifications : SplashOpen()

    object HelpDisappear : SplashOpen()

    data class CreateNote(val type: NoteType) : SplashOpen()

}