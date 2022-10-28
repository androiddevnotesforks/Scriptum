package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.develop.model.PrintType
import sgtmelon.scriptum.develop.screen.print.PrintDevelopActivity
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.test.idling.getWaitIdling

/**
 * Factory for build intents and get access to them from one place.
 */
object InstanceFactory {

    object Splash {

        fun getAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            return Intent(context, SplashActivity::class.java)
                .addFlags(flags)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                .putExtra(IntentData.Note.Intent.ID, noteId)
        }

        fun getBind(context: Context, item: NoteItem): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                .putExtra(IntentData.Note.Intent.ID, item.id)
                .putExtra(IntentData.Note.Intent.COLOR, item.color)
                .putExtra(IntentData.Note.Intent.TYPE, item.type.ordinal)
        }

        fun getNotification(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.NOTIFICATIONS)
        }

        fun getHelpDisappear(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.HELP_DISAPPEAR)
        }

        /**
         * This instance also used inside xml/shortcuts.xml
         */
        fun getNewNote(context: Context, type: NoteType): Intent {
            val key = when (type) {
                NoteType.TEXT -> OpenFrom.CREATE_TEXT
                NoteType.ROLL -> OpenFrom.CREATE_ROLL
            }

            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, key)
        }
    }

    object Intro {

        /**
         * After launch this instance application will be restarted with only this screen.
         */
        operator fun get(context: Context): Intent {
            return Intent(context, IntroActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    object Main {

        operator fun get(context: Context) = Intent(context, MainActivity::class.java)
    }

    object Note {

        operator fun get(context: Context, item: NotificationItem) =
            get(context, item.note.type.ordinal, item.note.id, item.note.color.ordinal)

        operator fun get(context: Context, item: NoteItem) =
            get(context, item.type.ordinal, item.id, item.color.ordinal)

        /**
         * If [id] and [color] have default values - it means that note will be created,
         * otherwise it will be opened.
         */
        operator fun get(
            context: Context,
            type: Int,
            id: Long = IntentData.Note.Default.ID,
            color: Int = IntentData.Note.Default.COLOR
        ): Intent {
            return Intent(context, NoteActivity::class.java)
                .putExtra(IntentData.Note.Intent.ID, id)
                .putExtra(IntentData.Note.Intent.COLOR, color)
                .putExtra(IntentData.Note.Intent.TYPE, type)
        }
    }

    object Alarm {

        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                .putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    object Notification {

        operator fun get(context: Context) = Intent(context, NotificationActivity::class.java)
    }


    object Preference {

        operator fun get(
            context: Context,
            screen: PreferenceScreen
        ): Intent {
            return Intent(context, PreferenceActivity::class.java)
                .putExtra(IntentData.Preference.Intent.SCREEN, screen.ordinal)
        }

        object HelpDisappear {

            operator fun get(context: Context): Intent {
                return Intent(context, HelpDisappearActivity::class.java)
            }
        }

        object Develop {

            object Print {

                operator fun get(context: Context, type: PrintType): Intent {
                    return Intent(context, PrintDevelopActivity::class.java)
                        .putExtra(IntentData.Print.Intent.TYPE, type.ordinal)
                }
            }
        }
    }

    object Chains {

        /**
         * Idling before open chain of screens, needed for Android (UI) tests
         */
        private inline fun <T> waitOpen(func: () -> T): T {
            getWaitIdling().start(waitMillis = 3000)
            return func()
        }

        fun toAlarm(context: Context, noteId: Long): Array<Intent> = waitOpen {
            arrayOf(Main[context], Alarm[context, noteId])
        }

        fun toNote(context: Context, noteId: Long, color: Int, type: Int): Array<Intent> {
            return waitOpen { arrayOf(Main[context], Note[context, type, noteId, color]) }
        }

        fun toNotifications(context: Context): Array<Intent> = waitOpen {
            arrayOf(Main[context], Notification[context])
        }

        fun toHelpDisappear(context: Context): Array<Intent> = waitOpen {
            arrayOf(
                Main[context],
                Preference[context, PreferenceScreen.PREFERENCE],
                Preference[context, PreferenceScreen.HELP],
                Preference.HelpDisappear[context]
            )
        }

        fun toNewNote(context: Context, type: NoteType): Array<Intent> = waitOpen {
            arrayOf(Main[context], Note[context, type.ordinal])
        }
    }
}