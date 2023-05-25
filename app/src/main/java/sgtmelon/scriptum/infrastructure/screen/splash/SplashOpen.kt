package sgtmelon.scriptum.infrastructure.screen.splash

import android.content.Context
import android.content.Intent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.Screens

/**
 * Describes which screen need open after splash.
 *
 * Some serializations from here is used inside xml/shortcuts.xml, be carefully with:
 * - Change class names
 * - Moving this class into another directory
 */
@Serializable
sealed class SplashOpen {

    @Serializable
    object Main : SplashOpen() {

        fun getIntent(context: Context): Intent = Screens.toMain(context)
    }

    @Serializable
    object NotificationsHelp : SplashOpen() {

        fun getIntent(context: Context): Intent = Screens.toMain(context)
    }

    @Serializable
    object Notifications : SplashOpen() {

        fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.toNotifications(context))
        }
    }

    @Serializable
    data class Alarm(private val noteId: Long) : SplashOpen() {

        fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.toAlarm(context, noteId))
        }
    }

    @Serializable
    data class BindNote(private val item: NoteItem) : SplashOpen() {

        fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.Note.toExist(context, item))
        }
    }

    @Serializable
    data class NewNote(@SerialName("noteType") val type: NoteType) : SplashOpen() {

        fun getIntents(context: Context, item: NoteItem): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.Note.toNew(context, item))
        }
    }
}