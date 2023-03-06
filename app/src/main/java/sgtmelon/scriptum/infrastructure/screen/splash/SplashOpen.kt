package sgtmelon.scriptum.infrastructure.screen.splash

import android.content.Context
import android.content.Intent
import kotlinx.serialization.SerialName
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.Screens
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen

/**
 * Describes which screen need open after splash.
 *
 * Some serializations from here is used inside xml/shortcuts.xml, be carefully with:
 * - Change class names
 * - Moving this class into another directory
 */
@Serializable
sealed class SplashOpen {

    abstract fun getIntents(context: Context): Array<Intent>

    @Serializable
    object Main : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> = arrayOf(Screens.toMain(context))
    }

    @Serializable
    object Notifications : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.toNotifications(context))
        }
    }

    @Serializable
    data class Alarm(val noteId: Long) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.toAlarm(context, noteId))
        }
    }

    @Serializable
    @Deprecated("Remove after help disappear refactor")
    object HelpDisappear : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                Screens.toMain(context),
                Screens.toPreference(context, PreferenceScreen.MENU),
                Screens.toPreference(context, PreferenceScreen.HELP),
                Screens.toHelpDisappear(context)
            )
        }
    }

    /**
     * TODO после того, как сделаешь сериализацию для noteItem можно её сюда будет передавать и
     *      спокойно юзать (сейчас ошибку выдаст)
     */
    @Serializable
    data class BindNote(
        val id: Long,
        @SerialName("noteType") val type: NoteType,
        val color: Color,
        val name: String
    ) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                Screens.toMain(context),
                Screens.Note.toExist(context, type, id, color, name)
            )
        }
    }

    @Serializable
    data class NewNote(@SerialName("noteType") val type: NoteType) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(Screens.toMain(context), Screens.Note.toNew(context, type))
        }
    }
}