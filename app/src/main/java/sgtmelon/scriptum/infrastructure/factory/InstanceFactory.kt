package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note

/**
 * Factory for build intents.
 */
object InstanceFactory {

    object Splash {

        fun getAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            return Intent(context, SplashActivity::class.java)
                .addFlags(flags)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                .putExtra(Note.Intent.ID, noteId)
        }

        fun getBind(context: Context, item: NoteItem): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                .putExtra(Note.Intent.ID, item.id)
                .putExtra(Note.Intent.COLOR, item.color)
                .putExtra(Note.Intent.TYPE, item.type.ordinal)
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

        /** After launch this instance application will be restarted with only this screen. */
        operator fun get(context: Context): Intent {
            return Intent(context, IntroActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}