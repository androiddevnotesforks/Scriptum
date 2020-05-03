package sgtmelon.scriptum.domain.interactor.impl.note

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getText
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.note.text.ITextNoteBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [TextNoteInteractor].
 */
@ExperimentalCoroutinesApi
class TextNoteInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: ITextNoteBridge

    private val interactor by lazy {
        TextNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
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

    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }


    @Test fun getItem() {
        TODO()
    }

    @Test fun getRankDialogItemArray() {
        TODO()
    }


    @Test fun getRankId() = startCoTest {
        val check = Random.nextInt()
        val id = Random.nextLong()

        coEvery { rankRepo.getId(check) } returns id

        assertEquals(id, interactor.getRankId(check))

        coVerifySequence {
            rankRepo.getId(check)
        }
    }

    @Test fun getDateList() = startCoTest {
        val itemList = TestData.Notification.itemList
        val dateList = itemList.map { it.alarm.date }

        coEvery { alarmRepo.getList() } returns itemList

        assertEquals(dateList, interactor.getDateList())

        coVerifySequence {
            alarmRepo.getList()
        }
    }

    @Test fun clearDate() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()

        interactor.clearDate(item)

        coVerifySequence {
            alarmRepo.delete(item.id)
            callback.cancelAlarm(item.id)
        }
    }

    @Test fun setDate() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()
        val calendar = Calendar.getInstance()

        interactor.setDate(item, calendar)

        coVerifySequence {
            alarmRepo.insertOrUpdate(item, calendar.getText())
            callback.setAlarm(calendar, item.id)
        }
    }

    @Test fun convertNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()
        val returnItem = data.itemList.filterIsInstance<NoteItem.Roll>().random()

        coEvery { noteRepo.convertNote(item) } returns returnItem

        interactor.convertNote(item)

        coVerifySequence {
            noteRepo.convertNote(item)
        }
    }


    @Test fun restoreNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()

        interactor.restoreNote(item)

        coVerifySequence {
            noteRepo.restoreNote(item)
        }
    }

    @Test fun updateNote() {
        TODO()
    }

    @Test fun clearNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()

        interactor.clearNote(item)

        coVerifySequence {
            noteRepo.clearNote(item)
        }
    }

    @Test fun saveNote() {
        TODO()
    }

    @Test fun deleteNote() = startCoTest {
        val item = data.itemList.filterIsInstance<NoteItem.Text>().random()

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            callback.cancelAlarm(item.id)
            callback.cancelNoteBind(item.id.toInt())
        }
    }

}