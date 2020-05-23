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
import sgtmelon.scriptum.extension.clearAdd
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
        val themeList = listOf(Theme.LIGHT, Random.nextInt())

        themeList.forEach {
            every { interactor.theme } returns it
            viewModel.onSetup()
        }

        verifySequence {
            themeList.forEach {
                callback.setupToolbar()
                interactor.theme
                callback.setupRecycler(it)
            }
        }
    }

    @Test fun onUpdateData_startEmpty_getNotEmpty() = startCoTest {
        val itemList = data.itemList

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()
            callback.showProgress()
            interactor.getList()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startEmpty_getEmpty() = startCoTest {
        val itemList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns itemList.size

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
            interactor.getCount()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = data.itemList
        val returnList = data.itemList.apply { shuffle() }

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
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
        val startList = data.itemList
        val returnList = mutableListOf<NoteItem>()

        coEvery { interactor.getCount() } returns returnList.size

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)
        viewModel.onUpdateData()

        coVerifySequence {
            callback.beforeLoad()
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
        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

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

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()

        viewModel.onClickNote(p)
        verifySequence { callback.startNoteActivity(itemList[p]) }
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
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
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
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onResultOptionsDialog(p, Options.Bin.COPY)

        coVerifySequence { interactor.copy(item) }
    }

    @Test fun onResultOptionsDialog_onClear() = startCoTest {
        viewModel.onResultOptionsDialog(Random.nextInt(), Options.Bin.CLEAR)

        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList.removeAt(p)

        viewModel.onResultOptionsDialog(p, Options.Bin.CLEAR)

        coVerifySequence {
            interactor.clearNote(item)

            callback.notifyItemRemoved(itemList, p)
            callback.notifyMenuClearBin()
        }
    }

}