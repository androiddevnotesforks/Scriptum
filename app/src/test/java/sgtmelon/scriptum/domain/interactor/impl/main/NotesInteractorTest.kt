package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.NotificationItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentInteractorTest
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

    private val interactor by lazy {
        NotesInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, noteRepo, alarmRepo, rankRepo)
    }


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
        FastTest.Interactor.updateNote<NoteItem>(noteRepo) { interactor.updateNote(it) }
    }

    @Test fun convertNote_text() = startCoTest {
        val item = mockk<NoteItem.Text>()
        val convertItem = mockk<NoteItem.Roll>()

        coEvery { noteRepo.convertNote(item) } returns convertItem
        every { spyInteractor.onConvertOptimisation(convertItem) } returns Unit

        assertEquals(convertItem, spyInteractor.convertNote(item))

        coVerifySequence {
            spyInteractor.convertNote(item)
            noteRepo.convertNote(item)
            spyInteractor.onConvertOptimisation(convertItem)
        }
    }

    @Test fun convertNote_roll() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val convertItem = mockk<NoteItem.Text>()

        coEvery { noteRepo.convertNote(item, useCache = false) } returns convertItem

        assertEquals(convertItem, spyInteractor.convertNote(item))

        coVerifySequence {
            spyInteractor.convertNote(item)
            noteRepo.convertNote(item, useCache = false)
        }
    }

    @Test fun onConvertOptimisation() {
        val item = mockk<NoteItem.Roll>()
        val size = getRandomSize()
        val startList = MutableList<RollItem>(size) { mockk() }
        val finishList = startList.take(NoteItem.Roll.PREVIEW_SIZE).toMutableList()

        every { item.list } returns mutableListOf()
        interactor.onConvertOptimisation(item)

        every { item.list } returns finishList
        interactor.onConvertOptimisation(item)
        assertEquals(NoteItem.Roll.PREVIEW_SIZE, finishList.size)

        every { item.list } returns startList
        interactor.onConvertOptimisation(item)
        assertEquals(startList, finishList)

        verifySequence {
            item.list
            item.list
            item.list
        }
    }


    @Test fun getDateList() = startCoTest {
        FastTest.Interactor.getDateList(alarmRepo) { interactor.getDateList() }
    }

    @Test fun clearDate() = startCoTest {
        FastTest.Interactor.clearDate<NoteItem>(alarmRepo) { interactor.clearDate(it) }
    }

    @Test fun setDate() = startCoTest {
        FastTest.Interactor.setDate<NoteItem>(alarmRepo) { item, calendar ->
            interactor.setDate(item, calendar)
        }
    }


    @Test fun copy() = startCoTest {
        val item = mockk<NoteItem>()
        val text = nextString()

        coEvery { noteRepo.getCopyText(item) } returns text

        assertEquals(text, interactor.copy(item))

        coVerifySequence {
            noteRepo.getCopyText(item)
        }
    }

    @Test fun deleteNote() = startCoTest {
        FastTest.Interactor.deleteNote<NoteItem>(noteRepo) { interactor.deleteNote(it) }
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