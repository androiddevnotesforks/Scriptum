package sgtmelon.scriptum.test.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test open screens with wrong intent data, check error handling
 */
@RunWith(AndroidJUnit4::class)
class WrongData : ParentUiTest() {

    @Test fun bindTextNoteOpen() = launchBind(textNote) { waitBefore(TIME) }

    @Test fun bindRollNoteOpen() = launchBind(rollNote) { waitBefore(TIME) }

    @Test fun alarmTextNoteOpen() = launchAlarm(textNote) { waitBefore(TIME) }

    @Test fun alarmRollNoteOpen() = launchAlarm(rollNote) { waitBefore(TIME) }

    private companion object {
        const val WRONG_ID = 12345L
        const val TIME = 1000L

        val textNote = NoteModel(NoteEntity(WRONG_ID, type = NoteType.TEXT))
        val rollNote = NoteModel(NoteEntity(WRONG_ID, type = NoteType.ROLL))
    }

}