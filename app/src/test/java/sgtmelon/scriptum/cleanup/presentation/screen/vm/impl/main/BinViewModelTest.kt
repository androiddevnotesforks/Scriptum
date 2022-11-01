package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main

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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Options
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.clearAdd
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.domain.useCase.main.ClearBinUseCase
import sgtmelon.scriptum.domain.useCase.main.GetNoteListUseCase
import sgtmelon.scriptum.domain.useCase.note.ClearNoteUseCase
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase
import sgtmelon.scriptum.domain.useCase.note.RestoreNoteUseCase
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModel
import sgtmelon.scriptum.infrastructure.screen.main.bin.IBinFragment
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.test.common.nextString

/**
 * Test for [BinViewModel].
 */
@ExperimentalCoroutinesApi
class BinViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IBinFragment
    @MockK lateinit var interactor: IBinInteractor

    @MockK lateinit var getList: GetNoteListUseCase
    @MockK lateinit var getCopyText: GetCopyTextUseCase
    @MockK lateinit var restoreNote: RestoreNoteUseCase
    @MockK lateinit var clearBin: ClearBinUseCase
    @MockK lateinit var clearNote: ClearNoteUseCase

    private val viewModel by lazy {
        BinViewModel(callback, interactor, getList, getCopyText, restoreNote, clearBin, clearNote)
    }
    private val spyViewModel by lazy { spyk(viewModel) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            callback, interactor, getList, getCopyText, restoreNote, clearBin, clearNote
        )
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }

    //endregion

    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
            callback.setupDialog()

            callback.prepareForLoad()
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = List<NoteItem>(getRandomSize()) { mockk() }

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { getList(isBin = true) } returns itemList

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            callback.hideEmptyInfo()
            callback.showProgress()
            getList(isBin = true)
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns itemList.size

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = List<NoteItem>(getRandomSize()) { mockk() }
        val returnList = List<NoteItem>(getRandomSize()) { mockk() }

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { getList(isBin = true) } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            updateList(any())
            interactor.getCount()
            getList(isBin = true)
            updateList(returnList)
        }

        assertEquals(returnList, viewModel.itemList)
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        val startList = List<NoteItem>(getRandomSize()) { mockk() }
        val returnList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns returnList.size

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            updateList(any())
            interactor.getCount()
            updateList(returnList)
        }
    }

    private fun updateList(itemList: List<NoteItem>) = with(callback) {
        notifyList(itemList)
        notifyMenuClearBin()
        onBindingList()
    }


    @Test fun onClickClearBin() = startCoTest {
        val itemList = List<NoteItem>(getRandomSize()) { mockk() }

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onClickClearBin()

        coVerifySequence {
            clearBin()

            callback.apply {
                notifyList(listOf())
                notifyMenuClearBin()
                onBindingList()
            }
        }

        assertTrue(viewModel.itemList.isEmpty())
    }

    @Test fun onShowOptionsDialog() {
        TODO()
        //        viewModel.onShowOptionsDialog(mockk(), Random.nextInt())
        //
        //        val p = 0
        //        val itemArray = Array(getRandomSize()) { nextString() }
        //
        //        val item = mockk<NoteItem>()
        //        val untitledName = nextString()
        //        val name = nextString()
        //
        //        viewModel.itemList.add(item)
        //
        //        every { item.name } returns ""
        //        every { callback.getString(R.string.hint_text_name) } returns untitledName
        //        every { callback.getStringArray(R.array.dialog_menu_bin) } returns itemArray
        //        viewModel.onShowOptionsDialog(, p)
        //
        //        every { item.name } returns name
        //        viewModel.onShowOptionsDialog(, p)
        //
        //        verifySequence {
        //            item.name
        //            callback.getString(R.string.hint_text_name)
        //            callback.getStringArray(R.array.dialog_menu_bin)
        //            callback.showOptionsDialog(untitledName, itemArray, p)
        //
        //            item.name
        //            callback.getStringArray(R.array.dialog_menu_bin)
        //            callback.showOptionsDialog(name, itemArray, p)
        //        }
    }

    @Test fun onResultOptionsDialog() {
        val p = Random.nextInt()
        val which = -1

        spyViewModel.onResultOptionsDialog(p, which)

        every { spyViewModel.onMenuRestore(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Bin.RESTORE)

        every { spyViewModel.onMenuCopy(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Bin.COPY)

        every { spyViewModel.onMenuClear(p) } returns Unit
        spyViewModel.onResultOptionsDialog(p, Options.Bin.CLEAR)

        verifySequence {
            spyViewModel.onResultOptionsDialog(p, which)

            spyViewModel.onResultOptionsDialog(p, Options.Bin.RESTORE)
            spyViewModel.onMenuRestore(p)

            spyViewModel.onResultOptionsDialog(p, Options.Bin.COPY)
            spyViewModel.onMenuCopy(p)

            spyViewModel.onResultOptionsDialog(p, Options.Bin.CLEAR)
            spyViewModel.onMenuClear(p)
        }
    }

    @Test fun onMenuRestore() = startCoTest {
        viewModel.onMenuRestore(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        val resultList = ArrayList(itemList)
        resultList.removeAt(index)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuRestore(index)

        coVerifySequence {
            restoreNote(item)

            callback.notifyList(resultList)
            callback.notifyMenuClearBin()
        }

        assertEquals(resultList, viewModel.itemList)
    }

    @Test fun onMenuCopy() = startCoTest {
        viewModel.onMenuCopy(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]
        val text = nextString()

        coEvery { getCopyText(item) } returns text

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuCopy(index)

        coVerifySequence {
            getCopyText(item)
            callback.copyClipboard(text)
        }

        assertEquals(itemList, viewModel.itemList)
    }

    @Test fun onMenuClear() = startCoTest {
        viewModel.onMenuClear(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        val resultList = ArrayList(itemList)
        resultList.removeAt(index)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuClear(index)

        coVerifySequence {
            clearNote(item)

            callback.notifyList(resultList)
            callback.notifyMenuClearBin()
        }

        assertEquals(resultList, viewModel.itemList)
    }

}