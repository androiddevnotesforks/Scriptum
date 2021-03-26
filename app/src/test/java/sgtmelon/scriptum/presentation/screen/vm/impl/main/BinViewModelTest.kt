package sgtmelon.scriptum.presentation.screen.vm.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.model.annotation.Options
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.getRandomSize
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

    override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)

        viewModel.onDestroy()

        assertNull(viewModel.callback)
        verifySequence { interactor.onDestroy() }
    }


    @Test fun onSetup() {
        viewModel.onSetup()

        verifySequence {
            callback.setupToolbar()
            callback.setupRecycler()
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
        verifySequence { callback.openNoteScreen(itemList[p]) }
    }

    @Test fun onShowOptionsDialog() {
        viewModel.onShowOptionsDialog(Random.nextInt())

        val p = 0
        val itemArray = Array(getRandomSize()) { nextString() }

        val item = mockk<NoteItem>()
        val untitledName = nextString()
        val name = nextString()

        viewModel.itemList.add(item)

        every { item.name } returns ""
        every { callback.getString(R.string.hint_text_name) } returns untitledName
        every { callback.getStringArray(R.array.dialog_menu_bin) } returns itemArray
        viewModel.onShowOptionsDialog(p)

        every { item.name } returns name
        viewModel.onShowOptionsDialog(p)

        verifySequence {
            item.name
            callback.getString(R.string.hint_text_name)
            callback.getStringArray(R.array.dialog_menu_bin)
            callback.showOptionsDialog(untitledName, itemArray, p)

            item.name
            item.name
            callback.getStringArray(R.array.dialog_menu_bin)
            callback.showOptionsDialog(name, itemArray, p)
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