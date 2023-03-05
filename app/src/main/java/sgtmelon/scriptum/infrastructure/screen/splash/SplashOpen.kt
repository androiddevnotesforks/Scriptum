package sgtmelon.scriptum.infrastructure.screen.splash

import android.content.Context
import android.content.Intent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
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

        override fun getIntents(context: Context): Array<Intent> = arrayOf(MainActivity[context])
    }

    @Serializable
    data class Alarm(val noteId: Long) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                MainActivity[context],
                AlarmActivity[context, noteId]
            )
        }
    }

    // TODO после того, как сделаешь сериализацию для noteItem можно её сюда будет передавать и спокойно юзать (сейчас ошибку выдаст)
    @Serializable
    data class BindNote(
        val noteId: Long,
        @SerialName("noteType") val type: NoteType,
        val color: Color,
        val name: String
    ) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                MainActivity[context],
                // TODO make it shorter
                InstanceFactory.Note[context, false, NoteState.EXIST, type.ordinal, noteId, color.ordinal, name]
            )
        }
    }

    @Serializable
    object Notifications : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                MainActivity[context],
                NotificationsActivity[context]
            )
        }
    }

    @Serializable
    @Deprecated("Remove after help disappear refactor")
    object HelpDisappear : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                MainActivity[context],
                PreferenceActivity[context, PreferenceScreen.MENU],
                PreferenceActivity[context, PreferenceScreen.HELP],
                InstanceFactory.Preference.HelpDisappear[context]
            )
        }
    }

    @Serializable
    data class NewNote(@SerialName("noteType") val type: NoteType) : SplashOpen() {

        override fun getIntents(context: Context): Array<Intent> {
            return arrayOf(
                MainActivity[context],
                InstanceFactory.Note[context, true, NoteState.CREATE, type.ordinal]
            )
        }
    }
}