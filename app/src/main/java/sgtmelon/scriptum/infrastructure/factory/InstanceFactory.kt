package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import android.content.Intent
import sgtmelon.scriptum.infrastructure.model.data.IntentData
import sgtmelon.scriptum.infrastructure.model.key.NoteState
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity

/**
 * Factory for build intents and get access to them from one place.
 */
object InstanceFactory {

    object Note {

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
}