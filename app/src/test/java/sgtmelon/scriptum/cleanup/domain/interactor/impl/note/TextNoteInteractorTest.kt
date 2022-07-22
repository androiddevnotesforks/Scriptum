package sgtmelon.scriptum.cleanup.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentInteractorTest

/**
 * Test for [TextNoteInteractor].
 */
@ExperimentalCoroutinesApi
class TextNoteInteractorTest : ParentInteractorTest() {

    // TODO many items are common with [RollNoteInteractor]

    @MockK lateinit var preferences: Preferences
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy {
        TextNoteInteractor(preferences, alarmRepo, rankRepo, noteRepo)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences, alarmRepo, rankRepo, noteRepo)
    }

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferences) {
        interactor.defaultColor
    }


    @Test fun getItem() = startCoTest {
        val id = Random.nextLong()
        val wrongItem = mockk<NoteItem.Roll>()
        val item = mockk<NoteItem.Text>()

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


    @Test fun getRankId() = startCoTest {
        FastTest.Interactor.getRankId(rankRepo) { interactor.getRankId(it) }
    }

    @Test fun getDateList() = startCoTest {
        FastTest.Interactor.getDateList(alarmRepo) { interactor.getDateList() }
    }

    @Test fun clearDate() = startCoTest {
        FastTest.Interactor.clearDate<NoteItem.Text>(alarmRepo) {
            interactor.clearDate(it)
        }
    }

    @Test fun setDate() = startCoTest {
        FastTest.Interactor.setDate<NoteItem.Text>(alarmRepo) { item, calendar ->
            interactor.setDate(item, calendar)
        }
    }


    @Test fun convertNote() = startCoTest {
        val item = mockk<NoteItem.Text>()

        coEvery { noteRepo.convertNote(item) } returns mockk()

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item)
        }
    }

    @Test fun restoreNote() = startCoTest {
        FastTest.Interactor.restoreNote<NoteItem.Text>(noteRepo) { interactor.restoreNote(it) }
    }

    @Test fun updateNote() = startCoTest {
        FastTest.Interactor.updateNote<NoteItem.Text>(noteRepo) { interactor.updateNote(it) }
    }

    @Test fun clearNote() = startCoTest {
        FastTest.Interactor.clearNote<NoteItem.Text>(noteRepo) { interactor.clearNote(it) }
    }

    @Test fun saveNote() = startCoTest {
        FastTest.Interactor.saveNote<NoteItem.Text>(noteRepo, rankRepo) { item, isCreate ->
            interactor.saveNote(item, isCreate)
        }
    }

    @Test fun deleteNote() = startCoTest {
        FastTest.Interactor.deleteNote<NoteItem.Text>(noteRepo) { interactor.deleteNote(it) }
    }
}