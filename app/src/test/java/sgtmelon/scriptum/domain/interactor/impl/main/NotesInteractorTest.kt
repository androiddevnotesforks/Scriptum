package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.ui.callback.main.INotesBridge
import kotlin.random.Random

/**
 * Test for [NotesInteractor].
 */
@ExperimentalCoroutinesApi
class NotesInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var callback: INotesBridge

    private val interactor by lazy {
        NotesInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, callback)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    override fun setUp() {
        super.setUp()
        assertNull(interactor.rankIdVisibleList)
    }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, noteRepo, alarmRepo, rankRepo, callback)
    }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getRankIdVisibleList() = startCoTest {
        val list = mockk<List<Long>>()

        coEvery { rankRepo.getIdVisibleList() } returns list

        assertEquals(list, interactor.getRankIdVisibleList())
        assertEquals(list, interactor.rankIdVisibleList)

        coEvery { rankRepo.getIdVisibleList() } returns emptyList()

        assertEquals(list, interactor.getRankIdVisibleList())

        coVerifySequence {
            rankRepo.getIdVisibleList()
        }
    }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getSort() = FastTest.getSort(preferenceRepo) { interactor.sort }


    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { noteRepo.getCount(isBin = false) } returns count
        assertEquals(count, interactor.getCount())

        coVerifySequence {
            noteRepo.getCount(isBin = false)
        }
    }

    @Test fun getList() = startCoTest {
        val sort = Random.nextInt()
        val list = mockk<MutableList<NoteItem>>()

        every { preferenceRepo.sort } returns sort
        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
        } returns list

        interactor.getList()

        coVerifySequence {
            preferenceRepo.sort
            noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
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
        val item = mockk<NoteItem>()
        val mirrorItem = mockk<NoteItem>()
        val rankIdList = mockk<List<Long>>()
        val sort = Random.nextInt()

        coEvery { spyInteractor.makeMirror(item) } returns mirrorItem
        coEvery { spyInteractor.getRankIdVisibleList() } returns rankIdList
        every { preferenceRepo.sort } returns sort
        spyInteractor.updateNote(item)

        coVerifySequence {
            spyInteractor.updateNote(item)
            noteRepo.updateNote(item)

            spyInteractor.makeMirror(item)
            spyInteractor.getRankIdVisibleList()
            spyInteractor.callback
            preferenceRepo.sort
            callback.notifyNoteBind(mirrorItem, rankIdList, sort)
        }
    }

    @Test fun makeMirror() = startCoTest {
        TODO()
    }

    @Test fun convertNote() = startCoTest {
        TODO()
        //        val rankIdVisibleList = data.rankIdVisibleList
        //        val sort = TestData.sort
        //
        //        val textItem = data.itemList.filterIsInstance<NoteItem.Text>().random()
        //
        //        val rollList = data.rollList
        //        val rollItemSmall = data.itemList.filterIsInstance<NoteItem.Roll>().random().apply {
        //            list.addAll(rollList.subList(0, 2))
        //        }
        //        val rollItemLarge = data.itemList.filterIsInstance<NoteItem.Roll>().random().apply {
        //            list.addAll(rollList)
        //        }
        //        val rollItemLargePreview = rollItemLarge.deepCopy(list = rollList).apply {
        //            with(list) { dropLast(size - NoteItem.Roll.PREVIEW_SIZE) }
        //        }
        //
        //        /**
        //         * Convert textNote.
        //         */
        //        coEvery { noteRepo.convertNote(textItem) } returns rollItemSmall
        //        every { preferenceRepo.sort } returns sort
        //        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        //        assertEquals(rollItemSmall, interactor.convertNote(textItem))
        //
        //        /**
        //         * Convert textNote with list cut of return rollNote.
        //         */
        //        coEvery { noteRepo.convertNote(textItem) } returns rollItemLarge
        //        assertEquals(rollItemLargePreview, interactor.convertNote(textItem))
        //
        //        /**
        //         * Convert rollNote.
        //         */
        //        coEvery { noteRepo.convertNote(rollItemSmall, useCache = false) } returns textItem
        //        assertEquals(textItem, interactor.convertNote(rollItemSmall))
        //
        //        coVerifySequence {
        //            noteRepo.convertNote(textItem)
        //            rankRepo.getIdVisibleList()
        //            preferenceRepo.sort
        //            callback.notifyNoteBind(textItem, rankIdVisibleList, sort)
        //
        //            noteRepo.convertNote(textItem)
        //            preferenceRepo.sort
        //            callback.notifyNoteBind(textItem, rankIdVisibleList, sort)
        //
        //            noteRepo.convertNote(rollItemSmall, useCache = false)
        //            preferenceRepo.sort
        //            callback.notifyNoteBind(rollItemSmall, rankIdVisibleList, sort)
        //        }
    }


    @Test fun getDateList() = startCoTest {
        val size = getRandomSize()
        val itemList = MutableList<NotificationItem>(size) { mockk() }
        val alarmList = List<NotificationItem.Alarm>(size) { mockk() }
        val dateList = List(size) { nextString() }

        coEvery { alarmRepo.getList() } returns itemList

        for ((i, item) in itemList.withIndex()) {
            every { item.alarm } returns alarmList[i]
            every { alarmList[i].date } returns dateList[i]
        }

        assertEquals(dateList, interactor.getDateList())

        coVerifySequence {
            alarmRepo.getList()

            for ((i, item) in itemList.withIndex()) {
                item.alarm
                alarmList[i].date
            }
        }
    }

    @Test fun clearDate() = startCoTest {
        FastTest.Interactor.clearDate<NoteItem>(alarmRepo, callback) { interactor.clearDate(it) }
    }

    @Test fun setDate() = startCoTest {
        FastTest.Interactor.setDate<NoteItem>(alarmRepo, callback) { item, calendar ->
            interactor.setDate(item, calendar)
        }
    }


    @Test fun copy() = startCoTest {
        val item = mockk<NoteItem>()
        val text = nextString()

        coEvery { noteRepo.getCopyText(item) } returns text

        interactor.copy(item)

        coVerifySequence {
            noteRepo.getCopyText(item)
            callback.copyClipboard(text)
        }
    }

    @Test fun deleteNote() = startCoTest {
        val item = mockk<NoteItem>()
        val id = Random.nextLong()

        every { item.id } returns id

        interactor.deleteNote(item)

        coVerifySequence {
            noteRepo.deleteNote(item)

            item.id
            callback.cancelAlarm(id)
            item.id
            callback.cancelNoteBind(id)
        }
    }


    @Test fun getNotification() = startCoTest {
        val id = Random.nextLong()
        val item = mockk<NotificationItem>()

        coEvery { alarmRepo.getItem(id) } returns null
        assertNull(interactor.getNotification(id))

        coEvery { alarmRepo.getItem(id) } returns item
        assertEquals(item, interactor.getNotification(id))

        coVerifySequence {
            alarmRepo.getItem(id)
            alarmRepo.getItem(id)
        }
    }
}