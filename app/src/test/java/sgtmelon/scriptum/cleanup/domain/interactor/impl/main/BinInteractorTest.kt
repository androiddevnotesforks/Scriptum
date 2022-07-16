package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ParentInteractorTest
import kotlin.random.Random

/**
 * Test for [BinInteractor].
 */
@ExperimentalCoroutinesApi
class BinInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferences: Preferences
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy { BinInteractor(preferences, noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences, noteRepo)
    }


    @Test fun getCount() = startCoTest {
        val countList = listOf(Random.nextInt(), Random.nextInt())

        for (it in countList) {
            coEvery { noteRepo.getCount(isBin = true) } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { noteRepo.getCount(isBin = true) }
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = data.itemList

        coEvery {
            noteRepo.getList(any(), isBin = true, isOptimal = true, filterVisible = false)
        } returns itemList

        val firstSort = TestData.sort
        every { preferences.sort } returns firstSort
        interactor.getList()

        val secondSort = TestData.sort
        every { preferences.sort } returns secondSort
        interactor.getList()

        coVerifySequence {
            preferences.sort
            noteRepo.getList(firstSort, isBin = true, isOptimal = true, filterVisible = false)

            preferences.sort
            noteRepo.getList(secondSort, isBin = true, isOptimal = true, filterVisible = false)
        }
    }

    @Test fun clearBin() = startCoTest {
        interactor.clearBin()

        coVerifySequence {
            noteRepo.clearBin()
        }
    }

    @Test fun restoreNote() = startCoTest {
        FastTest.Interactor.restoreNote<NoteItem>(noteRepo) { interactor.restoreNote(it) }
    }

    @Test fun copy() = startCoTest {
        val item = data.itemList.random()
        val text = nextString()

        coEvery { noteRepo.getCopyText(item) } returns text

        interactor.copy(item)

        coVerifySequence {
            noteRepo.getCopyText(item)
        }
    }

    @Test fun clearNote() = startCoTest {
        FastTest.Interactor.clearNote<NoteItem>(noteRepo) { interactor.clearNote(it) }
    }
}