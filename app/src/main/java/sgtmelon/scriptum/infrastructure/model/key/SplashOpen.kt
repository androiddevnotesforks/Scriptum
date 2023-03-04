package sgtmelon.scriptum.infrastructure.model.key

import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Describes which screen need open after splash.
 */
@Serializable
sealed class SplashOpen {

    @Serializable
    object Main : SplashOpen()

    @Serializable
    data class Alarm(val noteId: Long) : SplashOpen()

    @Serializable
    data class BindNote(
        val noteId: Long,
        val type: Int,
        val color: Int,
        val name: String
    ) : SplashOpen()

    @Serializable
    object Notifications : SplashOpen()

    @Serializable
    object HelpDisappear : SplashOpen()

    @Serializable
    data class CreateNote(val type: NoteType) : SplashOpen()

}