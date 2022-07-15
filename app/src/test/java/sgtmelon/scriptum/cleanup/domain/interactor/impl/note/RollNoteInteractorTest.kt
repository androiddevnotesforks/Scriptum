package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ParentInteractorTest
import kotlin.random.Random

/**
 * Test for [RollNoteInteractor].
 */
@ExperimentalCoroutinesApi
class RollNoteInteractorTest : ParentInteractorTest() {

    // TODO many items are common with [TextNoteInteractor]

    @MockK lateinit var preferenceRepo: AppPreferences
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy {
        RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }


    @Test fun getSaveModel() = FastTest.Interactor.getSaveModel(preferenceRepo) {
        interactor.getSaveModel()
    }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val wrongItem = mockk<NoteItem.Text>()
        val item = mockk<NoteItem.Roll>()

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns null
        assertNull(interactor.getItem(id))

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns wrongItem
        assertNull(interactor.getItem(id))

        coEvery { noteRepo.getItem(id, isOptimal = false) } returns item
        assertEquals(item, interactor.getItem(id))

        coVerifySequence {
            noteRepo.getItem(id, isOptimal = false)
            noteRepo.getItem(id, isOptimal = false)
            noteRepo.getItem(id, isOptimal = false)
        }
    }

    @Test fun getRankDialogItemArray() = startCoTest {
        FastTest.Interactor.getRankDialogItemArray(rankRepo) {
            interactor.getRankDialogItemArray(it)
        }
    }


    @Test fun setVisible() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        interactor.setVisible(item)

        coVerifySequence {
            noteRepo.setRollVisible(item)
        }
    }

    @Test fun updateRollCheck_byPosition() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val p = Random.nextInt()

        interactor.updateRollCheck(item, p)

        coVerifySequence {
            noteRepo.updateRollCheck(item, p)
        }
    }

    @Test fun updateRollCheck_byCheck() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val isCheck = Random.nextBoolean()

        interactor.updateRollCheck(item, isCheck)

        coVerifySequence {
            noteRepo.updateRollCheck(item, isCheck)
        }
    }


    @Test fun getRankId() = startCoTest {
        FastTest.Interactor.getRankId(rankRepo) { interactor.getRankId(it) }
    }

    @Test fun getDateList() = startCoTest {
        FastTest.Interactor.getDateList(alarmRepo) { interactor.getDateList() }
    }

    @Test fun clearDate() = startCoTest {
        FastTest.Interactor.clearDate<NoteItem.Roll>(alarmRepo) {
            interactor.clearDate(it)
        }
    }

    @Test fun setDate() = startCoTest {
        FastTest.Interactor.setDate<NoteItem.Roll>(alarmRepo) { item, calendar ->
            interactor.setDate(item, calendar)
        }
    }


    @Test fun convertNote() = startCoTest {
        val item = mockk<NoteItem.Roll>()

        coEvery { noteRepo.convertNote(item, useCache = true) } returns mockk()

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item, useCache = true)
        }
    }

    @Test fun restoreNote() = startCoTest {
        FastTest.Interactor.restoreNote<NoteItem.Roll>(noteRepo) { interactor.restoreNote(it) }
    }

    @Test fun updateNote() = startCoTest {
        FastTest.Interactor.updateNote<NoteItem.Roll>(noteRepo) { interactor.updateNote(it) }
    }

    @Test fun clearNote() = startCoTest {
        FastTest.Interactor.clearNote<NoteItem.Roll>(noteRepo) { interactor.clearNote(it) }
    }

    @Test fun saveNote() = startCoTest {
        FastTest.Interactor.saveNote<NoteItem.Roll>(noteRepo, rankRepo) { item, isCreate ->
            interactor.saveNote(item, isCreate)
        }
    }

    @Test fun deleteNote() = startCoTest {
        FastTest.Interactor.deleteNote<NoteItem.Roll>(noteRepo) { interactor.deleteNote(it) }
    }
}