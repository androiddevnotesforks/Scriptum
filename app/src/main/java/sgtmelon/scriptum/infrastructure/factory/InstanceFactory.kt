package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.develop.infrastructure.screen.print.PrintDevelopActivity
import sgtmelon.scriptum.infrastructure.model.annotation.AppOpenFrom
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.model.key.PreferenceScreen
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.infrastructure.screen.preference.PreferenceActivity
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.test.idling.getWaitIdling

/**
 * Factory for build intents and get access to them from one place.
 */
object InstanceFactory {

    object Splash {

        operator fun get(context: Context): Intent = Intent(context, SplashActivity::class.java)

        fun getAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            return get(context)
                .addFlags(flags)
                .putExtra(AppOpenFrom.INTENT_KEY, AppOpenFrom.ALARM)
                .putExtra(IntentData.Note.Intent.ID, noteId)
        }

        fun getBind(context: Context, item: NoteItem): Intent {
            return get(context)
                .putExtra(AppOpenFrom.INTENT_KEY, AppOpenFrom.BIND_NOTE)
                .putExtra(IntentData.Note.Intent.ID, item.id)
                .putExtra(IntentData.Note.Intent.COLOR, item.color)
                .putExtra(IntentData.Note.Intent.TYPE, item.type.ordinal)
        }

        fun getNotification(context: Context): Intent {
            return get(context)
                .putExtra(AppOpenFrom.INTENT_KEY, AppOpenFrom.NOTIFICATIONS)
        }

        fun getHelpDisappear(context: Context): Intent {
            return get(context)
                .putExtra(AppOpenFrom.INTENT_KEY, AppOpenFrom.HELP_DISAPPEAR)
        }

        /**
         * This instance also used inside xml/shortcuts.xml
         */
        fun getNewNote(context: Context, type: NoteType): Intent {
            val key = when (type) {
                NoteType.TEXT -> AppOpenFrom.CREATE_TEXT
                NoteType.ROLL -> AppOpenFrom.CREATE_ROLL
            }

            return Intent(context, SplashActivity::class.java)
                .putExtra(AppOpenFrom.INTENT_KEY, key)
        }
    }

    object Main {

        operator fun get(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    object Note {

        operator fun get(context: Context, item: NotificationItem): Intent {
            with(item.note) {
                return get(
                    context, isEdit = false, NoteState.EXIST, type.ordinal, id, color.ordinal, name
                )
            }
        }

        operator fun get(context: Context, item: NoteItem, state: NoteState): Intent {
            with(item) {
                return get(context, isEdit = false, state, type.ordinal, id, color.ordinal, name)
            }
        }

        /**
         * If [id] and [color] have default values - it means that note will be created,
         * otherwise it will be opened (from bind/notes/bin/alarm/notifications).
         */
        operator fun get(
            context: Context,
            isEdit: Boolean,
            state: NoteState,
            type: Int,
            id: Long = IntentData.Note.Default.ID,
            color: Int = IntentData.Note.Default.COLOR,
            name: String = IntentData.Note.Default.NAME
        ): Intent {
            return Intent(context, NoteActivity::class.java)
                .putExtra(IntentData.Note.Intent.IS_EDIT, isEdit)
                .putExtra(IntentData.Note.Intent.STATE, state.ordinal)
                .putExtra(IntentData.Note.Intent.ID, id)
                .putExtra(IntentData.Note.Intent.TYPE, type)
                .putExtra(IntentData.Note.Intent.COLOR, color)
                .putExtra(IntentData.Note.Intent.NAME, name)
        }
    }

    object Alarm {

        operator fun get(context: Context, id: Long): Intent {
            return Intent(context, AlarmActivity::class.java)
                .putExtra(IntentData.Note.Intent.ID, id)
        }
    }

    object Notifications {

        operator fun get(context: Context): Intent {
            return Intent(context, NotificationsActivity::class.java)
        }
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

        fun toNote(
            context: Context,
            data: SplashOpen.BindNote
        ): Array<Intent> = waitOpen {
            arrayOf(
                Main[context],
                with(data) { Note[context, false, NoteState.EXIST, type, id, color, name] }
            )
        }

        fun toNote(context: Context, type: NoteType): Array<Intent> = waitOpen {
            arrayOf(Main[context], Note[context, true, NoteState.CREATE, type.ordinal])
        }

        fun toNotifications(context: Context): Array<Intent> = waitOpen {
            arrayOf(Main[context], Notifications[context])
        }

        fun toHelpDisappear(context: Context): Array<Intent> = waitOpen {
            arrayOf(
                Main[context],
                Preference[context, PreferenceScreen.MENU],
                Preference[context, PreferenceScreen.HELP],
                Preference.HelpDisappear[context]
            )
        }
    }
}