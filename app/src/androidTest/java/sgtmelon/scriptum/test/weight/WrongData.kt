package sgtmelon.scriptum.test.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test open screens with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class WrongData : ParentUiTest() {

    private val converter = NoteConverter()
    private val textNote = converter.toItem(NoteEntity(WRONG_ID, type = NoteType.TEXT))
    private val rollNote = converter.toItem(NoteEntity(WRONG_ID, type = NoteType.ROLL))

    @Test fun bindTextNoteOpen() = launchBind(textNote) { waitBefore(TIME) }

    @Test fun bindRollNoteOpen() = launchBind(rollNote) { waitBefore(TIME) }

    @Test fun alarmTextNoteOpen() = launchAlarm(textNote) { waitBefore(TIME) }

    @Test fun alarmRollNoteOpen() = launchAlarm(rollNote) { waitBefore(TIME) }

    companion object {
        private const val WRONG_ID = 12345L
        private const val TIME = 1000L
    }
}