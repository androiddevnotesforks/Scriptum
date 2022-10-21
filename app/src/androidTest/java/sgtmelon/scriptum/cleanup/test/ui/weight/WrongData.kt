package sgtmelon.scriptum.cleanup.test.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ParentUiTest

/**
 * Test open screens with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class WrongData : ParentUiTest() {

    private val converter = NoteConverter()
    private val textNote = converter.toItem(NoteEntity(WRONG_ID, type = NoteType.TEXT))
    private val rollNote = converter.toItem(NoteEntity(WRONG_ID, type = NoteType.ROLL))

    @Test fun bindTextNoteOpen() = launchBind(textNote) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun bindRollNoteOpen() = launchBind(rollNote) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun alarmTextNoteOpen() = launchAlarm(textNote) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    @Test fun alarmRollNoteOpen() = launchAlarm(rollNote) {
        mainScreen { notesScreen(isEmpty = true) }
    }

    companion object {
        private const val WRONG_ID = 12345L
    }
}