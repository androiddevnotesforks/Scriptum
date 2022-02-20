package sgtmelon.scriptum.presentation.screen.vm.impl.main

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.model.annotation.Options
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentViewModelTest
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IBinFragment
import kotlin.random.Random

/**
 * Test for [BinViewModel].
 */
@ExperimentalCoroutinesApi
class BinViewModelTest : ParentViewModelTest() {

    //region Setup

    @MockK lateinit var callback: IBinFragment

    @MockK lateinit var interactor: IBinInteractor

    private val viewModel by lazy { BinViewModel(application) }
    private val spyViewModel by lazy { spyk(viewModel) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
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
        val itemList = MutableList<NoteItem>(getRandomSize()) { mockk() }

        coEvery { interactor.getCount() } returns itemList.size
        coEvery { interactor.getList() } returns itemList

        viewModel.itemList.clear()
        viewModel.onUpdateData()

        coVerifySequence {
            interactor.getCount()
            callback.hideEmptyInfo()
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
            interactor.getCount()
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty_getNotEmpty() = startCoTest {
        val startList = List<NoteItem>(getRandomSize()) { mockk() }
        val returnList = MutableList<NoteItem>(getRandomSize()) { mockk() }

        coEvery { interactor.getCount() } returns returnList.size
        coEvery { interactor.getList() } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)

        viewModel.onUpdateData()

        coVerifySequence {
            updateList(any())
            interactor.getCount()
            interactor.getList()
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
            interactor.clearBin()

            callback.apply {
                notifyDataSetChanged(listOf())
                notifyMenuClearBin()
                onBindingList()
            }
        }

        assertTrue(viewModel.itemList.isEmpty())
    }

    @Test fun onClickNote() {
        viewModel.onClickNote(Random.nextInt())

        val itemList = List<NoteItem>(getRandomSize()) { mockk() }
        val index = itemList.indices.random()
        val item = itemList[index]

        viewModel.itemList.clearAdd(itemList)
        viewModel.onClickNote(index)

        verifySequence { callback.openNoteScreen(item) }

        assertEquals(itemList, viewModel.itemList)
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
            interactor.restoreNote(item)

            callback.notifyItemRemoved(resultList, index)
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

        coEvery { interactor.copy(item) } returns text

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onMenuCopy(index)

        coVerifySequence {
            interactor.copy(item)
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
            interactor.clearNote(item)

            callback.notifyItemRemoved(resultList, index)
            callback.notifyMenuClearBin()
        }

        assertEquals(resultList, viewModel.itemList)
    }

}