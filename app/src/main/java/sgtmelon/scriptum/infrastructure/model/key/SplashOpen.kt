package sgtmelon.scriptum.infrastructure.model.key

import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Describes which screen need open after splash.
 */
sealed class SplashOpen {

    object Intro : SplashOpen()

    object Simple : SplashOpen()

    class Alarm(val id: Long) : SplashOpen()

    class Note(val id: Long, val color: Int, val type: Int) : SplashOpen()

    object Notifications : SplashOpen()

    object HelpDisappear : SplashOpen()

    class CreateNote(val type: NoteType) : SplashOpen()

}