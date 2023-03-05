package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.screen.preference.disappear.HelpDisappearActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Factory for build intents and get access to them from one place.
 */
object InstanceFactory {

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
                .putExtra(IntentData.Note.Key.IS_EDIT, isEdit)
                .putExtra(IntentData.Note.Key.STATE, state.ordinal)
                .putExtra(IntentData.Note.Key.ID, id)
                .putExtra(IntentData.Note.Key.TYPE, type)
                .putExtra(IntentData.Note.Key.COLOR, color)
                .putExtra(IntentData.Note.Key.NAME, name)
        }
    }


    object Preference {

        @Deprecated("Remove after help disappear refactor")
        object HelpDisappear {

            operator fun get(context: Context): Intent {
                return Intent(context, HelpDisappearActivity::class.java)
            }
        }
    }
}