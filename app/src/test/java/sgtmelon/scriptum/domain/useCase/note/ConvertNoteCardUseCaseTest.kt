package sgtmelon.scriptum.domain.useCase.note

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
 * Test for [ConvertNoteCardUseCase].
 */
@ExperimentalCoroutinesApi
class ConvertNoteCardUseCaseTest : ParentCoTest() {

    @MockK lateinit var noteRepo: NoteRepo

    private val useCase by lazy { ConvertNoteCardUseCase(noteRepo) }
    private val spyUseCase by lazy { spyk(useCase) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(noteRepo)
    }

    @Test fun convertNote_text() = startCoTest {
        val item = mockk<NoteItem.Text>()
        val convertItem = mockk<NoteItem.Roll>()

        coEvery { noteRepo.convertNote(item) } returns convertItem
        every { spyUseCase.onConvertOptimisation(convertItem) } returns Unit

        assertEquals(convertItem, spyUseCase(item))

        coVerifySequence {
            spyUseCase(item)
            noteRepo.convertNote(item)
            spyUseCase.onConvertOptimisation(convertItem)
        }
    }

    @Test fun convertNote_roll() = startCoTest {
        val item = mockk<NoteItem.Roll>()
        val convertItem = mockk<NoteItem.Text>()

        coEvery { noteRepo.convertNote(item, useCache = false) } returns convertItem

        assertEquals(convertItem, spyUseCase(item))

        coVerifySequence {
            spyUseCase(item)
            noteRepo.convertNote(item, useCache = false)
        }
    }

    @Test fun onConvertOptimisation() {
        val item = mockk<NoteItem.Roll>()
        val size = getRandomSize()
        val startList = MutableList<RollItem>(size) { mockk() }
        val finishList = startList.take(NoteItem.Roll.PREVIEW_SIZE).toMutableList()

        every { item.list } returns mutableListOf()
        useCase.onConvertOptimisation(item)

        every { item.list } returns finishList
        useCase.onConvertOptimisation(item)
        assertEquals(NoteItem.Roll.PREVIEW_SIZE, finishList.size)

        every { item.list } returns startList
        useCase.onConvertOptimisation(item)
        assertEquals(startList, finishList)

        verifySequence {
            item.list
            item.list
            item.list
        }
    }
}