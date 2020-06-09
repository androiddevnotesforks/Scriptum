package sgtmelon.scriptum.presentation.screen.vm.impl.main

import android.view.inputmethod.EditorInfo
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastMock
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

        assertTrue(viewModel.itemList.isEmpty())
        assertTrue(viewModel.cancelList.isEmpty())

        assertFalse(viewModel.inTouchAction)
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
        val nameList = mockk<List<String>>()

        mockkObject(RankViewModel)
        every { RankViewModel.getNameList(itemList) } returns nameList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val p = itemList.indices.random()
        val item = itemList[p]

        viewModel.onShowRenameDialog(p)

        verifySequence {
            callback.dismissSnackbar()

            RankViewModel.getNameList(itemList)
            callback.showRenameDialog(p, item.name, nameList)
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

        val p = itemList.size
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.getNameList(any()) } returns listOf()
        every { RankViewModel.correctPositions(resultList) } returns noteIdList

        viewModel.onClickEnterAdd(simpleClick = true)

        assertEquals(resultList, viewModel.itemList)

        coVerifyAll {
            callback.clearEnter()
            RankViewModel.getNameList(any())
            callback.dismissSnackbar()
            interactor.insert(name)

            RankViewModel.correctPositions(resultList)
            interactor.updatePosition(resultList, noteIdList)
            callback.scrollToItem(resultList, p, simpleClick = true)
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

        val p = 0
        val resultList = ArrayList(itemList).apply { add(p, item) }
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.getNameList(any()) } returns listOf()
        every { RankViewModel.correctPositions(resultList) } returns noteIdList

        viewModel.onClickEnterAdd(simpleClick = false)

        assertEquals(resultList, viewModel.itemList)

        coVerifySequence {
            callback.clearEnter()
            RankViewModel.getNameList(any())
            callback.dismissSnackbar()
            interactor.insert(name)

            RankViewModel.correctPositions(resultList)
            interactor.updatePosition(resultList, noteIdList)
            callback.scrollToItem(resultList, p, simpleClick = false)
        }
    }

    @Test fun onClickEnterAdd_onNull() = startCoTest {
        val itemList = data.itemList
        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        val name = Random.nextString()

        mockkObject(RankViewModel)
        every { RankViewModel.getNameList(itemList) } returns listOf()

        every { callback.clearEnter() } returns name
        coEvery { interactor.insert(name) } returns null

        viewModel.onClickEnterAdd(simpleClick = false)
        viewModel.onClickEnterAdd(simpleClick = true)

        assertEquals(itemList, viewModel.itemList)

        coVerifySequence {
            repeat(times = 2) {
                callback.clearEnter()
                RankViewModel.getNameList(itemList)
                callback.dismissSnackbar()
                interactor.insert(name)
            }
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
        val p = itemList.indices.random()
        val animationArray = BooleanArray(size = 5) { Random.nextBoolean() }

        mockkObject(RankViewModel)
        every { RankViewModel.switchVisible(itemList, p) } returns animationArray

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onLongClickVisible(p)

        coVerifySequence {
            RankViewModel.switchVisible(itemList, p)
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

    @Test fun onItemAnimationFinished() {
        every { openState.clear() } returns Unit

        viewModel.onItemAnimationFinished()

        viewModel.inTouchAction = true
        viewModel.onItemAnimationFinished()

        verifySequence {
            callback.onBindingList()
            callback.openState
            openState.clear()

            callback.onBindingList()
        }
    }


    @Test fun onSnackbarAction_correct() {
        val theme = Random.nextInt()
        every { interactor.theme } returns theme

        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()

        val firstPair = Pair(Random.nextInt(), mockk<RankItem>())
        val secondPair = Pair(0, item)

        val itemList = mutableListOf(mockk<RankItem>())
        val cancelList = mutableListOf(firstPair, secondPair)

        val resultList = ArrayList(itemList).apply { add(0, item) }
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.correctPositions(resultList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 1)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.apply {
                notifyItemInsertedScroll(resultList, secondPair.first)
                interactor.theme
                showSnackbar(theme)
            }

            interactor.insert(item)
            RankViewModel.correctPositions(resultList)
            interactor.updatePosition(resultList, noteIdList)
            callback.setList(resultList)
        }
    }

    @Test fun onSnackbarAction_incorrect() {
        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf(mockk<RankItem>())
        val cancelList = mutableListOf(pair)

        val resultList = ArrayList(itemList).apply { add(item) }
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.correctPositions(resultList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.notifyItemInsertedScroll(resultList, resultList.indices.last)

            interactor.insert(item)
            RankViewModel.correctPositions(resultList)
            interactor.updatePosition(resultList, noteIdList)
            callback.setList(resultList)
        }
    }

    @Test fun onSnackbarAction_onBindingList() {
        viewModel.onSnackbarAction()

        val item = mockk<RankItem>()
        val pair = Pair(Random.nextInt(), item)

        val itemList = mutableListOf<RankItem>()
        val cancelList = mutableListOf(pair)

        val resultList = mutableListOf(item)
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.correctPositions(resultList) } returns noteIdList

        viewModel.cancelList.clearAdd(cancelList)

        assertEquals(itemList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        viewModel.onSnackbarAction()

        cancelList.removeAt(index = 0)

        assertEquals(resultList, viewModel.itemList)
        assertEquals(cancelList, viewModel.cancelList)

        coVerifySequence {
            callback.apply {
                notifyItemInsertedScroll(resultList, resultList.indices.last)
                onBindingList()
            }

            interactor.insert(item)
            RankViewModel.correctPositions(resultList)
            interactor.updatePosition(resultList, noteIdList)
            callback.setList(resultList)
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


    @Test fun onTouchAction() {
        every { openState.value = true } returns Unit
        viewModel.onTouchAction(inAction = true)
        assertTrue(viewModel.inTouchAction)

        every { openState.value = false } returns Unit
        viewModel.onTouchAction(inAction = false)
        assertFalse(viewModel.inTouchAction)

        verifySequence {
            callback.dismissSnackbar()
            callback.openState
            openState.value = true

            callback.openState
            openState.value = false
        }
    }

    @Test fun onTouchGetDrag() {
        every { openState.value } returns false
        assertTrue(viewModel.onTouchGetDrag())

        every { openState.value } returns true
        assertFalse(viewModel.onTouchGetDrag())

        verifySequence {
            callback.openState
            openState.value

            callback.openState
            openState.value
        }
    }

    @Test fun onTouchMove() {
        val itemList = data.itemList
        val from = Random.nextInt()
        val to = Random.nextInt()

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        FastMock.listExtension()
        every { itemList.move(from, to) } returns Unit

        assertTrue(viewModel.onTouchMove(from, to))

        verifySequence {
            itemList.move(from, to)
            callback.notifyItemMoved(itemList, from, to)
        }
    }

    @Test fun onTouchMoveResult() = startCoTest {
        every { openState.clear() } returns Unit

        val itemList = data.firstCorrectList
        val noteIdList = mockk<List<Long>>()

        mockkObject(RankViewModel)
        every { RankViewModel.correctPositions(itemList) } returns noteIdList

        viewModel.itemList.clearAdd(itemList)
        assertEquals(itemList, viewModel.itemList)

        viewModel.onTouchMoveResult()

        coVerifySequence {
            callback.openState

            RankViewModel.correctPositions(itemList)
            callback.setList(itemList)
            interactor.updatePosition(itemList, noteIdList)
        }
    }


    //region Companion test

    @Test fun getNameList() {
        val itemList = List(size = 5) {
            RankItem(id = Random.nextLong(), name = Random.nextString())
        }
        val nameList = itemList.map { it.name.toUpperCase() }

        assertEquals(nameList, RankViewModel.getNameList(itemList))
    }

    @Test fun switchVisible() = with(TestData.Rank) {
        var list = itemList
        var p = 0
        var animationArray = booleanArrayOf(false, false, true, false)

        assertArrayEquals(animationArray, RankViewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 1
        animationArray = booleanArrayOf(true, true, true, false)

        assertArrayEquals(animationArray, RankViewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 2
        animationArray = booleanArrayOf(true, false, false, false)

        assertArrayEquals(animationArray, RankViewModel.switchVisible(list, p))
        assertVisible(list, p)

        list = itemList
        p = 3
        animationArray = booleanArrayOf(true, false, true, true)

        assertArrayEquals(animationArray, RankViewModel.switchVisible(list, p))
        assertVisible(list, p)
    }

    private fun assertVisible(list: List<RankItem>, p: Int) {
        list.forEachIndexed { i, item -> assertEquals(i == p, item.isVisible) }
    }

    @Test fun correctPositions() = with(TestData.Rank) {
        var list: List<RankItem> = itemList
        var noteIdList = RankViewModel.correctPositions(list)

        assertTrue(noteIdList.isEmpty())
        assertPositions(list)

        list = firstCorrectList
        noteIdList = RankViewModel.correctPositions(list)

        assertEquals(firstCorrectPosition, noteIdList)
        assertPositions(list)

        list = secondCorrectList
        noteIdList = RankViewModel.correctPositions(list)

        assertEquals(secondCorrectPosition, noteIdList)
        assertPositions(list)
    }

    private fun assertPositions(list: List<RankItem>) = list.forEachIndexed { i, item ->
        assertEquals(i, item.position)
    }

    //endregion

}