package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.view.inputmethod.EditorInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.domain.model.state.OpenState
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.move
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IRankFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel.Companion.correctPositions
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel.Companion.getNameList
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel.Companion.switchVisible
import kotlin.random.Random

/**
 * Test for [RankViewModel].
 */
@ExperimentalCoroutinesApi
class RankViewModelTest : ParentViewModelTest() {

    private val data = TestData.Rank

    @MockK lateinit var callback: IRankFragment

    @MockK lateinit var interactor: IRankInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val openState = mockkClass(OpenState::class)

    private val viewModel by lazy { RankViewModel(application) }

    override fun setUp() {
        super.setUp()

        every { callback.openState } returns openState

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor, bindInteractor)
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
        val itemList = mutableListOf<RankItem>()

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
            updateList(mutableListOf())
        }
    }

    private fun updateList(itemList: List<RankItem>) = with(callback) {
        notifyList(itemList)
        onBindingList()
    }


    @Test fun onUpdateToolbar() {
        val itemList = data.itemList
        val item = itemList[itemList.indices.random()]

        every { callback.getEnterText() } returns ""
        viewModel.onUpdateToolbar()

        every { callback.getEnterText() } returns "   "
        viewModel.onUpdateToolbar()

        every { callback.getEnterText() } returns item.name
        viewModel.onUpdateToolbar()

        viewModel.itemList.clearAdd(data.itemList)
        viewModel.onUpdateToolbar()

        verifySequence {
            callback.apply {
                getEnterText()
                onBindingToolbar(isClearEnable = false, isAddEnable = false)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = false)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = true)

                getEnterText()
                onBindingToolbar(isClearEnable = true, isAddEnable = false)
            }
        }
    }

    @Test fun onShowRenameDialog() {
        viewModel.onShowRenameDialog(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onShowRenameDialog(p)

        verifySequence {
            callback.dismissSnackbar()
            callback.showRenameDialog(p, item.name, itemList.getNameList())
        }
    }

    @Test fun onResultRenameDialog() = startCoTest {
        val newName = Random.nextString()

        every { callback.getEnterText() } returns newName

        viewModel.onResultRenameDialog(Random.nextInt(), newName)

        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p].apply { name = newName }

        viewModel.onResultRenameDialog(p, newName)

        coVerifySequence {
            interactor.update(item)

            callback.getEnterText()
            callback.onBindingToolbar(isClearEnable = true, isAddEnable = false)

            callback.notifyItemChanged(itemList, p)
        }
    }

    @Test fun onClickEnterCancel() {
        every { callback.clearEnter() } returns data.itemList.random().name

        viewModel.onClickEnterCancel()

        verifySequence { callback.clearEnter() }
    }

    @Test fun onEditorClick() {
        assertFalse(viewModel.onEditorClick(EditorInfo.IME_ACTION_NEXT))

        every { callback.getEnterText() } returns ""
        assertFalse(viewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        every { callback.getEnterText() } returns Random.nextString()
        every { callback.clearEnter() } returns ""
        assertTrue(viewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        val name = Random.nextString()
        var itemList = listOf(data.firstRank.copy(name = name))
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        every { callback.getEnterText() } returns name
        assertFalse(viewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        every { callback.getEnterText() } returns itemList.random().name
        assertFalse(viewModel.onEditorClick(EditorInfo.IME_ACTION_DONE))

        verifySequence {
            callback.getEnterText()

            callback.getEnterText()
            callback.clearEnter()

            callback.getEnterText()

            callback.getEnterText()
        }
    }

    @Test fun onClickEnterAdd_onNameError() {
        every { callback.clearEnter() } returns ""
        viewModel.onClickEnterAdd(Random.nextBoolean())

        val name = Random.nextString()

        val itemList = listOf(data.firstRank.copy(name = name))
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        every { callback.getEnterText() } returns name
        viewModel.onClickEnterAdd(Random.nextBoolean())

        coVerifyAll {
            callback.clearEnter()
            callback.clearEnter()
        }
    }

    @Test fun onClickEnterAdd_onSimple() = startCoTest {
        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val name = Random.nextString()
        val item = data.firstRank.copy(name = name)

        every { callback.clearEnter() } returns name
        coEvery { interactor.insert(name) } returns item

        viewModel.onClickEnterAdd(simpleClick = true)

        val p = itemList.size
        itemList.add(p, item)
        itemList.correctPositions()

        coVerifyAll {
            callback.clearEnter()
            callback.dismissSnackbar()
            interactor.insert(name)
            interactor.updatePosition(itemList, listOf(1, 2))
            callback.scrollToItem(itemList, p, simpleClick = true)
        }
    }

    @Test fun onClickEnterAdd_onLong() = startCoTest {
        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val name = Random.nextString()
        val item = data.firstRank.copy(name = name)

        every { callback.clearEnter() } returns name
        coEvery { interactor.insert(name) } returns item

        viewModel.onClickEnterAdd(simpleClick = false)

        val p = 0
        itemList.add(p, item)
        itemList.correctPositions()

        coVerifySequence {
            callback.clearEnter()
            callback.dismissSnackbar()
            interactor.insert(name)
            interactor.updatePosition(itemList, listOf(1, 2, 3, 5, 4, 6))
            callback.scrollToItem(itemList, p, simpleClick = false)
        }
    }

    @Test fun onClickVisible() = startCoTest {
        viewModel.onClickVisible(Random.nextInt())

        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p].switchVisible()

        viewModel.onClickVisible(p)

        coVerifySequence {
            callback.setList(itemList)

            interactor.update(item)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    @Test fun onLongClickVisible() = startCoTest {
        viewModel.onLongClickVisible(Random.nextInt())

        val itemList = data.itemList
        val p = 0
        val animationArray = booleanArrayOf(false, false, true, false)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onLongClickVisible(p)
        itemList.switchVisible(p)

        coVerifySequence {
            callback.notifyDataSetChanged(itemList, animationArray)

            interactor.update(itemList)
            bindInteractor.notifyNoteBind(callback)
        }
    }

    @Test fun onClickCancel() = startCoTest {
        viewModel.onClickCancel(Random.nextInt())

        val theme = Random.nextInt()
        every { interactor.theme } returns theme

        val itemList = data.secondCorrectList
        val noteIdList = listOf(4L, 6, 1, 2)

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        assertEquals(itemList, viewModel.itemList)
        assertTrue(viewModel.cancelList.isEmpty())

        val p = 1
        val item = itemList.removeAt(p)

        viewModel.onClickCancel(p)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(mutableListOf(Pair(p, item)), viewModel.cancelList)

        coVerifySequence {
            callback.notifyItemRemoved(itemList, p)
            interactor.theme
            callback.showSnackbar(theme)

            interactor.delete(item)
            interactor.updatePosition(itemList, noteIdList)
            bindInteractor.notifyNoteBind(callback)
        }
    }


    @Test fun onSnackbarAction_correct() {
        assertTrue(viewModel.cancelList.isEmpty())
        assertTrue(viewModel.itemList.isEmpty())

        val theme = Random.nextInt()
        every { interactor.theme } returns theme

        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val newItem = mockk<RankItem>()

        val firstPair = Pair(Random.nextInt(), mockk<RankItem>())
        val secondPair = Pair(0, item)

        val itemList = mutableListOf(mockk<RankItem>())
        val cancelList = mutableListOf(firstPair, secondPair)

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coEvery { interactor.insert(item) } returns newItem
        viewModel.onSnackbarAction()

        itemList.add(0, newItem)
        cancelList.removeAt(index = 1)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            interactor.insert(item)
            callback.setList(itemList)

            callback.apply {
                notifyItemInsertedScroll(itemList, secondPair.first)
                interactor.theme
                showSnackbar(theme)
            }
        }
    }

    @Test fun onSnackbarAction_incorrect() {
        assertTrue(viewModel.cancelList.isEmpty())
        assertTrue(viewModel.itemList.isEmpty())

        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val newItem = mockk<RankItem>()

        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf<RankItem>()
        val cancelList = mutableListOf(pair)

        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coEvery { interactor.insert(item) } returns newItem
        viewModel.onSnackbarAction()

        itemList.add(0, newItem)
        cancelList.removeAt(index = 0)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            interactor.insert(item)
            callback.setList(itemList)

            callback.apply {
                notifyItemInsertedScroll(itemList, itemList.indices.last)
                onBindingList()
            }
        }
    }

    @Test fun onSnackbarDismiss() {
        val cancelList = MutableList(size = 5) { Pair(Random.nextInt(), mockk<RankItem>()) }

        viewModel.cancelList.clearAdd(cancelList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarDismiss()

        assertTrue(viewModel.cancelList.isEmpty())
    }


    @Test fun onReceiveUnbindNote() {
        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val hasValue = List(itemList.size) { Random.nextBoolean() }
        val id = itemList.random().noteId.random()
        val updateList = itemList.filter { it.noteId.contains(id) }

        updateList.forEachIndexed { i, item ->
            coEvery { interactor.getBind(item.noteId) } returns hasValue[i]
        }

        viewModel.onReceiveUnbindNote(id)

        updateList.forEachIndexed { i, item ->
            itemList[itemList.indexOf(item)].hasBind = hasValue[i]
        }

        assertEquals(itemList, viewModel.itemList)

        coVerifySequence {
            updateList.forEach {
                interactor.getBind(it.noteId)
            }

            callback.notifyList(itemList)
        }
    }

    @Test fun onTouchDrag() {
        every { openState.value } returns false
        assertTrue(viewModel.onTouchDrag())

        every { openState.value } returns true
        assertFalse(viewModel.onTouchDrag())

        verifySequence {
            callback.openState
            callback.dismissSnackbar()

            callback.openState
        }
    }

    @Test fun onTouchMove() {
        val itemList = data.itemList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val from = 0
        val to = 2

        assertTrue(viewModel.onTouchMove(from, to))
        itemList.move(from, to)

        verifySequence { callback.notifyItemMoved(itemList, from, to) }
    }

    @Test fun onTouchMoveResult() = startCoTest {
                        every { openState.clear() } returns Unit

        val itemList = data.firstCorrectList
        val noteIdList = data.firstCorrectPosition

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onTouchMoveResult()
        itemList.correctPositions()

        coVerifySequence {
            callback.openState

            callback.setList(itemList)
            interactor.updatePosition(itemList, noteIdList)
        }
    }


    @Test fun switchVisible() = with(data) {
        var list = itemList
        var p = 0
        var animationArray = booleanArrayOf(false, false, true, false)

        assertArrayEquals(animationArray, list.switchVisible(p))
        assertVisible(list, p)

        list = itemList
        p = 1
        animationArray = booleanArrayOf(true, true, true, false)

        assertArrayEquals(animationArray, list.switchVisible(p))
        assertVisible(list, p)

        list = itemList
        p = 2
        animationArray = booleanArrayOf(true, false, false, false)

        assertArrayEquals(animationArray, list.switchVisible(p))
        assertVisible(list, p)

        list = itemList
        p = 3
        animationArray = booleanArrayOf(true, false, true, true)

        assertArrayEquals(animationArray, list.switchVisible(p))
        assertVisible(list, p)
    }

    private fun assertVisible(list: List<RankItem>, p: Int) {
        list.forEachIndexed { i, item -> assertEquals(i == p, item.isVisible) }
    }


    @Test fun correctPositions() = with(data) {
        var list: List<RankItem> = itemList
        var noteIdList = list.correctPositions()

        assertTrue(noteIdList.isEmpty())
        assertPositions(list)

        list = firstCorrectList
        noteIdList = list.correctPositions()

        assertEquals(firstCorrectPosition, noteIdList)
        assertPositions(list)

        list = secondCorrectList
        noteIdList = list.correctPositions()

        assertEquals(secondCorrectPosition, noteIdList)
        assertPositions(list)
    }

    private fun assertPositions(list: List<RankItem>) = list.forEachIndexed { i, item ->
        assertEquals(i, item.position)
    }

}