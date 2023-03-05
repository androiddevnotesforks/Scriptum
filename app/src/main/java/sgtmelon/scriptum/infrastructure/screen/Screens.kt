package sgtmelon.scriptum.infrastructure.screen

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.bundle.encode
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceScreen
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashOpen
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Class providing access to screen intents.
 */
object Screens {

    object Splash {

        fun toMain(context: Context) = context.intent<SplashActivity>()

        private fun get(context: Context, open: SplashOpen): Intent {
            return context.intent<SplashActivity>(IntentData.Splash.Key.OPEN to open.encode())
        }

        fun toAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return get(context, SplashOpen.Alarm(noteId)).addFlags(flags)
        }

        fun toNotification(context: Context) = get(context, SplashOpen.Notifications)

        @Deprecated("Remove after help disappear refactor")
        fun toHelpDisappear(context: Context) = get(context, SplashOpen.HelpDisappear)

        fun toBindNote(context: Context, item: NoteItem): Intent {
            return get(context, with(item) { SplashOpen.BindNote(id, type, color, name) })
        }

        /** This intent also used inside xml/shortcuts.xml. */
        fun toNewNote(context: Context, type: NoteType) = get(context, SplashOpen.NewNote(type))

    }

    fun toMain(context: Context) = context.intent<MainActivity>()

    fun toNotifications(context: Context) = context.intent<NotificationsActivity>()

    fun toPreference(context: Context, screen: PreferenceScreen): Intent {
        return context.intent<PreferenceActivity>(IntentData.Preference.Key.SCREEN to screen)
    }

    fun toAlarm(context: Context, noteId: Long): Intent {
        return context.intent<AlarmActivity>(IntentData.Note.Key.ID to noteId)
    }

    @Deprecated("Remove after help disappear refactor")
    fun toHelpDisappear(context: Context) = context.intent<HelpDisappearActivity>()
}