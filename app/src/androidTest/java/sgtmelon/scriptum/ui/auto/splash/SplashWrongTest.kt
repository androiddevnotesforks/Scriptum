package sgtmelon.scriptum.ui.auto.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlin.random.Random
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.data.room.converter.model.NoteConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.provider.EntityProvider.nextNoteEntity
import sgtmelon.scriptum.ui.testing.parent.ParentUiTest
import sgtmelon.scriptum.ui.testing.parent.launchAlarm
import sgtmelon.scriptum.ui.testing.parent.launchBind

/**
 * Open screens with wrong intent data, check error handling.
 */
@RunWith(AndroidJUnit4::class)
class SplashWrongTest : ParentUiTest() {

    private val converter = NoteConverter()
    private val textNote = converter.toItem(nextNoteEntity(Random.nextLong(), type = NoteType.TEXT))
    private val rollNote = converter.toItem(nextNoteEntity(Random.nextLong(), type = NoteType.ROLL))

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
}