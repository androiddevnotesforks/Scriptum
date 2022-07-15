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
import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
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

    @MockK lateinit var preferenceRepo: AppPreferences
    @MockK lateinit var noteRepo: INoteRepo
    //    @MockK lateinit var callback: IBinBridge

    private val interactor by lazy { BinInteractor(preferenceRepo, noteRepo/*, callback*/) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, noteRepo/*, callback*/)
    }

    //    @Test override fun onDestroy() {
    //        assertNotNull(interactor.callback)
    //        interactor.onDestroy()
    //        assertNull(interactor.callback)
    //    }


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
        every { preferenceRepo.sort } returns firstSort
        interactor.getList()

        val secondSort = TestData.sort
        every { preferenceRepo.sort } returns secondSort
        interactor.getList()

        coVerifySequence {
            preferenceRepo.sort
            noteRepo.getList(firstSort, isBin = true, isOptimal = true, filterVisible = false)

            preferenceRepo.sort
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
            //            callback.copyClipboard(text)
        }
    }

    @Test fun clearNote() = startCoTest {
        FastTest.Interactor.clearNote<NoteItem>(noteRepo) { interactor.clearNote(it) }
    }
}