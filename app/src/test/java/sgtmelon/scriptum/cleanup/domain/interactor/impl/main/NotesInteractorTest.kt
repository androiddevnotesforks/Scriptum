package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastTest
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.NotificationItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Sort
import sgtmelon.test.common.nextString

/**
 * Test for [NotesInteractor].
 */
@ExperimentalCoroutinesApi
class NotesInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var alarmRepo: AlarmRepo
    @MockK lateinit var rankRepo: IRankRepo

    private val interactor by lazy {
        NotesInteractor(preferencesRepo, alarmRepo, noteRepo)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, noteRepo, alarmRepo, rankRepo)
    }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { noteRepo.getCount(isBin = false) } returns count
        assertEquals(count, interactor.getCount())

        coVerifySequence {
            noteRepo.getCount(isBin = false)
        }
    }

    @Test fun getList() = startCoTest {
        val sort = mockk<Sort>()
        val list = mockk<MutableList<NoteItem>>()

        every { preferencesRepo.sort } returns sort
        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = true, filterVisible = true)
        } returns list

        interactor.getList()

        coVerifySequence {
            preferencesRepo.sort
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