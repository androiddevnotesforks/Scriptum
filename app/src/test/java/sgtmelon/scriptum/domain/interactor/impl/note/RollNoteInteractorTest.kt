package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.note.roll.IRollNoteBridge
import kotlin.random.Random

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IRollNoteBridge

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getSaveModel() {
        TODO()
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getDefaultColor() {
        TODO()
    }


    @Test fun getItem() {
        TODO()
    }

    @Test fun getRankDialogItemArray() {
        TODO()
    }


    @Test fun setVisible() = startCoTest {
        val noteId = Random.nextLong()
        val isVisible = Random.nextBoolean()

        interactor.setVisible(noteId, isVisible)

        coVerifySequence {
            noteRepo.setRollVisible(noteId, isVisible)
        }
    }

    @Test fun getVisible() = startCoTest {
        val noteId = Random.nextLong()
        val isVisible = Random.nextBoolean()

        coEvery { noteRepo.getRollVisible(noteId) } returns isVisible

        assertEquals(isVisible, interactor.getVisible(noteId))

        coVerifySequence {
            noteRepo.getRollVisible(noteId)
        }
    }


    @Test fun updateRollCheck() {
        TODO()
    }

    @Test fun testUpdateRollCheck() {
        TODO()
    }

    @Test fun getRankId() {
        TODO()
    }

    @Test fun getDateList() {
        TODO()
    }

    @Test fun clearDate() {
        TODO()
    }

    @Test fun setDate() {
        TODO()
    }

    @Test fun convertNote() {
        TODO()
    }


    @Test fun restoreNote() {
        TODO()
    }

    @Test fun updateNote() {
        TODO()
    }

    @Test fun clearNote() {
        TODO()
    }

    @Test fun saveNote() {
        TODO()
    }

    @Test fun deleteNote() {
        TODO()
    }

}