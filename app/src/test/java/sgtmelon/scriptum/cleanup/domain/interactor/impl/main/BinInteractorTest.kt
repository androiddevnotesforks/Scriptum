package sgtmelon.scriptum.cleanup.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.FastTest
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Test for [BinInteractor].
 */
@ExperimentalCoroutinesApi
class BinInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: INoteRepo

    private val interactor by lazy { BinInteractor(preferencesRepo, noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferencesRepo, noteRepo)
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

        val firstSort = Sort.values().random()
        every { preferencesRepo.sort } returns firstSort
        interactor.getList()

        val secondSort = Sort.values().random()
        every { preferencesRepo.sort } returns secondSort
        interactor.getList()

        coVerifySequence {
            preferencesRepo.sort
            noteRepo.getList(firstSort, isBin = true, isOptimal = true, filterVisible = false)

            preferencesRepo.sort
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