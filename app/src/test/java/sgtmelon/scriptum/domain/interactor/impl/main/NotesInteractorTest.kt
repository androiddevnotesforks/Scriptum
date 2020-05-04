package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
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
import sgtmelon.scriptum.domain.model.annotation.Sort
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
        val count = Random.nextInt()

        coEvery { noteRepo.getCount(bin = false) } returns count

        assertEquals(count, interactor.getCount())

        coVerifySequence {
            noteRepo.getCount(bin = false)
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = data.itemList

        coEvery {
            noteRepo.getList(any(), bin = false, optimal = true, filterVisible = true)
        } returns itemList

        every { preferenceRepo.sort } returns Sort.CHANGE
        interactor.getList()

        every { preferenceRepo.sort } returns Sort.COLOR
        interactor.getList()

        coVerifySequence {
            preferenceRepo.sort
            noteRepo.getList(Sort.CHANGE, bin = false, optimal = true, filterVisible = true)

            preferenceRepo.sort
            noteRepo.getList(Sort.COLOR, bin = false, optimal = true, filterVisible = true)
        }
    }

    @Test fun isListHide() = startCoTest {
        val isListHide = Random.nextBoolean()

        coEvery { noteRepo.isListHide() } returns isListHide

        assertEquals(isListHide, interactor.isListHide())

        coVerifySequence {
            noteRepo.isListHide()
        }
    }

    @Test fun updateNote() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList

        val textItem = data.itemList.filterIsInstance<NoteItem.Text>().random()
        val textMirror = textItem.deepCopy()

        val rollList = data.rollList

        val rollItem = data.itemList.filterIsInstance<NoteItem.Roll>().random()
        val rollMirror = rollItem.deepCopy(list = rollList)

        coEvery { noteRepo.getRollList(rollItem.id) } returns rollList
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        interactor.updateNote(textItem)
        interactor.updateNote(rollItem)

        coVerifySequence {
            noteRepo.updateNote(textItem)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(textMirror, rankIdVisibleList)

            noteRepo.updateNote(rollItem)
            noteRepo.getRollList(rollItem.id)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(rollMirror, rankIdVisibleList)
        }
    }

    @Test fun convert() = startCoTest {
        val rankIdVisibleList = data.rankIdVisibleList

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

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList

        coEvery { noteRepo.convertNote(textItem) } returns rollItemSmall
        assertEquals(rollItemSmall, interactor.convert(textItem))

        coEvery { noteRepo.convertNote(textItem) } returns rollItemLarge
        assertEquals(rollItemLargePreview, interactor.convert(textItem))

        coEvery { noteRepo.convertNote(rollItemSmall, useCache = false) } returns textItem
        assertEquals(textItem, interactor.convert(rollItemSmall))

        coVerifySequence {
            noteRepo.convertNote(textItem)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(textItem, rankIdVisibleList)

            noteRepo.convertNote(textItem)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(textItem, rankIdVisibleList)

            noteRepo.convertNote(rollItemSmall, useCache = false)
            rankRepo.getIdVisibleList()
            callback.notifyNoteBind(rollItemSmall, rankIdVisibleList)
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
        val text = TestData.uniqueString

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
            callback.cancelNoteBind(item.id.toInt())
        }
    }


    @Test fun getNotification() = startCoTest {
        val noteId = Random.nextLong()
        val item = TestData.Notification.itemList.random()

        coEvery { alarmRepo.getItem(noteId) } returns item

        assertEquals(item, interactor.getNotification(noteId))

        coVerifySequence {
            alarmRepo.getItem(noteId)
        }
    }

}