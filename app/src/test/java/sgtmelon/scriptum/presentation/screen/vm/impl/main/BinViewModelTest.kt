package sgtmelon.scriptum.presentation.screen.vm.impl.main

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.model.annotation.Options
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinFragment
import kotlin.random.Random

/**
 * Test for [BinViewModel].
 */
@ExperimentalCoroutinesApi
class BinViewModelTest : ParentViewModelTest() {

    private val data = TestData.Note

    @MockK lateinit var callback: IBinFragment

    @MockK lateinit var interactor: IBinInteractor

    private val viewModel by lazy { BinViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        every { interactor.theme } returns Theme.LIGHT
        viewModel.onSetup()

        every { interactor.theme } returns Theme.DARK
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            interactor.theme
            callback.setupRecycler(Theme.LIGHT)

            callback.setupToolbar()
            interactor.theme
            callback.setupRecycler(Theme.DARK)
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns data.itemList.size
        coEvery { interactor.getList() } returns data.itemList

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(data.itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val returnList = mutableListOf(data.itemList.first())

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())

            interactor.getCount()
            interactor.getList()
            updateList(returnList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getEmpty() = startCoTest {
        coEvery { interactor.getCount() } returns 0
        coEvery { interactor.getList() } returns mutableListOf()

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())

            interactor.getCount()
            updateList(mutableListOf())
        }
    }

    private fun updateList(itemList: List<NoteItem>) = with(callback) {
        notifyList(itemList)
        notifyMenuClearBin()
        onBindingList()
    }


    @Test fun onClickClearBin() = startCoTest {
        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        viewModel.onClickClearBin()

        coVerifySequence {
            interactor.clearBin()

            callback.apply {
                notifyDataSetChanged(listOf())
                notifyMenuClearBin()
                onBindingList()
            }
        }
    }

    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        viewModel.itemList.addAll(data.itemList)
        assertEquals(data.itemList, viewModel.itemList)

        val p = data.itemList.indices.random()

        viewModel.onClickNote(p)
        verifySequence { callback.startNoteActivity(data.itemList[p]) }
    }

    @Test fun onShowOptionsDialog() {
        val p = Random.nextInt()

        every { callback.getStringArray(R.array.dialog_menu_bin) } returns arrayOf()

        viewModel.onShowOptionsDialog(p)

        verifySequence {
            callback.getStringArray(R.array.dialog_menu_bin)
            callback.showOptionsDialog(arrayOf(), p)
        }
    }

    @Test fun onResultOptionsDialog_onRestore() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Bin.RESTORE)

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onResultOptionsDialog(p, Options.Bin.RESTORE)

        coVerifySequence {
            interactor.restoreNote(item)

            callback.notifyItemRemoved(itemList, p)
            callback.notifyMenuClearBin()
        }
    }

    @Test fun onResultOptionsDialog_onCopy() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Bin.COPY)

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Bin.COPY)

        coVerifySequence { interactor.copy(item) }
    }

    @Test fun onResultOptionsDialog_onClear() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Bin.CLEAR)

        val itemList = data.itemList

        viewModel.itemList.addAll(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = data.itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onResultOptionsDialog(p, Options.Bin.CLEAR)

        coVerifySequence {
            interactor.clearNote(item)

            callback.notifyItemRemoved(itemList, p)
            callback.notifyMenuClearBin()
        }
    }

}