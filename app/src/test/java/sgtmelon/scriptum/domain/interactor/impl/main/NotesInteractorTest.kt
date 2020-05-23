package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.getText
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesBridge
import java.util.*
import kotlin.random.Random

/**
 * Test for [NotesInteractor].
 */
@ExperimentalCoroutinesApi
class NotesInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var callback: INotesBridge

    private val interactor by lazy {
        NotesInteractor(preferenceRepo, noteRepo, alarmRepo, rankRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getSort() = FastTest.getSort(preferenceRepo) { interactor.sort }


    @Test fun getCount()  = startCoTest {
        val countList = listOf(Random.nextInt(), Random.nextInt())

        countList.forEach {
            coEvery { noteRepo.getCount(bin = false) } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { noteRepo.getCount(bin = false) }
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = data.itemList

        coEvery {
            noteRepo.getList(any(), bin = false, isOptimal = true, filterVisible = true)
        } returns itemList

        val firstSort = TestData.sort
        every { preferenceRepo.sort } returns firstSort
        interactor.getList()

        val secondSort = TestData.sort
        every { preferenceRepo.sort } returns secondSort
        interactor.getList()

        coVerifySequence {
            preferenceRepo.sort
            noteRepo.getList(firstSort, bin = false, isOptimal = true, filterVisible = true)

            preferenceRepo.sort
            noteRepo.getList(secondSort, bin = false, isOptimal = true, filterVisible = true)
        }
    }

    @Test fun isListHide() = startCoTest {
        val hideList = listOf(Random.nextBoolean(), Random.nextBoolean())

        hideList.forEach {
            coEvery { noteRepo.isListHide() } returns it
            assertEquals(it, interactor.isListHide())
        }

        coVerifySequence {
            repeat(hideList.size) { noteRepo.isListHide() }
        }
    }

    @Test fun updateNote() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val sort = TestData.sort

        val textItem = data.itemList.filterIsInstance<NoteItem.Text>().random()
        val textMirror = textItem.deepCopy()

        val rollList = data.rollList

        val rollItem = data.itemList.filterIsInstance<NoteItem.Roll>().random()
        val rollMirror = rollItem.deepCopy(list = rollList)

        coEvery { noteRepo.getRollList(rollItem.id) } returns rollList
        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        interactor.updateNote(textItem)

        coEvery { noteRepo.getRollList(rollItem.id) } returns rollList
        interactor.updateNote(rollItem)

        coVerifySequence {
            noteRepo.updateNote(textItem)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(textMirror, rankIdVisibleList, sort)

            noteRepo.updateNote(rollItem)
            noteRepo.getRollList(rollItem.id)
            preferenceRepo.sort
            callback.notifyNoteBind(rollMirror, rankIdVisibleList, sort)
        }
    }

    @Test fun convertNote() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList
        val sort = TestData.sort

        val textItem = data.itemList.filterIsInstance<NoteItem.Text>().random()

        val rollList = data.rollList
        val rollItemSmall = data.itemList.filterIsInstance<NoteItem.Roll>().random().apply {
            list.addAll(rollList.subList(0, 2))
        }
        val rollItemLarge = data.itemList.filterIsInstance<NoteItem.Roll>().random().apply {
            list.addAll(rollList)
        }
        val rollItemLargePreview = rollItemLarge.deepCopy(list = rollList).apply {
            with(list) { dropLast(size - NoteItem.Roll.PREVIEW_SIZE) }
        }

        /**
         * Convert textNote.
         */
        coEvery { noteRepo.convertNote(textItem) } returns rollItemSmall
        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        assertEquals(rollItemSmall, interactor.convertNote(textItem))

        /**
         * Convert textNote with list cut of return rollNote.
         */
        coEvery { noteRepo.convertNote(textItem) } returns rollItemLarge
        assertEquals(rollItemLargePreview, interactor.convertNote(textItem))

        /**
         * Convert rollNote.
         */
        coEvery { noteRepo.convertNote(rollItemSmall, useCache = false) } returns textItem
        assertEquals(textItem, interactor.convertNote(rollItemSmall))

        coVerifySequence {
            noteRepo.convertNote(textItem)
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            callback.notifyNoteBind(textItem, rankIdVisibleList, sort)

            noteRepo.convertNote(textItem)
            preferenceRepo.sort
            callback.notifyNoteBind(textItem, rankIdVisibleList, sort)

            noteRepo.convertNote(rollItemSmall, useCache = false)
            preferenceRepo.sort
            callback.notifyNoteBind(rollItemSmall, rankIdVisibleList, sort)
        }
    }


    @Test fun getDateList() = startCoTest {
        val itemList = TestData.Notification.itemList

        coEvery { alarmRepo.getList() } returns itemList
        assertEquals(itemList.map { it.alarm.date }, interactor.getDateList())

        coVerifySequence {
            alarmRepo.getList()
        }
    }

    @Test fun clearDate() = startCoTest {
        val item = data.itemList.random()

        interactor.clearDate(item)

        coVerifySequence {
            alarmRepo.delete(item.id)
            callback.cancelAlarm(item.id)
        }
    }

    @Test fun setDate() = startCoTest {
        val item = data.itemList.random()
        val calendar = Calendar.getInstance()

        interactor.setDate(item, calendar)

        coVerifySequence {
            alarmRepo.insertOrUpdate(item, calendar.getText())
            callback.setAlarm(calendar, item.id)
        }
    }


    @Test fun copy() = startCoTest {
        val item = data.itemList.random()
        val text = Random.nextString()

        coEvery { noteRepo.getCopyText(item) } returns text

        interactor.copy(item)

        coVerifySequence {
            noteRepo.getCopyText(item)
            callback.copyClipboard(text)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = data.itemList.random()

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            callback.cancelAlarm(item.id)
            callback.cancelNoteBind(item.id)
        }
    }


    @Test fun getNotification() = startCoTest {
        val item = TestData.Notification.itemList.random()

        val firstId = Random.nextLong()
        coEvery { alarmRepo.getItem(firstId) } returns null
        assertEquals(null, interactor.getNotification(firstId))

        val secondId = Random.nextLong()
        coEvery { alarmRepo.getItem(secondId) } returns item
        assertEquals(item, interactor.getNotification(secondId))

        coVerifySequence {
            alarmRepo.getItem(firstId)
            alarmRepo.getItem(secondId)
        }
    }

}