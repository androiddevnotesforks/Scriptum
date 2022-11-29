package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.parent.ParentCoTest
import sgtmelon.scriptum.testing.getRandomSize

/**
 * Test for [NotesInteractor].
 */
@ExperimentalCoroutinesApi
class NotesInteractorTest : ParentCoTest() {

    @MockK lateinit var noteRepo: NoteRepo

    private val interactor by lazy { NotesInteractor(noteRepo) }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo)
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
}