package sgtmelon.scriptum.domain.interactor.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinBridge
import kotlin.random.Random

/**
 * Test for [BinInteractor].
 */
@ExperimentalCoroutinesApi
class BinInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var callback: IBinBridge

    private val interactor by lazy { BinInteractor(preferenceRepo, noteRepo, callback) }

    @Test override fun onDestroy() {
        assertNotNull(interactor.callback)
        interactor.onDestroy()
        assertNull(interactor.callback)
    }


    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getCount() = startCoTest {
        val countList = listOf(null, Random.nextInt(), Random.nextInt(), null)

        countList.forEach {
            coEvery { noteRepo.getCount(bin = true) } returns it
            assertEquals(it, interactor.getCount())
        }

        coVerifySequence {
            repeat(countList.size) { noteRepo.getCount(bin = true) }
        }
    }

    @Test fun getList() = startCoTest {
        val itemList = data.itemList

        coEvery {
            noteRepo.getList(any(), bin = true, isOptimal = true, filterVisible = false)
        } returns itemList

        every { preferenceRepo.sort } returns null
        interactor.getList()

        val firstSort = TestData.sort
        every { preferenceRepo.sort } returns firstSort
        interactor.getList()

        val secondSort = TestData.sort
        every { preferenceRepo.sort } returns secondSort
        interactor.getList()

        coVerifySequence {
            preferenceRepo.sort

            preferenceRepo.sort
            noteRepo.getList(firstSort, bin = true, isOptimal = true, filterVisible = false)

            preferenceRepo.sort
            noteRepo.getList(secondSort, bin = true, isOptimal = true, filterVisible = false)
        }
    }

    @Test fun clearBin() = startCoTest {
        interactor.clearBin()

        coVerifySequence {
            noteRepo.clearBin()
        }
    }

    @Test fun restoreNote() = startCoTest {
        val item = data.itemList.random()

        interactor.restoreNote(item)

        coVerifySequence {
            noteRepo.restoreNote(item)
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

    @Test fun clearNote() = startCoTest {
        val item = data.itemList.random()

        interactor.clearNote(item)

        coVerifySequence {
            noteRepo.clearNote(item)
        }
    }

}