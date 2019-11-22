package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.repository.note.INoteRepo
import sgtmelon.scriptum.repository.note.NoteRepo
import sgtmelon.scriptum.room.converter.NoteConverter
import sgtmelon.scriptum.room.converter.RollConverter
import sgtmelon.scriptum.test.ParentIntegrationTest

/**
 * Integration test for [NoteRepo]
 */
@RunWith(AndroidJUnit4::class)
class NoteRepoTest : ParentIntegrationTest()  {

    private val iNoteRepo: INoteRepo = NoteRepo(context)

    private val noteConverter = NoteConverter()
    private val rollConverter = RollConverter()

    @Test fun getList() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getItem() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getRollList() {
        TODO(reason = "#TEST write test")
    }


    @Test fun isListHide() {
        TODO(reason = "#TEST write test")
    }

    @Test fun clearBin() {
        TODO(reason = "#TEST write test")
    }


    @Test fun deleteNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun restoreNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun clearNote() {
        TODO(reason = "#TEST write test")
    }


    @Test fun convertToRoll() {
        TODO(reason = "#TEST write test")
    }

    @Test fun convertToText() {
        TODO(reason = "#TEST write test")
    }

    @Test fun getCopyText() {
        TODO(reason = "#TEST write test")
    }

    @Test fun saveTextNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun saveRollNote() {
        TODO(reason = "#TEST write test")
    }

    @Test fun updateRollCheck() {
        TODO(reason = "#TEST write test")
    }

    @Test fun updateNote() {
        TODO(reason = "#TEST write test")
    }

}